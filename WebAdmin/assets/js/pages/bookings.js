// Bookings Management
let bookings = [];
let filteredBookings = [];

// Initialize
document.addEventListener('DOMContentLoaded', () => {
  loadBookings();
  initEventListeners();
});

// Load bookings
async function loadBookings() {
  try {
    showLoading();
    // Dùng endpoint dashboard để lấy thông tin đầy đủ (tenNguoiDat, tenKhoaHoc)
    const data = await apiService.get('/datlich/dashboard');
    bookings = Array.isArray(data) ? data : [];
    filteredBookings = [...bookings];
    renderBookings();
    hideLoading();
  } catch (error) {
    console.error('Error loading bookings:', error);
    bookings = [];
    filteredBookings = [];
    renderBookings();
    hideLoading();
  }
}

// Render bookings table
function renderBookings() {
  const tbody = document.getElementById('bookingsTableBody');
  
  if (filteredBookings.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="8" class="text-center text-muted">Không có đơn đặt lịch nào</td>
      </tr>
    `;
    return;
  }
  
  tbody.innerHTML = filteredBookings.map(booking => `
    <tr>
      <td>#${booking.maDatLich}</td>
      <td>${booking.tenNguoiDat || booking.hoTenHocVien || 'N/A'}</td>
      <td>${booking.tenKhoaHoc || 'N/A'}</td>
      <td>${formatDate(booking.ngayThamGia)}</td>
      <td class="text-bold">${formatCurrency(booking.tongTien)}</td>
      <td>
        <span class="badge badge-${getStatusColor(booking.trangThai)}">
          ${getStatusText(booking.trangThai)}
        </span>
      </td>
      <td>
        <span class="badge badge-${booking.daThanhToan ? 'success' : 'warning'}">
          ${booking.daThanhToan ? 'Đã thanh toán' : 'Chưa thanh toán'}
        </span>
      </td>
      <td>
        <div class="action-buttons">
          <button class="btn-action btn-view" onclick="viewDetail(${booking.maDatLich})" title="Xem chi tiết">
            <i class="fas fa-eye"></i>
          </button>
          ${booking.trangThai === 'DatTruoc' ? `
            <button class="btn-action btn-edit" onclick="confirmComplete(${booking.maDatLich})" title="Xác nhận hoàn thành">
              <i class="fas fa-check"></i>
            </button>
            <button class="btn-action btn-delete" onclick="confirmCancel(${booking.maDatLich})" title="Hủy đơn">
              <i class="fas fa-times"></i>
            </button>
          ` : ''}
        </div>
      </td>
    </tr>
  `).join('');
}

// Get status text
function getStatusText(status) {
  const texts = {
    'DatTruoc': 'Đặt trước',
    'DaHoanThanh': 'Đã hoàn thành',
    'DaHuy': 'Đã hủy'
  };
  return texts[status] || status;
}

// Get status color
function getStatusColor(status) {
  const colors = {
    'DatTruoc': 'primary',
    'DaHoanThanh': 'success',
    'DaHuy': 'danger'
  };
  return colors[status] || 'secondary';
}

// Filter bookings
function filterBookings() {
  const status = document.getElementById('statusFilter').value;
  const fromDate = document.getElementById('fromDate').value;
  const toDate = document.getElementById('toDate').value;
  
  filteredBookings = bookings.filter(booking => {
    // Filter by status
    if (status && booking.trangThai !== status) {
      return false;
    }
    
    // Filter by date range
    if (fromDate || toDate) {
      const bookingDate = new Date(booking.ngayThamGia);
      
      if (fromDate) {
        const from = new Date(fromDate);
        if (bookingDate < from) return false;
      }
      
      if (toDate) {
        const to = new Date(toDate);
        if (bookingDate > to) return false;
      }
    }
    
    return true;
  });
  
  renderBookings();
}

// View booking detail
async function viewDetail(id) {
  try {
    showLoading();
    
    // Tìm booking từ danh sách đã load (có đầy đủ thông tin từ dashboard endpoint)
    let booking = bookings.find(b => b.maDatLich === id);
    
    // Nếu không tìm thấy trong danh sách, gọi API
    if (!booking) {
      booking = await apiService.get(`/datlich/${id}`);
    }
    
    const content = `
      <div class="booking-detail-modal">
        <!-- Header info with ID and badges -->
        <div class="detail-header-info">
          <span class="detail-booking-id">#${booking.maDatLich}</span>
          <div class="detail-status-badges">
            <span class="status-badge badge-${getStatusColor(booking.trangThai)}">
              <i class="fas fa-circle"></i> ${getStatusText(booking.trangThai)}
            </span>
            <span class="status-badge badge-${booking.daThanhToan ? 'success' : 'warning'}">
              <i class="fas ${booking.daThanhToan ? 'fa-check-circle' : 'fa-clock'}"></i>
              ${booking.daThanhToan ? 'Đã thanh toán' : 'Chưa thanh toán'}
            </span>
          </div>
        </div>

        <!-- Thông tin người đặt -->
        <div class="detail-section">
          <div class="detail-section-title">
            <i class="fas fa-user"></i> Thông Tin Người Đặt
          </div>
          <div class="detail-info-grid">
            <div class="detail-info-item">
              <i class="fas fa-user-circle"></i>
              <div>
                <label>Họ và tên</label>
                <span>${booking.tenNguoiDat || booking.hoTenHocVien || 'N/A'}</span>
              </div>
            </div>
            <div class="detail-info-item">
              <i class="fas fa-envelope"></i>
              <div>
                <label>Email</label>
                <span>${booking.emailNguoiDat || booking.emailHocVien || 'N/A'}</span>
              </div>
            </div>
            <div class="detail-info-item">
              <i class="fas fa-phone"></i>
              <div>
                <label>Số điện thoại</label>
                <span>${booking.sdtNguoiDat || 'N/A'}</span>
              </div>
            </div>
            <div class="detail-info-item">
              <i class="fas fa-calendar-alt"></i>
              <div>
                <label>Ngày đặt</label>
                <span>${formatDateTime(booking.ngayDat)}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Thông tin khóa học -->
        <div class="detail-section">
          <div class="detail-section-title">
            <i class="fas fa-book"></i> Thông Tin Khóa Học
          </div>
          <div class="detail-info-grid">
            <div class="detail-info-item full-width">
              <i class="fas fa-graduation-cap"></i>
              <div>
                <label>Tên khóa học</label>
                <span class="course-name">${booking.tenKhoaHoc || 'N/A'}</span>
              </div>
            </div>
            <div class="detail-info-item">
              <i class="fas fa-calendar-day"></i>
              <div>
                <label>Ngày tham gia</label>
                <span>${formatDate(booking.ngayThamGia)}</span>
              </div>
            </div>
            <div class="detail-info-item">
              <i class="fas fa-users"></i>
              <div>
                <label>Số lượng người</label>
                <span class="highlight">${booking.soLuongNguoi} người</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Thông tin thanh toán -->
        <div class="detail-section payment-section">
          <div class="detail-section-title">
            <i class="fas fa-money-bill-wave"></i> Thông Tin Thanh Toán
          </div>
          <div class="payment-summary">
            <div class="payment-row">
              <span>Giá khóa học</span>
              <span>${formatCurrency(booking.tongTien / booking.soLuongNguoi)}</span>
            </div>
            <div class="payment-row">
              <span>Số lượng người</span>
              <span>× ${booking.soLuongNguoi}</span>
            </div>
            <div class="payment-row total">
              <span>Tổng cộng</span>
              <span class="total-amount">${formatCurrency(booking.tongTien)}</span>
            </div>
          </div>
        </div>

        ${booking.ghiChu ? `
          <!-- Ghi chú -->
          <div class="detail-section">
            <div class="detail-section-title">
              <i class="fas fa-sticky-note"></i> Ghi Chú
            </div>
            <div class="note-section">${booking.ghiChu}</div>
          </div>
        ` : ''}

        <!-- Actions -->
        ${booking.trangThai === 'DatTruoc' || booking.trangThai === 'Đặt trước' ? `
          <div class="modal-actions-bottom">
            <button class="btn btn-success" onclick="confirmComplete(${booking.maDatLich}); closeModal('detailModal');">
              <i class="fas fa-check"></i> Xác Nhận Hoàn Thành
            </button>
            <button class="btn btn-danger" onclick="confirmCancel(${booking.maDatLich}); closeModal('detailModal');">
              <i class="fas fa-times"></i> Hủy Đơn
            </button>
          </div>
        ` : ''}
      </div>
    `;
    
    document.getElementById('bookingDetailContent').innerHTML = content;
    openModal('detailModal');
    hideLoading();
  } catch (error) {
    console.error('Error loading booking detail:', error);
    alert('Không thể tải chi tiết đơn');
    hideLoading();
  }
}

// Confirm complete booking
function confirmComplete(id) {
  if (!confirm('Xác nhận đơn này đã hoàn thành?')) return;
  
  completeBooking(id);
}

// Complete booking
async function completeBooking(id) {
  try {
    showLoading();
    
    // Get current booking data
    const booking = await apiService.get(`/datlich/${id}`);
    
    // Update status to "DaHoanThanh"
    booking.trangThai = 'DaHoanThanh';
    await apiService.put(`/datlich/${id}`, booking);
    
    alert('Đã xác nhận hoàn thành đơn');
    loadBookings();
  } catch (error) {
    console.error('Error completing booking:', error);
    alert('Không thể xác nhận hoàn thành đơn');
    hideLoading();
  }
}

// Confirm cancel booking
function confirmCancel(id) {
  if (!confirm('Bạn có chắc muốn hủy đơn này?')) return;
  
  cancelBooking(id);
}

// Cancel booking
async function cancelBooking(id) {
  try {
    showLoading();
    
    await apiService.put(`/datlich/${id}/cancel`);
    
    alert('Đã hủy đơn thành công');
    loadBookings();
  } catch (error) {
    console.error('Error cancelling booking:', error);
    alert('Không thể hủy đơn: ' + (error.message || 'Lỗi không xác định'));
    hideLoading();
  }
}

// Event listeners
function initEventListeners() {
  document.getElementById('btnFilter').addEventListener('click', filterBookings);
  
  // Reset filter when status changes
  document.getElementById('statusFilter').addEventListener('change', filterBookings);
  
  // Close modal buttons
  const detailModal = document.getElementById('detailModal');
  const closeBtn = detailModal.querySelector('.close');
  
  if (closeBtn) {
    closeBtn.addEventListener('click', () => {
      closeModal('detailModal');
    });
  }
  
  // Close when clicking outside
  window.addEventListener('click', (e) => {
    if (e.target === detailModal) {
      closeModal('detailModal');
    }
  });
}

// Format helpers
function formatDate(dateString) {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString('vi-VN');
}

function formatDateTime(dateString) {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleString('vi-VN');
}

function formatCurrency(amount) {
  if (!amount) return '0 VNĐ';
  return amount.toLocaleString('vi-VN') + ' VNĐ';
}
