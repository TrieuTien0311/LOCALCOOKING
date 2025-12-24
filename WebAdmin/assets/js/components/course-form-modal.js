class CourseFormModal {
  constructor() {
    this.modal = null;
    this.modalBody = null;
    this.courseId = null;
    this.availableDishes = [];
    this.selectedDishes = {
      khaiVi: null,
      monChinh1: null,
      monChinh2: null,
      trangMieng: null
    };
    this.mainImage = null;
    this.galleryImages = [];
    this.existingGalleryImages = [];
    this.init();
  }

  init() {
    if (!document.getElementById("courseFormModal")) {
      this.createModal();
    }
    this.modal = new Modal("courseFormModal");
    this.modalBody = document.getElementById("courseFormBody");
  }

  createModal() {
    const modalHTML = `
      <div id="courseFormModal" class="modal">
        <div class="modal-dialog modal-xl">
          <div class="modal-content">
            <div class="modal-header">
              <h2 id="courseFormTitle">Thêm Khóa Học Mới</h2>
              <span class="close">&times;</span>
            </div>
            <div class="modal-body" id="courseFormBody" style="max-height: 75vh; overflow-y: auto;">
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


  async loadAvailableDishes() {
    try {
      const response = await apiService.get("/monan");
      const allDishes = Array.isArray(response) ? response : [];
      this.availableDishes = allDishes.filter(d => !d.maKhoaHoc && !d.maDanhMuc);
    } catch (error) {
      console.error("Error loading dishes:", error);
      this.availableDishes = [];
    }
  }

  async showCreate() {
    this.courseId = null;
    this.selectedDishes = { khaiVi: null, monChinh1: null, monChinh2: null, trangMieng: null };
    this.mainImage = null;
    this.galleryImages = [];
    this.existingGalleryImages = [];
    
    document.getElementById("courseFormTitle").textContent = "Thêm Khóa Học Mới";
    document.getElementById("courseFormSubmitBtn").innerHTML = '<i class="fas fa-save"></i> Tạo mới';
    
    this.modalBody.innerHTML = '<div class="loading-state"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>';
    this.modal.show();
    
    await this.loadAvailableDishes();
    this.renderForm();
  }

  async showEdit(courseId) {
    try {
      this.courseId = courseId;
      this.selectedDishes = { khaiVi: null, monChinh1: null, monChinh2: null, trangMieng: null };
      this.mainImage = null;
      this.galleryImages = [];
      this.existingGalleryImages = [];
      
      document.getElementById("courseFormTitle").textContent = "Chỉnh Sửa Khóa Học";
      document.getElementById("courseFormSubmitBtn").innerHTML = '<i class="fas fa-save"></i> Cập nhật';
      
      this.modalBody.innerHTML = '<div class="loading-state"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>';
      this.modal.show();

      const [course, galleryImages] = await Promise.all([
        apiService.get(`/khoahoc/${courseId}`),
        this.loadCourseGalleryImages(courseId)
      ]);
      
      if (!course) throw new Error("Không tìm thấy khóa học");

      this.existingGalleryImages = galleryImages || [];
      await this.loadAvailableDishes();
      this.renderForm(course);
    } catch (error) {
      console.error("Error loading course:", error);
      this.modalBody.innerHTML = `<div class="error-state"><i class="fas fa-exclamation-circle"></i><h3>Lỗi tải dữ liệu</h3><p>${error.message}</p></div>`;
    }
  }

  async loadCourseGalleryImages(courseId) {
    try {
      const response = await apiService.get(`/hinhanh-khoahoc/khoahoc/${courseId}`);
      return Array.isArray(response) ? response : [];
    } catch (error) {
      return [];
    }
  }


  renderForm(course = null) {
    // URL ảnh - sử dụng /uploads/courses/ cho ảnh đại diện
    const mainImageUrl = course?.hinhAnh ? `http://localhost:8080/uploads/courses/${course.hinhAnh}` : '';
    
    this.modalBody.innerHTML = `
      <form id="courseForm" class="course-form">
        <div class="form-section">
          <h4><i class="fas fa-info-circle"></i> Thông tin cơ bản</h4>
          
          <div class="form-group">
            <label>Tên khóa học <span class="text-red">*</span></label>
            <input type="text" name="tenKhoaHoc" class="form-control" value="${course?.tenKhoaHoc || ''}" placeholder="Nhập tên khóa học" required />
          </div>

          <div class="form-group">
            <label>Giá tiền (VNĐ) <span class="text-red">*</span></label>
            <input type="number" name="giaTien" class="form-control" value="${course?.giaTien || ''}" placeholder="500000" required />
          </div>

          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" name="coUuDai" ${course?.coUuDai ? 'checked' : ''} />
              <span>Có ưu đãi (giảm 10%)</span>
            </label>
          </div>

          <div class="form-group">
            <label>Giới thiệu</label>
            <textarea name="gioiThieu" class="form-control" rows="3" placeholder="Giới thiệu ngắn về khóa học">${course?.gioiThieu || ''}</textarea>
          </div>

          <div class="form-group">
            <label>Mô tả chi tiết</label>
            <textarea name="moTa" class="form-control" rows="4" placeholder="Mô tả chi tiết về khóa học">${course?.moTa || ''}</textarea>
          </div>

          <div class="form-group">
            <label>Giá trị sau buổi học</label>
            <textarea name="giaTriSauBuoiHoc" class="form-control" rows="4" placeholder="• Kỹ năng 1&#10;• Kỹ năng 2">${course?.giaTriSauBuoiHoc || ''}</textarea>
          </div>
        </div>

        <div class="form-section">
          <h4><i class="fas fa-images"></i> Hình ảnh khóa học</h4>
          
          <div class="form-group">
            <label>Ảnh đại diện <span class="text-red">*</span></label>
            <small class="form-text d-block mb-2">Ảnh chính hiển thị trên danh sách (lưu vào KhoaHoc.hinhAnh)</small>
            <div class="image-upload-container">
              <div class="image-preview main-image-preview" id="mainImagePreview">
                ${mainImageUrl ? `
                  <img src="${mainImageUrl}" alt="Preview" onerror="this.parentElement.innerHTML='<div class=\\'upload-placeholder\\'><i class=\\'fas fa-cloud-upload-alt\\'></i><p>Click để chọn ảnh</p></div>'" />
                  <button type="button" class="remove-image" onclick="courseFormModal.removeMainImage()"><i class="fas fa-times"></i></button>
                ` : `<div class="upload-placeholder"><i class="fas fa-cloud-upload-alt"></i><p>Click để chọn ảnh đại diện</p></div>`}
              </div>
              <input type="file" id="mainImageInput" accept="image/*" style="display: none;" />
              <input type="hidden" name="hinhAnh" id="hinhAnhValue" value="${course?.hinhAnh || ''}" />
            </div>
          </div>

          <div class="form-group">
            <label>Ảnh bổ sung <span class="text-red">*</span></label>
            <small class="form-text d-block mb-2">Chọn đúng 2 ảnh (lưu vào HinhAnhKhoaHoc)</small>
            <div class="gallery-upload-container" id="galleryContainer">
              <div class="gallery-preview" id="galleryPreview">${this.renderExistingGalleryImages()}</div>
              <div class="gallery-add-btn" id="galleryAddBtn" style="${this.existingGalleryImages.length >= 2 ? 'display:none;' : ''}">
                <i class="fas fa-plus"></i><span>Thêm ảnh</span>
              </div>
              <input type="file" id="galleryImageInput" accept="image/*" multiple style="display: none;" />
            </div>
          </div>
        </div>

        <div class="form-section">
          <h4><i class="fas fa-utensils"></i> Chọn món ăn cho khóa học</h4>
          <small class="form-text d-block mb-3">Chọn món ăn chưa được gán. Sau khi tạo, các món sẽ tự động được gán maDanhMuc và maKhoaHoc.</small>
          
          <div class="dish-category-section">
            <label><i class="fas fa-leaf" style="color: #22c55e;"></i> Món khai vị (1 món)</label>
            <div class="dish-select-container">
              <select id="selectKhaiVi" class="form-control dish-select">
                <option value="">-- Chọn món khai vị --</option>
                ${this.availableDishes.map(d => `<option value="${d.maMonAn}">${d.tenMon}</option>`).join('')}
              </select>
              <div class="selected-dish" id="selectedKhaiVi"></div>
            </div>
          </div>

          <div class="dish-category-section">
            <label><i class="fas fa-drumstick-bite" style="color: #f59e0b;"></i> Món chính (2 món)</label>
            <div class="dish-select-container">
              <select id="selectMonChinh1" class="form-control dish-select">
                <option value="">-- Chọn món chính 1 --</option>
                ${this.availableDishes.map(d => `<option value="${d.maMonAn}">${d.tenMon}</option>`).join('')}
              </select>
              <div class="selected-dish" id="selectedMonChinh1"></div>
            </div>
            <div class="dish-select-container mt-2">
              <select id="selectMonChinh2" class="form-control dish-select">
                <option value="">-- Chọn món chính 2 --</option>
                ${this.availableDishes.map(d => `<option value="${d.maMonAn}">${d.tenMon}</option>`).join('')}
              </select>
              <div class="selected-dish" id="selectedMonChinh2"></div>
            </div>
          </div>

          <div class="dish-category-section">
            <label><i class="fas fa-ice-cream" style="color: #ec4899;"></i> Món tráng miệng (1 món)</label>
            <div class="dish-select-container">
              <select id="selectTrangMieng" class="form-control dish-select">
                <option value="">-- Chọn món tráng miệng --</option>
                ${this.availableDishes.map(d => `<option value="${d.maMonAn}">${d.tenMon}</option>`).join('')}
              </select>
              <div class="selected-dish" id="selectedTrangMieng"></div>
            </div>
          </div>
        </div>
      </form>
    `;
    this.attachEventHandlers();
  }

  renderExistingGalleryImages() {
    if (!this.existingGalleryImages?.length) return '';
    return this.existingGalleryImages.map((img, i) => `
      <div class="gallery-item" data-id="${img.maHinhAnh}">
        <img src="http://localhost:8080/uploads/courses/${img.duongDan}" alt="Gallery ${i + 1}" />
        <button type="button" class="remove-gallery-image" onclick="courseFormModal.removeGalleryImage(${img.maHinhAnh})"><i class="fas fa-times"></i></button>
      </div>
    `).join('');
  }


  attachEventHandlers() {
    document.getElementById("courseForm").addEventListener("submit", (e) => {
      e.preventDefault();
      this.handleSubmit();
    });
    this.setupMainImageUpload();
    this.setupGalleryUpload();
    this.setupDishSelection();
  }

  setupMainImageUpload() {
    const preview = document.getElementById("mainImagePreview");
    const input = document.getElementById("mainImageInput");
    if (!preview || !input) return;

    preview.addEventListener("click", () => {
      if (!preview.querySelector('img')) input.click();
    });

    input.addEventListener("change", (e) => {
      const file = e.target.files[0];
      if (!file) return;
      if (!file.type.startsWith("image/")) { alert("Vui lòng chọn file hình ảnh!"); return; }
      if (file.size > 5 * 1024 * 1024) { alert("Kích thước ảnh không được vượt quá 5MB"); return; }

      this.mainImage = file;
      const reader = new FileReader();
      reader.onload = (e) => {
        preview.innerHTML = `<img src="${e.target.result}" alt="Preview" /><button type="button" class="remove-image" onclick="courseFormModal.removeMainImage()"><i class="fas fa-times"></i></button>`;
      };
      reader.readAsDataURL(file);
    });
  }

  removeMainImage() {
    this.mainImage = null;
    document.getElementById("hinhAnhValue").value = '';
    document.getElementById("mainImagePreview").innerHTML = `<div class="upload-placeholder"><i class="fas fa-cloud-upload-alt"></i><p>Click để chọn ảnh đại diện</p></div>`;
  }

  setupGalleryUpload() {
    const addBtn = document.getElementById("galleryAddBtn");
    const input = document.getElementById("galleryImageInput");
    const preview = document.getElementById("galleryPreview");
    if (!addBtn || !input) return;

    addBtn.addEventListener("click", () => input.click());

    input.addEventListener("change", (e) => {
      const files = Array.from(e.target.files);
      const currentCount = this.existingGalleryImages.length + this.galleryImages.length;
      const maxAllowed = 2 - currentCount;

      if (files.length > maxAllowed) { alert(`Chỉ được thêm tối đa ${maxAllowed} ảnh nữa.`); return; }

      files.forEach(file => {
        if (!file.type.startsWith("image/")) { alert("Chỉ chấp nhận file ảnh"); return; }
        if (file.size > 5 * 1024 * 1024) { alert("Kích thước ảnh không được vượt quá 5MB"); return; }

        this.galleryImages.push(file);
        const reader = new FileReader();
        reader.onload = (e) => {
          const div = document.createElement('div');
          div.className = 'gallery-item new-image';
          div.dataset.index = this.galleryImages.length - 1;
          div.innerHTML = `<img src="${e.target.result}" alt="Gallery" /><button type="button" class="remove-gallery-image" onclick="courseFormModal.removeNewGalleryImage(${this.galleryImages.length - 1})"><i class="fas fa-times"></i></button>`;
          preview.appendChild(div);
          this.updateGalleryAddButton();
        };
        reader.readAsDataURL(file);
      });
      input.value = '';
    });
  }


  removeGalleryImage(imageId) {
    this.existingGalleryImages = this.existingGalleryImages.filter(img => img.maHinhAnh !== imageId);
    document.querySelector(`.gallery-item[data-id="${imageId}"]`)?.remove();
    this.updateGalleryAddButton();
  }

  removeNewGalleryImage(index) {
    this.galleryImages.splice(index, 1);
    document.querySelectorAll('.gallery-item.new-image').forEach(el => el.remove());
    this.galleryImages.forEach((file, i) => {
      const reader = new FileReader();
      reader.onload = (e) => {
        const div = document.createElement('div');
        div.className = 'gallery-item new-image';
        div.dataset.index = i;
        div.innerHTML = `<img src="${e.target.result}" alt="Gallery" /><button type="button" class="remove-gallery-image" onclick="courseFormModal.removeNewGalleryImage(${i})"><i class="fas fa-times"></i></button>`;
        document.getElementById("galleryPreview").appendChild(div);
      };
      reader.readAsDataURL(file);
    });
    this.updateGalleryAddButton();
  }

  updateGalleryAddButton() {
    const totalImages = this.existingGalleryImages.length + this.galleryImages.length;
    const addBtn = document.getElementById("galleryAddBtn");
    if (addBtn) addBtn.style.display = totalImages >= 2 ? 'none' : 'flex';
  }

  setupDishSelection() {
    const selects = ['selectKhaiVi', 'selectMonChinh1', 'selectMonChinh2', 'selectTrangMieng'];
    const keys = ['khaiVi', 'monChinh1', 'monChinh2', 'trangMieng'];
    
    selects.forEach((selectId, index) => {
      const select = document.getElementById(selectId);
      if (select) {
        select.addEventListener('change', (e) => {
          const dishId = e.target.value ? parseInt(e.target.value) : null;
          this.selectedDishes[keys[index]] = dishId;
          this.updateSelectedDishDisplay(keys[index], dishId);
          this.updateDishOptions();
        });
      }
    });
  }

  updateSelectedDishDisplay(key, dishId) {
    const displayId = 'selected' + key.charAt(0).toUpperCase() + key.slice(1);
    const display = document.getElementById(displayId);
    if (!display) return;

    if (dishId) {
      const dish = this.availableDishes.find(d => d.maMonAn === dishId);
      if (dish) {
        display.innerHTML = `<div class="selected-dish-item"><span class="dish-name">${dish.tenMon}</span><button type="button" class="btn-remove-dish" onclick="courseFormModal.clearDishSelection('${key}')"><i class="fas fa-times"></i></button></div>`;
      }
    } else {
      display.innerHTML = '';
    }
  }

  clearDishSelection(key) {
    this.selectedDishes[key] = null;
    const selectId = 'select' + key.charAt(0).toUpperCase() + key.slice(1);
    const select = document.getElementById(selectId);
    if (select) select.value = '';
    this.updateSelectedDishDisplay(key, null);
    this.updateDishOptions();
  }


  updateDishOptions() {
    const selectedIds = Object.values(this.selectedDishes).filter(id => id !== null);
    const selects = ['selectKhaiVi', 'selectMonChinh1', 'selectMonChinh2', 'selectTrangMieng'];
    const keys = ['khaiVi', 'monChinh1', 'monChinh2', 'trangMieng'];
    
    selects.forEach((selectId, index) => {
      const select = document.getElementById(selectId);
      if (!select) return;
      const currentValue = this.selectedDishes[keys[index]];
      
      let optionsHtml = '<option value="">-- Chọn món --</option>';
      this.availableDishes.forEach(dish => {
        const isSelected = dish.maMonAn === currentValue;
        const isUsedElsewhere = selectedIds.includes(dish.maMonAn) && dish.maMonAn !== currentValue;
        if (!isUsedElsewhere) {
          optionsHtml += `<option value="${dish.maMonAn}" ${isSelected ? 'selected' : ''}>${dish.tenMon}</option>`;
        }
      });
      select.innerHTML = optionsHtml;
    });
  }

  async handleSubmit() {
    try {
      const form = document.getElementById("courseForm");
      const formData = new FormData(form);
      
      const hasMainImage = this.mainImage || document.getElementById("hinhAnhValue").value;
      const totalGalleryImages = this.existingGalleryImages.length + this.galleryImages.length;
      
      if (!hasMainImage) { alert("Vui lòng chọn ảnh đại diện cho khóa học"); return; }
      if (totalGalleryImages !== 2) { alert(`Cần chọn đúng 2 ảnh bổ sung. Hiện tại: ${totalGalleryImages}/2`); return; }

      // Upload main image
      let mainImagePath = document.getElementById("hinhAnhValue").value;
      if (this.mainImage) {
        const uploadedPath = await this.uploadImage(this.mainImage, 'courses');
        if (uploadedPath) mainImagePath = uploadedPath;
        else throw new Error("Không thể upload ảnh đại diện");
      }
      
      const data = {
        tenKhoaHoc: formData.get("tenKhoaHoc"),
        moTa: formData.get("moTa"),
        gioiThieu: formData.get("gioiThieu"),
        giaTriSauBuoiHoc: formData.get("giaTriSauBuoiHoc"),
        giaTien: parseFloat(formData.get("giaTien")),
        hinhAnh: mainImagePath,
        coUuDai: formData.get("coUuDai") === "on",
      };

      if (!data.tenKhoaHoc || !data.giaTien) { alert("Vui lòng điền đầy đủ thông tin bắt buộc"); return; }

      let courseId;
      if (this.courseId) {
        data.maKhoaHoc = this.courseId;
        const response = await apiService.put(`/khoahoc/${this.courseId}`, data);
        if (!response || response.success === false) throw new Error(response?.message || "Cập nhật thất bại");
        courseId = this.courseId;
      } else {
        const response = await apiService.post("/khoahoc", data);
        if (!response || !response.maKhoaHoc) throw new Error(response?.message || "Tạo khóa học thất bại");
        courseId = response.maKhoaHoc;
      }

      // Upload gallery images
      for (const file of this.galleryImages) {
        await this.uploadGalleryImage(courseId, file);
      }

      // Assign dishes
      await this.assignDishesToCourse(courseId);

      alert(this.courseId ? "Cập nhật khóa học thành công" : "Tạo khóa học thành công");
      this.modal.hide();
      if (typeof loadCourses === "function") loadCourses();
    } catch (error) {
      console.error("Error saving course:", error);
      alert(`Lỗi: ${error.message || "Không thể lưu khóa học"}`);
    }
  }


  async uploadImage(file, folder = 'courses') {
    try {
      const formData = new FormData();
      formData.append("file", file);
      const response = await fetch(`http://localhost:8080/api/upload/image?folder=${folder}`, {
        method: "POST",
        body: formData,
      });
      if (!response.ok) throw new Error("Upload failed");
      const result = await response.json();
      return result.fileName || result.fileUrl?.split("/").pop();
    } catch (error) {
      console.error("Error uploading image:", error);
      return null;
    }
  }

  async uploadGalleryImage(courseId, file) {
    try {
      const formData = new FormData();
      formData.append("file", file);
      formData.append("maKhoaHoc", courseId);
      const response = await apiService.upload("/hinhanh-khoahoc", formData);
      console.log("Gallery image uploaded:", response);
      return response;
    } catch (error) {
      console.error("Error uploading gallery image:", error);
      return null;
    }
  }

  async assignDishesToCourse(courseId) {
    const categoryMap = { khaiVi: 1, monChinh1: 2, monChinh2: 2, trangMieng: 3 };

    for (const [key, dishId] of Object.entries(this.selectedDishes)) {
      if (dishId) {
        try {
          const maDanhMuc = categoryMap[key];
          await apiService.put(`/monan/${dishId}`, { maKhoaHoc: courseId, maDanhMuc: maDanhMuc });
        } catch (error) {
          console.error(`Error assigning dish ${dishId}:`, error);
        }
      }
    }
  }
}

let courseFormModal;
document.addEventListener("DOMContentLoaded", () => {
  courseFormModal = new CourseFormModal();
});
