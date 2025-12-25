// Dish Detail Modal Component

// Lấy base URL không có /api
function getBaseUrl() {
  return API_CONFIG.BASE_URL.replace('/api', '');
}

function openDishDetailModal(dish, images = []) {
  const modal = document.getElementById('dishDetailModal');
  if (!modal) return;

  // Update modal content
  document.getElementById('detailDishName').textContent = dish.tenMon;
  document.getElementById('detailDishId').textContent = `#${dish.maMonAn}`;
  document.getElementById('detailDishIntro').textContent = dish.gioiThieu || 'Chưa có giới thiệu';
  document.getElementById('detailDishIngredients').textContent = dish.nguyenLieu || 'Chưa có nguyên liệu';

  // Update status badges
  const categoryBadge = document.getElementById('detailDishCategory');
  if (dish.maDanhMuc && dish.tenDanhMuc) {
    categoryBadge.innerHTML = `<i class="fas fa-tag"></i> ${dish.tenDanhMuc}`;
    categoryBadge.className = 'status-badge badge-success';
  } else {
    categoryBadge.innerHTML = '<i class="fas fa-minus"></i> Chưa phân loại';
    categoryBadge.className = 'status-badge badge-secondary';
  }

  const courseBadge = document.getElementById('detailDishCourse');
  if (dish.maKhoaHoc) {
    courseBadge.innerHTML = '<i class="fas fa-link"></i> Đã gán khóa học';
    courseBadge.className = 'status-badge badge-primary';
  } else {
    courseBadge.innerHTML = '<i class="fas fa-circle"></i> Món ăn độc lập';
    courseBadge.className = 'status-badge badge-info';
  }

  // Update images - sử dụng đường dẫn đúng cho dishes
  const imageGallery = document.getElementById('detailDishImages');
  const baseUrl = getBaseUrl();
  
  if (images && images.length > 0) {
    imageGallery.innerHTML = images
      .map(
        (img, index) => {
          // Ảnh món ăn nằm trong uploads/dishes/
          // duongDan chỉ chứa tên file (vd: monan_1_abc12345.jpg)
          const imageUrl = `${baseUrl}/uploads/dishes/${img.duongDan}`;
          
          return `
            <div class="gallery-item">
              <img src="${imageUrl}" 
                   alt="${dish.tenMon} - Ảnh ${index + 1}" 
                   onclick="viewFullImage('${imageUrl}')"
                   onerror="this.onerror=null; this.src='${baseUrl}/uploads/${img.duongDan}'" />
            </div>
          `;
        }
      )
      .join('');
  } else {
    imageGallery.innerHTML = `
      <div class="no-images">
        <i class="fas fa-image"></i>
        <p>Chưa có hình ảnh</p>
      </div>
    `;
  }

  // Show modal
  modal.style.display = 'flex';
  document.body.style.overflow = 'hidden';
}

function closeDishDetailModal() {
  const modal = document.getElementById('dishDetailModal');
  if (modal) {
    modal.style.display = 'none';
    document.body.style.overflow = 'auto';
  }
}

function viewFullImage(imageUrl) {
  const fullImageModal = document.createElement('div');
  fullImageModal.className = 'full-image-modal';
  fullImageModal.innerHTML = `
    <div class="full-image-backdrop" onclick="this.parentElement.remove()">
      <div class="full-image-container">
        <img src="${imageUrl}" alt="Full size" />
        <button class="close-full-image" onclick="this.closest('.full-image-modal').remove()">
          <i class="fas fa-times"></i>
        </button>
      </div>
    </div>
  `;
  document.body.appendChild(fullImageModal);
}

// Close modal on outside click
document.addEventListener('click', (e) => {
  const modal = document.getElementById('dishDetailModal');
  if (e.target === modal) {
    closeDishDetailModal();
  }
});

// Close modal on ESC key
document.addEventListener('keydown', (e) => {
  if (e.key === 'Escape') {
    closeDishDetailModal();
  }
});
