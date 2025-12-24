/**
 * Main JavaScript
 * Xử lý các chức năng chung của ứng dụng
 */

// ==================== GLOBAL VARIABLES ====================
let currentUser = null;

// ==================== INITIALIZATION ====================
document.addEventListener("DOMContentLoaded", function () {
  // Check authentication
  // checkAuth();

  // Load user info
  loadUserInfo();

  // Setup event listeners
  setupEventListeners();
});

// ==================== AUTHENTICATION ====================

/**
 * Check if user is authenticated
 */
function checkAuth() {
  if (!isLoggedIn()) {
    // Redirect to login page if not authenticated
    if (!window.location.pathname.includes("login.html")) {
      window.location.href = "login.html";
    }
  }
}

/**
 * Load user information
 */
function loadUserInfo() {
  const user = getUserSession();
  if (user) {
    currentUser = user;

    // Update UI with user info
    const userNameElement = document.getElementById("userName");
    if (userNameElement) {
      userNameElement.textContent = user.hoTen || user.tenDangNhap;
    }

    // Check if user is admin
    if (!isAdmin()) {
      showToast("Bạn không có quyền truy cập trang này", "error");
      setTimeout(() => {
        logout();
      }, 2000);
    }
  }
}

/**
 * Logout
 */
function logout() {
  showConfirm("Bạn có chắc muốn đăng xuất?", () => {
    clearUserSession();
    // Check if we're in pages folder or root
    const isInPagesFolder = window.location.pathname.includes('/pages/');
    const loginUrl = isInPagesFolder ? '../login.html' : 'login.html';
    window.location.href = loginUrl;
  });
}

// ==================== SIDEBAR ====================

/**
 * Toggle sidebar
 */
function toggleSidebar() {
  const sidebar = document.getElementById("sidebar");
  const mainContent = document.querySelector(".main-content");

  if (sidebar && mainContent) {
    sidebar.classList.toggle("collapsed");
    mainContent.classList.toggle("expanded");
  }
}

/**
 * Set active menu item
 */
function setActiveMenu() {
  const currentPath = window.location.pathname;
  const menuLinks = document.querySelectorAll(".sidebar-nav a");

  menuLinks.forEach((link) => {
    link.classList.remove("active");
    if (
      link.getAttribute("href") === currentPath ||
      currentPath.includes(link.getAttribute("href"))
    ) {
      link.classList.add("active");
    }
  });
}

// ==================== EVENT LISTENERS ====================

/**
 * Setup global event listeners
 */
function setupEventListeners() {
  // Set active menu
  setActiveMenu();

  // Handle window resize
  window.addEventListener("resize", handleResize);

  // Handle online/offline status
  window.addEventListener("online", () => {
    showToast("Kết nối internet đã được khôi phục", "success");
  });

  window.addEventListener("offline", () => {
    showToast("Mất kết nối internet", "error");
  });
}

/**
 * Handle window resize
 */
function handleResize() {
  const width = window.innerWidth;
  const sidebar = document.getElementById("sidebar");
  const mainContent = document.querySelector(".main-content");

  // Auto collapse sidebar on mobile
  if (width < 768) {
    if (sidebar && !sidebar.classList.contains("collapsed")) {
      sidebar.classList.add("collapsed");
      mainContent.classList.add("expanded");
    }
  }
}

// ==================== MODAL ====================

/**
 * Open modal
 */
function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.style.display = "flex";
    document.body.classList.add("modal-open");
    document.body.style.overflow = "hidden";
  }
}

/**
 * Close modal
 */
function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.style.display = "none";
    document.body.classList.remove("modal-open");
    document.body.style.overflow = "auto";
  }
}

/**
 * Show loading overlay
 */
function showLoading(message = "Đang tải...") {
  let loading = document.getElementById("loadingOverlay");
  if (!loading) {
    loading = document.createElement("div");
    loading.id = "loadingOverlay";
    loading.className = "loading-overlay show";
    loading.innerHTML = `
      <div class="loading-content">
        <div class="spinner"></div>
        <p class="loading-text">${message}</p>
      </div>
    `;
    document.body.appendChild(loading);
  } else {
    loading.classList.add("show");
    const text = loading.querySelector(".loading-text");
    if (text) text.textContent = message;
  }
}

/**
 * Hide loading overlay
 */
function hideLoading() {
  const loading = document.getElementById("loadingOverlay");
  if (loading) {
    loading.classList.remove("show");
  }
}

/**
 * Close modal on outside click
 */
window.addEventListener("click", function (event) {
  if (event.target.classList.contains("modal")) {
    event.target.style.display = "none";
    document.body.classList.remove("modal-open");
  }
});

// ==================== TABLE ====================

/**
 * Handle table sort
 */
function handleTableSort(columnKey, tableId = "dataTable") {
  const table = document.getElementById(tableId);
  if (!table) return;

  const tbody = table.querySelector("tbody");
  const rows = Array.from(tbody.querySelectorAll("tr"));

  // Get current sort order
  const currentOrder = table.dataset.sortOrder || "asc";
  const newOrder = currentOrder === "asc" ? "desc" : "asc";

  // Sort rows
  rows.sort((a, b) => {
    const aValue =
      a.querySelector(`td[data-key="${columnKey}"]`)?.textContent || "";
    const bValue =
      b.querySelector(`td[data-key="${columnKey}"]`)?.textContent || "";

    if (newOrder === "asc") {
      return aValue.localeCompare(bValue, "vi");
    } else {
      return bValue.localeCompare(aValue, "vi");
    }
  });

  // Update table
  tbody.innerHTML = "";
  rows.forEach((row) => tbody.appendChild(row));

  // Update sort order
  table.dataset.sortOrder = newOrder;

  // Update sort icon
  updateSortIcon(columnKey, newOrder);
}

/**
 * Update sort icon
 */
function updateSortIcon(columnKey, order) {
  const headers = document.querySelectorAll("th[data-key]");
  headers.forEach((header) => {
    const icon = header.querySelector(".sort-icon");
    if (icon) {
      if (header.dataset.key === columnKey) {
        icon.className = `fas fa-sort-${
          order === "asc" ? "up" : "down"
        } sort-icon`;
      } else {
        icon.className = "fas fa-sort sort-icon";
      }
    }
  });
}

// ==================== SEARCH & FILTER ====================

/**
 * Handle search
 */
function handleSearch(searchValue, tableId = "dataTable") {
  const table = document.getElementById(tableId);
  if (!table) return;

  const tbody = table.querySelector("tbody");
  const rows = tbody.querySelectorAll("tr");

  const searchLower = searchValue.toLowerCase().trim();

  rows.forEach((row) => {
    const text = row.textContent.toLowerCase();
    if (text.includes(searchLower)) {
      row.style.display = "";
    } else {
      row.style.display = "none";
    }
  });
}

/**
 * Handle filter
 */
function handleFilter(filterKey, filterValue, tableId = "dataTable") {
  const table = document.getElementById(tableId);
  if (!table) return;

  const tbody = table.querySelector("tbody");
  const rows = tbody.querySelectorAll("tr");

  rows.forEach((row) => {
    const cell = row.querySelector(`td[data-key="${filterKey}"]`);
    if (!cell) return;

    if (!filterValue || cell.textContent === filterValue) {
      row.style.display = "";
    } else {
      row.style.display = "none";
    }
  });
}

// ==================== PAGINATION ====================

/**
 * Setup pagination
 */
function setupPagination(totalItems, itemsPerPage, currentPage, onPageChange) {
  const totalPages = Math.ceil(totalItems / itemsPerPage);
  const paginationContainer = document.getElementById("pagination");

  if (!paginationContainer || totalPages <= 1) {
    if (paginationContainer) paginationContainer.innerHTML = "";
    return;
  }

  let html = '<ul class="pagination">';

  // Previous button
  if (currentPage > 1) {
    html += `<li><a href="#" onclick="handlePageChange(${
      currentPage - 1
    }); return false;">«</a></li>`;
  }

  // Page numbers
  for (let i = 1; i <= totalPages; i++) {
    if (i === currentPage) {
      html += `<li class="active"><span>${i}</span></li>`;
    } else if (
      i === 1 ||
      i === totalPages ||
      (i >= currentPage - 2 && i <= currentPage + 2)
    ) {
      html += `<li><a href="#" onclick="handlePageChange(${i}); return false;">${i}</a></li>`;
    } else if (i === currentPage - 3 || i === currentPage + 3) {
      html += "<li><span>...</span></li>";
    }
  }

  // Next button
  if (currentPage < totalPages) {
    html += `<li><a href="#" onclick="handlePageChange(${
      currentPage + 1
    }); return false;">»</a></li>`;
  }

  html += "</ul>";
  paginationContainer.innerHTML = html;

  // Store callback
  window.handlePageChange = onPageChange;
}

// ==================== ERROR HANDLING ====================

/**
 * Handle API error
 */
function handleApiError(error) {
  console.error("API Error:", error);

  if (error.success === false) {
    showToast(error.message, "error");
  } else if (error.message) {
    showToast(error.message, "error");
  } else {
    showToast(ERROR_MESSAGES.UNKNOWN, "error");
  }
}

// ==================== UTILITY FUNCTIONS ====================

/**
 * Refresh current page
 */
function refreshPage() {
  window.location.reload();
}

/**
 * Go back
 */
function goBack() {
  window.history.back();
}

/**
 * Scroll to top
 */
function scrollToTop() {
  window.scrollTo({ top: 0, behavior: "smooth" });
}

// Add scroll to top button functionality
window.addEventListener("scroll", function () {
  const scrollBtn = document.getElementById("scrollTopBtn");
  if (scrollBtn) {
    if (window.pageYOffset > 300) {
      scrollBtn.style.display = "block";
    } else {
      scrollBtn.style.display = "none";
    }
  }
});
