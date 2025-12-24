/**
 * Dashboard Page JavaScript
 * Load and display dashboard statistics
 */

// ==================== GLOBAL VARIABLES ====================
let dashboardData = {
  totalUsers: 0,
  totalCourses: 0,
  totalBookings: 0,
  totalRevenue: 0,
  recentActivities: [],
};

// ==================== INITIALIZATION ====================
document.addEventListener("DOMContentLoaded", function () {
  // Load dashboard data
  loadDashboardStats();
  loadRecentActivities();

  // Add animations to stat cards
  animateStatCards();

  // Set up auto-refresh (every 30 seconds)
  setInterval(loadDashboardStats, 30000);
});

// ==================== LOAD DASHBOARD STATISTICS ====================

/**
 * Load all dashboard statistics
 */
async function loadDashboardStats() {
  try {
    // Load statistics in parallel
    await Promise.all([
      loadUserStats(),
      loadCourseStats(),
      loadBookingStats(),
      loadRevenueStats(),
    ]);
  } catch (error) {
    console.error("Error loading dashboard stats:", error);
    handleApiError(error);
  }
}

/**
 * Load user statistics
 */
async function loadUserStats() {
  try {
    const response = await apiService.get(API_ENDPOINTS.USERS.GET_ALL);

    if (response && Array.isArray(response)) {
      dashboardData.totalUsers = response.length;
      updateStatCard("totalUsers", dashboardData.totalUsers);
    }
  } catch (error) {
    console.error("Error loading user stats:", error);
  }
}

/**
 * Load course statistics
 */
async function loadCourseStats() {
  try {
    const response = await apiService.get(API_ENDPOINTS.COURSES.GET_ALL);

    if (response && Array.isArray(response)) {
      dashboardData.totalCourses = response.length;
      updateStatCard("totalCourses", dashboardData.totalCourses);
    }
  } catch (error) {
    console.error("Error loading course stats:", error);
  }
}

/**
 * Load booking statistics
 */
async function loadBookingStats() {
  try {
    const response = await apiService.get('/datlich/dashboard');

    if (response && Array.isArray(response)) {
      dashboardData.totalBookings = response.length;
      dashboardData.bookingsData = response; // Store for other uses
      updateStatCard("totalBookings", dashboardData.totalBookings);
    }
  } catch (error) {
    console.error("Error loading booking stats:", error);
    // Fallback to basic endpoint
    try {
      const fallback = await apiService.get(API_ENDPOINTS.BOOKINGS.GET_ALL);
      if (fallback && Array.isArray(fallback)) {
        dashboardData.totalBookings = fallback.length;
        updateStatCard("totalBookings", dashboardData.totalBookings);
      }
    } catch (e) {
      console.error("Fallback also failed:", e);
    }
  }
}

/**
 * Load revenue statistics
 */
async function loadRevenueStats() {
  try {
    // Use cached data from loadBookingStats if available
    let bookings = dashboardData.bookingsData;
    
    if (!bookings) {
      const response = await apiService.get('/datlich/dashboard');
      bookings = response && Array.isArray(response) ? response : [];
    }

    // Calculate total revenue from completed bookings
    const totalRevenue = bookings
      .filter((booking) => booking.trangThai === 'DaHoanThanh' || booking.daThanhToan)
      .reduce((sum, booking) => sum + (parseFloat(booking.tongTien) || 0), 0);

    dashboardData.totalRevenue = totalRevenue;
    
    // Format revenue for display (e.g., 45.6M VNĐ)
    let displayValue;
    if (totalRevenue >= 1000000000) {
      displayValue = (totalRevenue / 1000000000).toFixed(1) + 'B';
    } else if (totalRevenue >= 1000000) {
      displayValue = (totalRevenue / 1000000).toFixed(1) + 'M';
    } else if (totalRevenue >= 1000) {
      displayValue = Math.round(totalRevenue / 1000) + 'K';
    } else {
      displayValue = totalRevenue.toLocaleString('vi-VN');
    }
    
    updateStatCard("totalRevenue", displayValue);
    
    // Update today's revenue card
    const todayRevenue = calculateTodayRevenue(bookings);
    const todayRevenueElement = document.querySelector('.order-card-amount');
    if (todayRevenueElement) {
      todayRevenueElement.textContent = '₫ ' + todayRevenue.toLocaleString('vi-VN');
    }
  } catch (error) {
    console.error("Error loading revenue stats:", error);
  }
}

/**
 * Calculate today's revenue
 */
function calculateTodayRevenue(bookings) {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  return bookings
    .filter((booking) => {
      const bookingDate = new Date(booking.ngayDat);
      bookingDate.setHours(0, 0, 0, 0);
      return bookingDate.getTime() === today.getTime() && 
             (booking.trangThai === 'DaHoanThanh' || booking.daThanhToan);
    })
    .reduce((sum, booking) => sum + (parseFloat(booking.tongTien) || 0), 0);
}

/**
 * Update stat card display
 */
function updateStatCard(cardId, value) {
  const element = document.getElementById(cardId);
  if (element) {
    // Animate number change
    animateNumber(element, value);
  }
}

/**
 * Animate number change
 */
function animateNumber(element, targetValue) {
  // If value is string (like currency), just set it
  if (typeof targetValue === "string") {
    element.textContent = targetValue;
    return;
  }

  const currentValue = parseInt(element.textContent) || 0;
  const difference = targetValue - currentValue;
  const duration = 1000; // 1 second
  const steps = 30;
  const stepValue = difference / steps;
  const stepDuration = duration / steps;

  let currentStep = 0;

  const interval = setInterval(() => {
    currentStep++;
    const newValue = Math.round(currentValue + stepValue * currentStep);
    element.textContent = newValue;

    if (currentStep >= steps) {
      clearInterval(interval);
      element.textContent = targetValue;
    }
  }, stepDuration);
}

// ==================== RECENT ACTIVITIES ====================

/**
 * Load recent activities
 */
async function loadRecentActivities() {
  try {
    const tbody = document.getElementById("recentActivities");
    if (!tbody) return;

    // Show loading
    tbody.innerHTML =
      '<tr><td colspan="4" class="text-center">Đang tải...</td></tr>';

    // Load recent bookings with full details
    let bookings = dashboardData.bookingsData;
    
    if (!bookings) {
      const response = await apiService.get('/datlich/dashboard');
      bookings = response && Array.isArray(response) ? response : [];
    }

    if (!bookings || bookings.length === 0) {
      tbody.innerHTML =
        '<tr><td colspan="4" class="text-center">Chưa có hoạt động nào</td></tr>';
      return;
    }

    // Sort by date (newest first) and take first 10
    const recentBookings = bookings
      .sort(
        (a, b) =>
          new Date(b.ngayDat || b.createdAt) -
          new Date(a.ngayDat || a.createdAt)
      )
      .slice(0, 10);

    // Generate table rows
    const rows = recentBookings
      .map((booking) => {
        // Ưu tiên lấy tên từ tenNguoiDat, nếu không có thì lấy từ hoTenHocVien
        const userName = booking.tenNguoiDat || booking.hoTenHocVien || "Khách hàng";
        const email = booking.emailNguoiDat || booking.emailHocVien || "";
        const phone = booking.sdtNguoiDat || "";
        const courseName = booking.tenKhoaHoc || "Khóa học";
        const bookingDate = booking.ngayDat || booking.createdAt;
        const displayDate = formatDisplayDate(bookingDate);
        const status = getBookingStatusBadge(booking.trangThai);

        return `
          <tr>
            <td>
              <div class="table-avatar">
                <img src="https://i.pravatar.cc/100?u=${encodeURIComponent(email || userName)}" alt="Avatar" />
                <div class="table-avatar-info">
                  <h4>${userName}</h4>
                  <span>${email || phone || 'Không có thông tin'}</span>
                </div>
              </div>
            </td>
            <td>Đặt lịch khóa học "${courseName}"</td>
            <td>${displayDate}</td>
            <td>${status}</td>
          </tr>
        `;
      })
      .join("");

    tbody.innerHTML = rows;
  } catch (error) {
    console.error("Error loading recent activities:", error);
    const tbody = document.getElementById("recentActivities");
    if (tbody) {
      tbody.innerHTML =
        '<tr><td colspan="4" class="text-center text-danger">Lỗi tải dữ liệu</td></tr>';
    }
  }
}

/**
 * Format date for display
 */
function formatDisplayDate(date) {
  if (!date) return "";
  const d = new Date(date);
  const day = String(d.getDate()).padStart(2, '0');
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const year = d.getFullYear();
  return `${day}/${month}/${year}`;
}

/**
 * Get booking status badge
 */
function getBookingStatusBadge(status) {
  const statusMap = {
    'DatTruoc': '<span class="badge badge-warning">Đặt trước</span>',
    'DaHoanThanh': '<span class="badge badge-success">Hoàn thành</span>',
    'DaHuy': '<span class="badge badge-danger">Đã hủy</span>',
  };
  return statusMap[status] || '<span class="badge badge-info">Mới</span>';
}

/**
 * Time ago helper
 */
function timeAgo(date) {
  if (!date) return "";
  const now = new Date();
  const past = new Date(date);
  const diffInSeconds = Math.floor((now - past) / 1000);

  if (diffInSeconds < 60) return "Vừa xong";
  if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)} phút trước`;
  if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)} giờ trước`;
  if (diffInSeconds < 2592000) return `${Math.floor(diffInSeconds / 86400)} ngày trước`;
  return formatDate(date, "DD/MM/YYYY");
}

// ==================== ANIMATIONS ====================

/**
 * Animate stat cards on load
 */
function animateStatCards() {
  const statCards = document.querySelectorAll(".stat-card");

  statCards.forEach((card, index) => {
    card.style.opacity = "0";
    card.style.transform = "translateY(20px)";

    setTimeout(() => {
      card.style.transition = "all 0.5s ease";
      card.style.opacity = "1";
      card.style.transform = "translateY(0)";
    }, index * 100);
  });
}

// ==================== REAL-TIME UPDATES ====================

/**
 * Simulate real-time updates (can be replaced with WebSocket)
 */
function startRealTimeUpdates() {
  // Update stats every 30 seconds
  setInterval(() => {
    loadDashboardStats();
  }, 30000);

  // Update activities every 60 seconds
  setInterval(() => {
    loadRecentActivities();
  }, 60000);
}

// ==================== EXPORT FUNCTIONS ====================

/**
 * Export dashboard data to CSV
 */
function exportDashboardData() {
  const data = [
    { label: "Người Dùng", value: dashboardData.totalUsers },
    { label: "Khóa Học", value: dashboardData.totalCourses },
    { label: "Đặt Lịch", value: dashboardData.totalBookings },
    { label: "Doanh Thu", value: dashboardData.totalRevenue },
  ];

  exportToCSV(data, "dashboard-stats.csv");
}

/**
 * Print dashboard
 */
function printDashboard() {
  window.print();
}

// ==================== EVENT LISTENERS ====================

// Add click handlers to stat cards
document.addEventListener("DOMContentLoaded", function () {
  const statCards = document.querySelectorAll(".stat-card");

  statCards.forEach((card, index) => {
    card.addEventListener("click", function () {
      // Navigate to corresponding page
      const pages = [
        "pages/users.html",
        "pages/courses.html",
        "pages/bookings.html",
        "pages/payments.html",
      ];

      if (pages[index]) {
        window.location.href = pages[index];
      }
    });
  });
});

// ==================== UTILITY FUNCTIONS ====================

/**
 * Refresh dashboard data
 */
function refreshDashboard() {
  showToast("Đang làm mới dữ liệu...", "info");
  loadDashboardStats();
  loadRecentActivities();
}

/**
 * Get chart data for visualization
 */
async function getChartData() {
  try {
    const bookings = await apiService.get(API_ENDPOINTS.BOOKINGS.GET_ALL);

    // Group bookings by date
    const bookingsByDate = {};
    bookings.forEach((booking) => {
      const date = formatDate(booking.ngayThamGia, "YYYY-MM-DD");
      bookingsByDate[date] = (bookingsByDate[date] || 0) + 1;
    });

    return bookingsByDate;
  } catch (error) {
    console.error("Error getting chart data:", error);
    return {};
  }
}

// Start real-time updates
// startRealTimeUpdates(); // Uncomment to enable auto-refresh
