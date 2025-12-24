class CourseFormModal {
  constructor() {
    this.modal = null;
    this.modalBody = null;
    this.courseId = null;
    this.teachers = [];
    this.init();
  }

  init() {
    if (!document.getElementById("courseFormModal")) {
      this.createModal();
    }
    this.modal = new Modal("courseFormModal");
    this.modalBody = document.getElementById("courseFormBody");
    this.loadTeachers();
  }

  createModal() {
    const modalHTML = `
      <div id="courseFormModal" class="modal">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h2 id="courseFormTitle">Thêm Khóa Học Mới</h2>
              <span class="close">&times;</span>
            </div>
            <div class="modal-body" id="courseFormBody">
              <!-- Content loaded via JS -->
            </div>
            <div class="modal-footer" id="courseFormFooter">
              <button type="button" class="btn btn-secondary close-modal">
                <i class="fas fa-times"></i> Hủy
              </button>
              <button type="submit" form="courseForm" class="btn btn-primary" id="courseFormSubmitBtn">
                <i class="fas fa-save"></i> Tạo mới
              </button>
            </div>
          </div>
        </div>
      </div>
    `;
    document.body.insertAdjacentHTML("beforeend", modalHTML);
  }

  async loadTeachers() {
    try {
      const response = await apiService.get("/giaovien");
      this.teachers = Array.isArray(response) ? response : [];
    } catch (error) {
      console.error("Error loading teachers:", error);
      this.teachers = [];
    }
  }

  async showCreate() {
    this.courseId = null;
    document.getElementById("courseFormTitle").textContent = "Thêm Khóa Học Mới";
    document.getElementById("courseFormSubmitBtn").innerHTML = '<i class="fas fa-save"></i> Tạo mới';
    this.renderForm();
    this.modal.show();
  }

  async showEdit(courseId) {
    try {
      this.courseId = courseId;
      document.getElementById("courseFormTitle").textContent = "Chỉnh Sửa Khóa Học";
      document.getElementById("courseFormSubmitBtn").innerHTML = '<i class="fas fa-save"></i> Cập nhật';
      
      this.modalBody.innerHTML = '<div class="loading-state"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>';
      this.modal.show();

      const response = await apiService.get(`/khoahoc/${courseId}`);
      if (!response) {
        throw new Error("Không tìm thấy khóa học");
      }

      this.renderForm(response);
    } catch (error) {
      console.error("Error loading course:", error);
      this.modalBody.innerHTML = `
        <div class="error-state">
          <i class="fas fa-exclamation-circle"></i>
          <h3>Lỗi tải dữ liệu</h3>
          <p>${error.message}</p>
        </div>
      `;
    }
  }

  renderForm(course = null) {
    const teacherId = course?.lichTrinhList?.[0]?.maGiaoVien || "";
    
    this.modalBody.innerHTML = `
      <form id="courseForm" class="course-form">
        <div class="form-section">
          <h4><i class="fas fa-info-circle"></i> Thông tin cơ bản</h4>
          
          <div class="form-group">
            <label>Tên khóa học <span class="text-red">*</span></label>
            <input type="text" name="tenKhoaHoc" class="form-control" 
                   value="${course?.tenKhoaHoc || ''}" 
                   placeholder="Nhập tên khóa học" required />
          </div>

          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <label>Giáo viên <span class="text-red">*</span></label>
                <select name="maGiaoVien" class="form-control" required>
                  <option value="">-- Chọn giáo viên --</option>
                  ${this.teachers.map(t => `
                    <option value="${t.maGiaoVien}" ${teacherId == t.maGiaoVien ? 'selected' : ''}>
                      ${t.hoTen}
                    </option>
                  `).join('')}
                </select>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label>Giá tiền (VNĐ) <span class="text-red">*</span></label>
                <input type="number" name="giaTien" class="form-control" 
                       value="${course?.giaTien || ''}" 
                       placeholder="500000" required />
              </div>
            </div>
          </div>

          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" name="coUuDai" ${course?.coUuDai ? 'checked' : ''} />
              <span>Có ưu đãi (giảm 10%)</span>
            </label>
          </div>

          <div class="form-group">
            <label>Giới thiệu</label>
            <textarea name="gioiThieu" class="form-control" rows="3" 
                      placeholder="Giới thiệu ngắn về khóa học">${course?.gioiThieu || ''}</textarea>
          </div>

          <div class="form-group">
            <label>Mô tả chi tiết</label>
            <textarea name="moTa" class="form-control" rows="4" 
                      placeholder="Mô tả chi tiết về khóa học">${course?.moTa || ''}</textarea>
          </div>

          <div class="form-group">
            <label>Giá trị sau buổi học</label>
            <textarea name="giaTriSauBuoiHoc" class="form-control" rows="4" 
                      placeholder="• Kỹ năng 1&#10;• Kỹ năng 2&#10;• Kỹ năng 3">${course?.giaTriSauBuoiHoc || ''}</textarea>
            <small class="form-text">Mỗi dòng là một giá trị, bắt đầu bằng dấu •</small>
          </div>

          <div class="form-group">
            <label>Hình ảnh đại diện</label>
            <div class="image-upload-container">
              <div class="image-preview" id="courseImagePreview">
                ${course?.hinhAnh ? `
                  <img src="http://localhost:8080/images/${course.hinhAnh}" alt="Preview" />
                ` : `
                  <div class="upload-placeholder">
                    <i class="fas fa-cloud-upload-alt"></i>
                    <p>Click để chọn ảnh</p>
                  </div>
                `}
              </div>
              <input type="file" id="courseImageInput" accept="image/*" style="display: none;" />
              <input type="hidden" name="hinhAnh" id="hinhAnhValue" value="${course?.hinhAnh || ''}" />
            </div>
            <small class="form-text">Ảnh sẽ được upload khi lưu khóa học</small>
          </div>
        </div>
      </form>
    `;

    // Attach form submit handler
    document.getElementById("courseForm").addEventListener("submit", (e) => {
      e.preventDefault();
      this.handleSubmit();
    });

    // Setup image upload
    this.setupImageUpload();
  }

  setupImageUpload() {
    const preview = document.getElementById("courseImagePreview");
    const input = document.getElementById("courseImageInput");
    
    if (!preview || !input) return;

    preview.addEventListener("click", () => {
      input.click();
    });

    input.addEventListener("change", (e) => {
      const file = e.target.files[0];
      if (file) {
        if (!file.type.startsWith("image/")) {
          alert("Vui lòng chọn file hình ảnh!");
          return;
        }

        const reader = new FileReader();
        reader.onload = (e) => {
          preview.innerHTML = `<img src="${e.target.result}" alt="Preview" />`;
        };
        reader.readAsDataURL(file);
      }
    });
  }

  async handleSubmit() {
    try {
      const form = document.getElementById("courseForm");
      const formData = new FormData(form);
      
      // Upload image first if selected
      let imagePath = document.getElementById("hinhAnhValue").value;
      const imageInput = document.getElementById("courseImageInput");
      
      if (imageInput.files.length > 0) {
        const uploadedPath = await this.uploadImage(imageInput.files[0]);
        if (uploadedPath) {
          imagePath = uploadedPath;
        }
      }
      
      const data = {
        tenKhoaHoc: formData.get("tenKhoaHoc"),
        moTa: formData.get("moTa"),
        gioiThieu: formData.get("gioiThieu"),
        giaTriSauBuoiHoc: formData.get("giaTriSauBuoiHoc"),
        maGiaoVien: parseInt(formData.get("maGiaoVien")),
        giaTien: parseFloat(formData.get("giaTien")),
        hinhAnh: imagePath,
        coUuDai: formData.get("coUuDai") === "on",
      };

      // Validate
      if (!data.tenKhoaHoc || !data.maGiaoVien || !data.giaTien) {
        alert("Vui lòng điền đầy đủ thông tin bắt buộc");
        return;
      }

      let response;
      if (this.courseId) {
        // Update
        data.maKhoaHoc = this.courseId;
        response = await apiService.put(`/khoahoc/${this.courseId}`, data);
      } else {
        // Create
        response = await apiService.post("/khoahoc", data);
      }

      if (response && (response.maKhoaHoc || response.success !== false)) {
        if (typeof showToast === "function") {
          showToast(
            this.courseId ? "Cập nhật khóa học thành công" : "Tạo khóa học thành công",
            "success"
          );
        } else {
          alert(this.courseId ? "Cập nhật khóa học thành công" : "Tạo khóa học thành công");
        }
        
        this.modal.hide();
        
        // Reload courses list
        if (typeof loadCourses === "function") {
          loadCourses();
        }
      } else {
        throw new Error(response?.message || "Lưu thất bại");
      }
    } catch (error) {
      console.error("Error saving course:", error);
      alert(`Lỗi: ${error.message || "Không thể lưu khóa học"}`);
    }
  }

  async uploadImage(file) {
    try {
      const formData = new FormData();
      formData.append("file", file);

      const response = await fetch("http://localhost:8080/api/upload/image", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Upload failed");
      }

      const result = await response.json();
      // API returns { fileName: "xxx.jpg", fileUrl: "/uploads/reviews/xxx.jpg" }
      // We only need the fileName
      return result.fileName || result.fileUrl?.split("/").pop();
    } catch (error) {
      console.error("Error uploading image:", error);
      alert("Lỗi upload ảnh. Vui lòng thử lại.");
      return null;
    }
  }
}

// Initialize global instance
let courseFormModal;
document.addEventListener("DOMContentLoaded", () => {
  courseFormModal = new CourseFormModal();
});
