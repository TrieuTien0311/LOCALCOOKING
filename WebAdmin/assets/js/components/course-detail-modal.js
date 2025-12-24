class CourseDetailModal {
  constructor() {
    this.modal = null;
    this.modalBody = null;
    this.init();
  }

  init() {
    // Create modal if not exists
    if (!document.getElementById("courseDetailModal")) {
      this.createModal();
    }
    this.modal = new Modal("courseDetailModal");
    this.modalBody = document.getElementById("courseDetailBody");
  }

  createModal() {
    const modalHTML = `
      <div id="courseDetailModal" class="modal">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h2 id="courseDetailTitle">Chi Tiết Khóa Học</h2>
              <span class="close">&times;</span>
            </div>
            <div class="modal-body" id="courseDetailBody">
              <!-- Content loaded via JS -->
            </div>
          </div>
        </div>
      </div>
    `;
    document.body.insertAdjacentHTML("beforeend", modalHTML);
  }

  async show(courseId) {
    try {
      this.modalBody.innerHTML =
        '<div class="loading-state"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>';
      this.modal.show();

      const response = await apiService.get(`/khoahoc/${courseId}`);

      if (!response) {
        throw new Error("Không tìm thấy khóa học");
      }

      this.renderCourseDetail(response);
    } catch (error) {
      console.error("Error loading course detail:", error);
      this.modalBody.innerHTML = `
        <div class="error-state">
          <i class="fas fa-exclamation-circle"></i>
          <h3>Lỗi tải dữ liệu</h3>
          <p>${error.message}</p>
        </div>
      `;
    }
  }

  renderCourseDetail(course) {
    const teacherName = this.getTeacherName(course);
    const dishCount = this.getDishCount(course);

    this.modalBody.innerHTML = `
      <div class="course-detail">
        <div class="detail-image">
          <img src="${this.getImageUrl(course.hinhAnh)}" 
               alt="${course.tenKhoaHoc}" />
          ${
            course.coUuDai
              ? '<span class="detail-badge">-' +
                (course.phanTramGiam || 10) +
                "%</span>"
              : ""
          }
        </div>

        <div class="detail-content">
          <h3 class="detail-title">${course.tenKhoaHoc || "Chưa có tên"}</h3>
          
          <div class="detail-meta">
            <div class="meta-item">
              <i class="fas fa-star"></i>
              <span>${
                course.saoTrungBinh ? course.saoTrungBinh.toFixed(1) : "0.0"
              } (${course.soLuongDanhGia || 0} đánh giá)</span>
            </div>
            <div class="meta-item">
              <i class="fas fa-user-tie"></i>
              <span>${teacherName}</span>
            </div>
            <div class="meta-item">
              <i class="fas fa-calendar-alt"></i>
              <span>${
                course.lichTrinhList ? course.lichTrinhList.length : 0
              } lịch trình</span>
            </div>
            <div class="meta-item">
              <i class="fas fa-utensils"></i>
              <span>${dishCount} món ăn</span>
            </div>
          </div>

          ${this.renderPrice(course)}
          ${this.renderIntroduction(course)}
          ${this.renderDescription(course)}
          ${this.renderValues(course)}
          ${this.renderSchedules(course)}
          ${this.renderDishes(course)}
        </div>
      </div>
    `;
  }

  getImageUrl(hinhAnh) {
    if (hinhAnh) {
      return `http://localhost:8080/images/${hinhAnh}`;
    }
    return 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="800" height="400"%3E%3Crect fill="%23f1f5f9" width="800" height="400"/%3E%3Ctext fill="%2394a3b8" font-family="Arial" font-size="32" x="50%25" y="50%25" text-anchor="middle" dominant-baseline="middle"%3EKhóa học%3C/text%3E%3C/svg%3E';
  }

  getTeacherName(course) {
    if (
      course.lichTrinhList &&
      course.lichTrinhList.length > 0 &&
      course.lichTrinhList[0].tenGiaoVien
    ) {
      return course.lichTrinhList[0].tenGiaoVien;
    }
    return "Chưa có giáo viên";
  }

  getDishCount(course) {
    if (!course.danhMucMonAnList) return 0;
    return course.danhMucMonAnList.reduce(
      (sum, dm) => sum + (dm.danhSachMon?.length || 0),
      0
    );
  }

  renderPrice(course) {
    if (course.coUuDai && course.giaSauGiam) {
      return `
        <div class="detail-price">
          <span class="price-old">${this.formatPrice(course.giaTien)}</span>
          <span class="price-new">${this.formatPrice(course.giaSauGiam)}</span>
        </div>
      `;
    }
    return `
      <div class="detail-price">
        <span class="price-single">${this.formatPrice(course.giaTien)}</span>
      </div>
    `;
  }

  renderIntroduction(course) {
    if (!course.gioiThieu) return "";
    return `
      <div class="detail-section">
        <h4><i class="fas fa-info-circle"></i> Giới thiệu</h4>
        <p>${course.gioiThieu}</p>
      </div>
    `;
  }

  renderDescription(course) {
    if (!course.moTa) return "";
    return `
      <div class="detail-section">
        <h4><i class="fas fa-align-left"></i> Mô tả</h4>
        <p>${course.moTa}</p>
      </div>
    `;
  }

  renderValues(course) {
    if (!course.giaTriSauBuoiHoc) return "";
    const values = course.giaTriSauBuoiHoc
      .split("\n")
      .map((v) => v.trim())
      .filter((v) => v);
    if (values.length === 0) return "";

    return `
      <div class="detail-section">
        <h4><i class="fas fa-check-circle"></i> Giá trị sau buổi học</h4>
        <div class="value-list">
          ${values.map((v) => `<div class="value-item">${v}</div>`).join("")}
        </div>
      </div>
    `;
  }

  renderSchedules(course) {
    if (!course.lichTrinhList || course.lichTrinhList.length === 0) return "";

    return `
      <div class="detail-section">
        <h4><i class="fas fa-calendar-check"></i> Lịch trình</h4>
        <div class="schedule-list">
          ${course.lichTrinhList
            .map(
              (lt) => `
            <div class="schedule-item">
              <div class="schedule-time">
                <i class="fas fa-clock"></i>
                <span>${lt.gioBatDau} - ${lt.gioKetThuc}</span>
              </div>
              <div class="schedule-days">
                <i class="fas fa-calendar"></i>
                <span>${lt.thuTrongTuan}</span>
              </div>
              <div class="schedule-location">
                <i class="fas fa-map-marker-alt"></i>
                <span>${lt.diaDiem}</span>
              </div>
              <div class="schedule-seats">
                <i class="fas fa-users"></i>
                <span>${lt.conTrong || 0}/${lt.soLuongToiDa || 0} chỗ</span>
              </div>
            </div>
          `
            )
            .join("")}
        </div>
      </div>
    `;
  }

  renderDishes(course) {
    if (!course.danhMucMonAnList || course.danhMucMonAnList.length === 0)
      return "";

    return `
      <div class="detail-section">
        <h4><i class="fas fa-utensils"></i> Món ăn</h4>
        <div class="dishes-list">
          ${course.danhMucMonAnList
            .map(
              (dm) => `
            <div class="dish-category">
              <h5>${dm.tenDanhMuc}</h5>
              <div class="dish-items">
                ${
                  dm.danhSachMon && dm.danhSachMon.length > 0
                    ? dm.danhSachMon
                        .map(
                          (mon) => `
                      <div class="dish-item">
                        <strong>${mon.tenMon}</strong>
                        ${mon.gioiThieu ? `<p>${mon.gioiThieu}</p>` : ""}
                      </div>
                    `
                        )
                        .join("")
                    : '<p class="text-muted">Chưa có món ăn</p>'
                }
              </div>
            </div>
          `
            )
            .join("")}
        </div>
      </div>
    `;
  }

  formatPrice(price) {
    if (!price) return "Liên hệ";
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  }
}

// Initialize global instance
let courseDetailModal;
document.addEventListener("DOMContentLoaded", () => {
  courseDetailModal = new CourseDetailModal();
});
