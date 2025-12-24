// Dishes Management
let dishes = [];
let filteredDishes = [];
let courses = []; // Danh sách khóa học
let categories = []; // Danh mục món ăn
let currentPage = 1;
const itemsPerPage = 10;
let editingDishId = null;
let selectedImages = [];
let imagesToDelete = []; // Track images to delete on save

// Initialize
document.addEventListener('DOMContentLoaded', () => {
  loadData();
  initEventListeners();
  initUpload();
});

// Load all data
async function loadData() {
  try {
    showLoading();
    await Promise.all([loadDishesData(), loadCoursesData(), loadCategoriesData()]);
    populateCourseSelect();
    populateCategorySelect();
    renderDishes();
    hideLoading();
  } catch (error) {
    console.error('Error loading data:', error);
    hideLoading();
  }
}

// Load dishes data only
async function loadDishesData() {
  try {
    const response = await apiService.get('/monan');
    dishes = Array.isArray(response) ? response : [];
    filteredDishes = [...dishes];
  } catch (error) {
    console.error('Error loading dishes:', error);
    dishes = [];
    filteredDishes = [];
  }
}

// Load courses data
async function loadCoursesData() {
  try {
    const response = await apiService.get('/khoahoc');
    courses = Array.isArray(response) ? response : [];
  } catch (error) {
    console.error('Error loading courses:', error);
    courses = [];
  }
}

// Load categories data
async function loadCategoriesData() {
  try {
    const response = await apiService.get('/danhmucmonan');
    console.log('Categories API response:', response);
    
    // Check if response is error object
    if (response && response.success === false) {
      console.error('Categories API error:', response.message);
      categories = [];
      return;
    }
    
    categories = Array.isArray(response) ? response : [];
    console.log('Loaded categories:', categories);
  } catch (error) {
    console.error('Error loading categories:', error);
    categories = [];
  }
}

// Populate course select
function populateCourseSelect() {
  const select = document.getElementById('maKhoaHoc');
  if (select) {
    select.innerHTML = '<option value="">-- Chọn khóa học --</option>' +
      courses.map(c => `<option value="${c.maKhoaHoc}">${c.tenKhoaHoc}</option>`).join('');
  }
}

// Populate category select
function populateCategorySelect() {
  const select = document.getElementById('maDanhMuc');
  if (select) {
    select.innerHTML = '<option value="">-- Chọn danh mục --</option>' +
      categories.map(c => `<option value="${c.maDanhMuc}">${c.tenDanhMuc}</option>`).join('');
  }
}

// Load dishes và render (dùng khi reload)
async function loadDishes() {
  try {
    showLoading();
    await loadDishesData();
    renderDishes();
    hideLoading();
  } catch (error) {
    console.error('Error loading dishes:', error);
    hideLoading();
  }
}

// Render dishes table
function renderDishes() {
  const tbody = document.getElementById('dishesTableBody');
  const start = (currentPage - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  const paginatedDishes = filteredDishes.slice(start, end);

  if (paginatedDishes.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="6" class="text-center text-muted">
          <i class="fas fa-hamburger" style="font-size: 48px; opacity: 0.3;"></i>
          <p>Chưa có món ăn nào</p>
        </td>
      </tr>
    `;
    return;
  }

  tbody.innerHTML = paginatedDishes
    .map(
      (dish) => {
        const course = courses.find(c => c.maKhoaHoc == dish.maKhoaHoc);
        const category = categories.find(c => c.maDanhMuc == dish.maDanhMuc);
        return `
    <tr>
      <td>
        <span class="badge badge-secondary">#${dish.maMonAn}</span>
      </td>
      <td>
        <strong>${dish.tenMon}</strong>
      </td>
      <td>
        <div class="text-truncate" style="max-width: 250px;" title="${dish.gioiThieu || ''}">
          ${dish.gioiThieu || '<span class="text-muted">Chưa có giới thiệu</span>'}
        </div>
      </td>
      <td>
        ${category 
          ? `<span class="badge badge-success"><i class="fas fa-tag"></i> ${category.tenDanhMuc}</span>`
          : '<span class="badge badge-warning"><i class="fas fa-question"></i> Chưa phân loại</span>'
        }
      </td>
      <td>
        ${course
          ? `<span class="badge badge-primary"><i class="fas fa-book"></i> ${course.tenKhoaHoc}</span>`
          : '<span class="badge badge-info"><i class="fas fa-circle"></i> Chưa gán</span>'
        }
      </td>
      <td class="text-center">
        <div class="btn-group">
          <button class="btn-action btn-view" onclick="viewDish(${dish.maMonAn})" title="Xem chi tiết">
            <i class="fas fa-eye"></i>
          </button>
          <button class="btn-action btn-edit" onclick="editDish(${dish.maMonAn})" title="Chỉnh sửa">
            <i class="fas fa-edit"></i>
          </button>
          <button class="btn-action btn-delete" onclick="deleteDish(${dish.maMonAn})" title="Xóa">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </td>
    </tr>
  `;
      }
    )
    .join('');

  renderPagination();
}

// Render pagination
function renderPagination() {
  const totalPages = Math.ceil(filteredDishes.length / itemsPerPage);
  const pagination = document.getElementById('pagination');

  if (totalPages <= 1) {
    pagination.innerHTML = '';
    return;
  }

  let html = `
    <button class="btn-page" ${currentPage === 1 ? 'disabled' : ''} onclick="changePage(${currentPage - 1})">
      <i class="fas fa-chevron-left"></i>
    </button>
  `;

  for (let i = 1; i <= totalPages; i++) {
    if (i === 1 || i === totalPages || (i >= currentPage - 1 && i <= currentPage + 1)) {
      html += `
        <button class="btn-page ${i === currentPage ? 'active' : ''}" onclick="changePage(${i})">
          ${i}
        </button>
      `;
    } else if (i === currentPage - 2 || i === currentPage + 2) {
      html += '<span class="pagination-dots">...</span>';
    }
  }

  html += `
    <button class="btn-page" ${currentPage === totalPages ? 'disabled' : ''} onclick="changePage(${currentPage + 1})">
      <i class="fas fa-chevron-right"></i>
    </button>
  `;

  pagination.innerHTML = html;
}

// Change page
function changePage(page) {
  currentPage = page;
  renderDishes();
  document.getElementById('contentArea').scrollTop = 0;
}

// Search dishes
function searchDishes(query) {
  const searchTerm = query.toLowerCase().trim();
  if (!searchTerm) {
    filteredDishes = [...dishes];
  } else {
    filteredDishes = dishes.filter(
      (dish) =>
        dish.tenMon.toLowerCase().includes(searchTerm) ||
        (dish.gioiThieu && dish.gioiThieu.toLowerCase().includes(searchTerm)) ||
        (dish.nguyenLieu && dish.nguyenLieu.toLowerCase().includes(searchTerm))
    );
  }
  currentPage = 1;
  renderDishes();
}

// View dish details
async function viewDish(id) {
  const dish = dishes.find((d) => d.maMonAn === id);
  if (!dish) return;

  // Load images if not loaded
  let images = dish.hinhAnhList || [];
  if (!images.length) {
    try {
      showLoading();
      const response = await apiService.get(`/hinhanh-monan/monan/${id}`);
      images = response || [];
      hideLoading();
    } catch (error) {
      console.error('Error loading images:', error);
      hideLoading();
    }
  }

  // Load category name if exists
  if (dish.maDanhMuc && !dish.tenDanhMuc) {
    try {
      const categories = await apiService.get('/danhmucmonan');
      const categoryList = Array.isArray(categories) ? categories : [];
      const category = categoryList.find((c) => c.maDanhMuc === dish.maDanhMuc);
      dish.tenDanhMuc = category ? category.tenDanhMuc : 'Không xác định';
    } catch (error) {
      console.log('Could not load category:', error);
    }
  }

  openDishDetailModal(dish, images);
}

// Edit dish
async function editDish(id) {
  const dish = dishes.find((d) => d.maMonAn === id);
  if (!dish) return;

  editingDishId = id;
  imagesToDelete = []; // Reset delete list
  document.getElementById('modalTitle').textContent = 'Chỉnh Sửa Món Ăn';
  document.getElementById('dishId').value = dish.maMonAn;
  document.getElementById('tenMon').value = dish.tenMon;
  document.getElementById('gioiThieu').value = dish.gioiThieu || '';
  document.getElementById('nguyenLieu').value = dish.nguyenLieu || '';
  document.getElementById('maKhoaHoc').value = dish.maKhoaHoc || '';
  document.getElementById('maDanhMuc').value = dish.maDanhMuc || '';

  // Load existing images
  selectedImages = [];
  const previewContainer = document.getElementById('imagePreviewContainer');
  previewContainer.innerHTML = '';

  try {
    showLoading();
    const images = await apiService.get(`/hinhanh-monan/monan/${id}`);
    hideLoading();

    if (images && images.length > 0) {
      previewContainer.innerHTML = images
        .map(
          (img) => `
        <div class="image-preview-item" data-existing="${img.maHinhAnh}">
          <img src="${API_CONFIG.BASE_URL}/uploads/${img.duongDan}" alt="Preview" />
          <button type="button" class="remove-image" onclick="removeExistingImage(${img.maHinhAnh})">
            <i class="fas fa-times"></i>
          </button>
        </div>
      `
        )
        .join('');
    }
  } catch (error) {
    console.error('Error loading images:', error);
    hideLoading();
  }

  openModal('dishModal');
}

// Delete dish
async function deleteDish(id) {
  const dish = dishes.find((d) => d.maMonAn === id);
  if (!dish) return;

  if (dish.maKhoaHoc) {
    alert('Không thể xóa món ăn đã được gán vào khóa học');
    return;
  }

  if (!confirm(`Bạn có chắc muốn xóa món ăn "${dish.tenMon}"?`)) return;

  try {
    showLoading();
    await apiService.delete(`/monan/${id}`);
    alert('Xóa món ăn thành công');
    loadDishes();
  } catch (error) {
    console.error('Error deleting dish:', error);
    alert('Không thể xóa món ăn');
    hideLoading();
  }
}

// Remove existing image (temporary, will delete on save)
function removeExistingImage(imageId) {
  imagesToDelete.push(imageId);
  document.querySelector(`[data-existing="${imageId}"]`).remove();
}

// Initialize upload
function initUpload() {
  const uploadArea = document.getElementById('uploadArea');
  const fileInput = document.getElementById('dishImages');
  const previewContainer = document.getElementById('imagePreviewContainer');

  uploadArea.addEventListener('click', () => fileInput.click());

  uploadArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    uploadArea.classList.add('drag-over');
  });

  uploadArea.addEventListener('dragleave', () => {
    uploadArea.classList.remove('drag-over');
  });

  uploadArea.addEventListener('drop', (e) => {
    e.preventDefault();
    uploadArea.classList.remove('drag-over');
    handleFiles(e.dataTransfer.files);
  });

  fileInput.addEventListener('change', (e) => {
    handleFiles(e.target.files);
  });

  function handleFiles(files) {
    // Count existing images (not marked for deletion)
    const existingImages = previewContainer.querySelectorAll('[data-existing]');
    const existingCount = existingImages.length;
    const newCount = selectedImages.length;
    const totalAfterAdd = existingCount + newCount + files.length;

    if (totalAfterAdd > 2) {
      alert(`Chỉ được chọn tối đa 2 ảnh. Hiện tại: ${existingCount} ảnh cũ + ${newCount} ảnh mới = ${existingCount + newCount}/2`);
      return;
    }

    Array.from(files).forEach((file) => {
      if (!file.type.startsWith('image/')) {
        alert('Chỉ chấp nhận file ảnh');
        return;
      }

      if (file.size > 5 * 1024 * 1024) {
        alert('Kích thước ảnh không được vượt quá 5MB');
        return;
      }

      selectedImages.push(file);

      const reader = new FileReader();
      reader.onload = (e) => {
        const preview = document.createElement('div');
        preview.className = 'image-preview-item';
        preview.innerHTML = `
          <img src="${e.target.result}" alt="Preview" />
          <button type="button" class="remove-image" onclick="removeNewImage(${selectedImages.length - 1})">
            <i class="fas fa-times"></i>
          </button>
        `;
        previewContainer.appendChild(preview);
      };
      reader.readAsDataURL(file);
    });
  }
}

// Remove new image
function removeNewImage(index) {
  selectedImages.splice(index, 1);
  const previewContainer = document.getElementById('imagePreviewContainer');
  const items = previewContainer.querySelectorAll('.image-preview-item:not([data-existing])');
  if (items[index]) {
    items[index].remove();
  }
}

// Handle form submit
document.getElementById('dishForm').addEventListener('submit', async (e) => {
  e.preventDefault();

  const tenMon = document.getElementById('tenMon').value.trim();
  const gioiThieu = document.getElementById('gioiThieu').value.trim();
  const nguyenLieu = document.getElementById('nguyenLieu').value.trim();
  const maKhoaHoc = document.getElementById('maKhoaHoc').value;
  const maDanhMuc = document.getElementById('maDanhMuc').value;

  if (!tenMon || !gioiThieu || !nguyenLieu) {
    alert('Vui lòng điền đầy đủ thông tin');
    return;
  }

  if (!maKhoaHoc) {
    alert('Vui lòng chọn khóa học cho món ăn');
    return;
  }

  if (!maDanhMuc) {
    alert('Vui lòng chọn danh mục cho món ăn');
    return;
  }

  // Count existing images (not marked for deletion)
  const existingImages = document.querySelectorAll('[data-existing]');
  const existingImagesCount = existingImages.length;
  const totalImages = existingImagesCount + selectedImages.length;

  if (totalImages < 2) {
    alert(`Món ăn phải có đúng 2 ảnh. Hiện tại: ${existingImagesCount} ảnh cũ + ${selectedImages.length} ảnh mới = ${totalImages}/2`);
    return;
  }

  if (totalImages > 2) {
    alert(`Món ăn chỉ được có tối đa 2 ảnh. Hiện tại: ${existingImagesCount} ảnh cũ + ${selectedImages.length} ảnh mới = ${totalImages}/2`);
    return;
  }

  try {
    showLoading();

    // Create/Update dish với maKhoaHoc và maDanhMuc
    const dishData = {
      tenMon,
      gioiThieu,
      nguyenLieu,
      maKhoaHoc: parseInt(maKhoaHoc),
      maDanhMuc: parseInt(maDanhMuc),
    };

    console.log('Saving dish data:', dishData);

    let dishId;
    if (editingDishId) {
      const response = await apiService.put(`/monan/${editingDishId}`, dishData);
      console.log('Update response:', response);
      
      // Check if response is error
      if (response && response.success === false) {
        throw new Error(response.message || 'Lỗi cập nhật món ăn');
      }
      
      dishId = editingDishId;

      // Delete marked images
      for (const imageId of imagesToDelete) {
        try {
          await apiService.delete(`/hinhanh-monan/${imageId}`);
        } catch (error) {
          console.error('Error deleting image:', error);
        }
      }

      alert('Cập nhật món ăn thành công');
    } else {
      const response = await apiService.post('/monan', dishData);
      console.log('Create response:', response);
      
      // Check if response is error
      if (response && response.success === false) {
        throw new Error(response.message || 'Lỗi tạo món ăn');
      }
      
      if (!response || !response.maMonAn) {
        throw new Error('Không nhận được ID món ăn từ server');
      }
      
      dishId = response.maMonAn;
      alert('Thêm món ăn thành công');
    }

    // Upload new images
    for (let i = 0; i < selectedImages.length; i++) {
      const imageFormData = new FormData();
      imageFormData.append('file', selectedImages[i]);
      imageFormData.append('maMonAn', dishId);
      imageFormData.append('thuTu', i + 1);

      const uploadResponse = await apiService.upload('/hinhanh-monan', imageFormData);
      console.log('Upload response:', uploadResponse);
    }

    closeModal('dishModal');
    resetForm();
    loadDishes();
  } catch (error) {
    console.error('Error saving dish:', error);
    alert('Không thể lưu món ăn: ' + (error.message || 'Lỗi không xác định'));
    hideLoading();
  }
});

// Reset form
function resetForm() {
  document.getElementById('dishForm').reset();
  document.getElementById('dishId').value = '';
  document.getElementById('maKhoaHoc').value = '';
  document.getElementById('maDanhMuc').value = '';
  document.getElementById('modalTitle').textContent = 'Thêm Món Ăn Mới';
  document.getElementById('imagePreviewContainer').innerHTML = '';
  editingDishId = null;
  selectedImages = [];
  imagesToDelete = [];
}

// Event listeners
function initEventListeners() {
  document.getElementById('btnAddDish').addEventListener('click', () => {
    resetForm();
    openModal('dishModal');
  });

  document.getElementById('searchDish').addEventListener('input', (e) => {
    searchDishes(e.target.value);
  });

  document.querySelectorAll('.close, .close-modal').forEach((btn) => {
    btn.addEventListener('click', () => {
      closeModal('dishModal');
      resetForm();
    });
  });
}
