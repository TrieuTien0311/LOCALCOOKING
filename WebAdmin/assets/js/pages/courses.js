document.addEventListener("DOMContentLoaded", () => {
  loadCourses();

  // Add course button - open modal instead of redirect
  document.getElementById("btnAddCourse").addEventListener("click", () => {
    if (courseFormModal) {
      courseFormModal.showCreate();
    }
  });

  // Search functionality
  document.getElementById("searchCourse").addEventListener(
    "input",
    debounce(() => {
      loadCourses();
    }, 500)
  );
});

// Debounce helper
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

async function loadCourses() {
  const grid = document.getElementById("coursesGrid");
  grid.innerHTML = '<div class="loading-state"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...</div>';

  try {
    const searchQuery = document
      .getElementById("searchCourse")
      .value.toLowerCase()
      .trim();

    // Fetch from API
    const response = await apiService.get("/khoahoc");

    console.log("=== DEBUG COURSES API ===");
    console.log("Raw response:", response);
    console.log("Response type:", typeof response);
    console.log("Is array:", Array.isArray(response));

    if (!response || response.success === false) {
      throw new Error(response?.message || "Không thể tải dữ liệu khóa học");
    }

    let courses = Array.isArray(response) ? response : [];
    
    console.log("Total courses:", courses.length);
    
    // Log first course detail
    if (courses.length > 0) {
      console.log("First course detail:", JSON.stringify(courses[0], null, 2));
      console.log("First course lichTrinhList:", courses[0].lichTrinhList);
      if (courses[0].lichTrinhList && courses[0].lichTrinhList.length > 0) {
        console.log("First schedule:", courses[0].lichTrinhList[0]);
      }
    }

    // Filter by search query
    if (searchQuery) {
      courses = courses.filter((c) => {
        const name = (c.tenKhoaHoc || "").toLowerCase();
        const teacher = (c.tenGiaoVien || "").toLowerCase();
        return name.includes(searchQuery) || teacher.includes(searchQuery);
      });
    }

    if (courses.length === 0) {
      grid.innerHTML = `
        <div class="empty-state">
          <i class="fas fa-book-open"></i>
          <h3>Không tìm thấy khóa học</h3>
          <p>Chưa có khóa học nào hoặc không tìm thấy kết quả phù hợp</p>
        </div>
      `;
      return;
    }

    grid.innerHTML = courses
      .map(
        (c) => `
        <div class="course-card">
          <div class="course-image">
            <img src="${c.hinhAnh ? `http://localhost:8080/images/${c.hinhAnh}` : 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="400" height="300"%3E%3Crect fill="%23f1f5f9" width="400" height="300"/%3E%3Ctext fill="%2394a3b8" font-family="Arial" font-size="24" x="50%25" y="50%25" text-anchor="middle" dominant-baseline="middle"%3EKhóa học%3C/text%3E%3C/svg%3E'}" 
                 alt="${c.tenKhoaHoc || 'Khóa học'}" 
                 onerror="if(this.src!=='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22400%22 height=%22300%22%3E%3Crect fill=%22%23f1f5f9%22 width=%22400%22 height=%22300%22/%3E%3Ctext fill=%22%2394a3b8%22 font-family=%22Arial%22 font-size=%2224%22 x=%2250%25%22 y=%2250%25%22 text-anchor=%22middle%22 dominant-baseline=%22middle%22%3EKhóa học%3C/text%3E%3C/svg%3E')this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22400%22 height=%22300%22%3E%3Crect fill=%22%23f1f5f9%22 width=%22400%22 height=%22300%22/%3E%3Ctext fill=%22%2394a3b8%22 font-family=%22Arial%22 font-size=%2224%22 x=%2250%25%22 y=%2250%25%22 text-anchor=%22middle%22 dominant-baseline=%22middle%22%3EKhóa học%3C/text%3E%3C/svg%3E'" />
            ${c.coUuDai ? '<span class="course-badge">-' + (c.phanTramGiam || 10) + '%</span>' : ''}
          </div>
          <div class="course-content">
            <div class="course-header">
              <h3 class="course-title">${c.tenKhoaHoc || "Chưa có tên"}</h3>
              <div class="course-rating">
                <i class="fas fa-star"></i>
                <span>${c.saoTrungBinh ? c.saoTrungBinh.toFixed(1) : "0.0"}</span>
              </div>
            </div>
            
            <p class="course-desc">${truncateText(c.moTa || c.gioiThieu || "", 60)}</p>
            
            <div class="course-teacher">
              <i class="fas fa-chalkboard-teacher"></i>
              <span>${getTeacherName(c)}</span>
            </div>

            <div class="course-info-row">
              <div class="info-badge">
                <i class="fas fa-calendar-alt"></i>
                <span>${c.lichTrinhList ? c.lichTrinhList.length : 0}</span>
              </div>
              <div class="info-badge">
                <i class="fas fa-utensils"></i>
                <span>${c.danhMucMonAnList ? c.danhMucMonAnList.reduce((sum, dm) => sum + (dm.danhSachMon?.length || 0), 0) : 0}</span>
              </div>
              <div class="info-badge">
                <i class="fas fa-comment"></i>
                <span>${c.soLuongDanhGia || 0}</span>
              </div>
            </div>

            <div class="course-footer">
              <div class="course-price">
                ${c.coUuDai && c.giaSauGiam ? `
                  <span class="price-old">${formatPrice(c.giaTien)}</span>
                  <span class="price-new">${formatPrice(c.giaSauGiam)}</span>
                ` : `
                  <span class="price-single">${formatPrice(c.giaTien)}</span>
                `}
              </div>
              <div class="course-actions">
                <button class="btn-action btn-view" onclick="viewCourse(${c.maKhoaHoc})" title="Xem">
                  <i class="fas fa-eye"></i>
                </button>
                <button class="btn-action btn-edit" onclick="editCourse(${c.maKhoaHoc})" title="Sửa">
                  <i class="fas fa-edit"></i>
                </button>
                <button class="btn-action btn-delete" onclick="deleteCourse(${c.maKhoaHoc}, '${escapeHtml(c.tenKhoaHoc)}')" title="Xóa">
                  <i class="fas fa-trash-alt"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      `
      )
      .join("");
  } catch (error) {
    console.error("Error loading courses:", error);
    grid.innerHTML = `
      <div class="error-state">
        <i class="fas fa-exclamation-circle"></i>
        <h3>Lỗi tải dữ liệu</h3>
        <p>${error.message}</p>
      </div>
    `;
  }
}

function getTeacherName(course) {
  console.log("Getting teacher for course:", course.tenKhoaHoc);
  console.log("  - lichTrinhList:", course.lichTrinhList);
  
  // Nếu có lichTrinhList, lấy tên giáo viên từ lịch trình đầu tiên
  if (course.lichTrinhList && course.lichTrinhList.length > 0) {
    const firstSchedule = course.lichTrinhList[0];
    console.log("  - First schedule:", firstSchedule);
    console.log("  - tenGiaoVien:", firstSchedule.tenGiaoVien);
    
    if (firstSchedule.tenGiaoVien) {
      return firstSchedule.tenGiaoVien;
    }
  }
  
  console.log("  - No teacher found, returning default");
  return "Chưa có giáo viên";
}

function truncateText(text, maxLength) {
  if (!text || text === "-") return "";
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + "...";
}

function formatPrice(price) {
  if (!price) return "Liên hệ";
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
}

function escapeHtml(text) {
  if (!text) return "";
  return text.replace(/'/g, "\\'").replace(/"/g, "&quot;");
}

window.viewCourse = function (courseId) {
  if (courseDetailModal) {
    courseDetailModal.show(courseId);
  }
};

window.editCourse = function (courseId) {
  if (courseFormModal) {
    courseFormModal.showEdit(courseId);
  }
};

window.deleteCourse = async function (courseId, courseName) {
  if (!confirm(`Bạn có chắc muốn xóa khóa học "${courseName}"?\n\nLưu ý: Các lịch trình và đơn đặt lịch liên quan sẽ bị ảnh hưởng.`)) {
    return;
  }

  try {
    const response = await apiService.delete(`/khoahoc/${courseId}`);

    if (response === null || response === undefined || response.success !== false) {
      if (typeof showToast === "function") {
        showToast("Xóa khóa học thành công", "success");
      } else {
        alert("Xóa khóa học thành công");
      }
      loadCourses();
    } else {
      throw new Error(response?.message || "Xóa thất bại");
    }
  } catch (error) {
    console.error("Error deleting course:", error);
    alert(`Lỗi: ${error.message || "Không thể xóa khóa học"}`);
  }
};
