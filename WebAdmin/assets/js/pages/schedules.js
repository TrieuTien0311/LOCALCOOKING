// Schedules Management
let schedules = [];
let filteredSchedules = [];
let courses = [];
let teachers = [];
let currentPage = 1;
const itemsPerPage = 10;
let editingScheduleId = null;

// Initialize
document.addEventListener('DOMContentLoaded', () => {
  loadData();
  initEventListeners();
});

// Load all data
async function loadData() {
  try {
    showLoading();
    await Promise.all([
      loadSchedulesData(),
      loadCoursesData(),
      loadTeachersData()
    ]);
    populateCourseSelect();
    populateTeacherSelect();
    renderSchedules();
    hideLoading();
  } catch (error) {
    console.error('Error loading data:', error);
    alert('Không thể tải dữ liệu');
    hideLoading();
  }
}

// Load schedules data only
async function loadSchedulesData() {
  try {
    const response = await apiService.get('/lichtrinh');
    schedules = Array.isArray(response) ? response : [];
    schedules.sort((a, b) => b.maLichTrinh - a.maLichTrinh);
    filteredSchedules = [...schedules];
  } catch (error) {
    console.error('Error loading schedules:', error);
    schedules = [];
    filteredSchedules = [];
  }
}

// Load schedules và render (dùng khi reload)
async function loadSchedules() {
  try {
    showLoading();
    await loadSchedulesData();
    renderSchedules();
    hideLoading();
  } catch (error) {
    console.error('Error loading schedules:', error);
    hideLoading();
  }
}

// Load courses data only
async function loadCoursesData() {
  try {
    const response = await apiService.get('/khoahoc');
    courses = Array.isArray(response) ? response : [];
  } catch (error) {
    console.error('Error loading courses:', error);
    courses = [];
  }
}

// Load teachers data only
async function loadTeachersData() {
  try {
    const response = await apiService.get('/giaovien');
    teachers = Array.isArray(response) ? response : [];
  } catch (error) {
    console.error('Error loading teachers:', error);
    teachers = [];
  }
}

// Render schedules table
function renderSchedules() {
  const tbody = document.getElementById('schedulesTableBody');
  const start = (currentPage - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  const paginatedSchedules = filteredSchedules.slice(start, end);

  if (paginatedSchedules.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="7" class="text-center text-muted">
          <i class="fas fa-calendar-alt" style="font-size: 48px; opacity: 0.3;"></i>
          <p>Chưa có lịch trình nào</p>
        </td>
      </tr>
    `;
    return;
  }

  tbody.innerHTML = paginatedSchedules
    .map((schedule) => {
      const course = courses.find((c) => c.maKhoaHoc == schedule.maKhoaHoc);
      const teacher = teachers.find((t) => t.maGiaoVien == schedule.maGiaoVien);
      const conTrong = schedule.soLuongToiDa - (schedule.soLuongHienTai || 0);

      return `
      <tr>
        <td>
          <strong>${course ? course.tenKhoaHoc : 'N/A'}</strong>
        </td>
        <td>${teacher ? teacher.hoTen : 'N/A'}</td>
        <td>
          <span class="badge badge-info">${schedule.thuTrongTuan || 'N/A'}</span>
        </td>
        <td>
          <i class="fas fa-clock"></i> ${schedule.gioBatDau} - ${schedule.gioKetThuc}
        </td>
        <td>
          <div class="text-truncate" style="max-width: 200px;" title="${schedule.diaDiem}">
            <i class="fas fa-map-marker-alt"></i> ${schedule.diaDiem}
          </div>
        </td>
        <td>
          <span class="badge ${conTrong > 0 ? 'badge-success' : 'badge-danger'}">
            ${schedule.soLuongHienTai || 0}/${schedule.soLuongToiDa} (Còn ${conTrong})
          </span>
        </td>
        <td class="text-center">
          <div class="btn-group">
            <button class="btn-action btn-view" onclick="viewSchedule(${schedule.maLichTrinh})" title="Xem chi tiết">
              <i class="fas fa-eye"></i>
            </button>
            <button class="btn-action btn-edit" onclick="editSchedule(${schedule.maLichTrinh})" title="Chỉnh sửa">
              <i class="fas fa-edit"></i>
            </button>
            <button class="btn-action btn-delete" onclick="deleteSchedule(${schedule.maLichTrinh})" title="Xóa">
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </td>
      </tr>
    `;
    })
    .join('');

  renderPagination();
}

// Render pagination
function renderPagination() {
  const totalPages = Math.ceil(filteredSchedules.length / itemsPerPage);
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
  renderSchedules();
  document.getElementById('contentArea').scrollTop = 0;
}

// Populate course select
function populateCourseSelect() {
  const select = document.querySelector('select[name="maKhoaHoc"]');
  if (select) {
    select.innerHTML = '<option value="">Chọn khóa học...</option>' +
      courses.map((c) => `<option value="${c.maKhoaHoc}">${c.tenKhoaHoc}</option>`).join('');
  }

  const filterSelect = document.getElementById('filterCourse');
  if (filterSelect) {
    filterSelect.innerHTML = '<option value="">Tất cả khóa học</option>' +
      courses.map((c) => `<option value="${c.maKhoaHoc}">${c.tenKhoaHoc}</option>`).join('');
  }
}

// Populate teacher select
function populateTeacherSelect() {
  const select = document.querySelector('select[name="maGiaoVien"]');
  if (select) {
    select.innerHTML = '<option value="">Chọn giáo viên...</option>' +
      teachers.map((t) => `<option value="${t.maGiaoVien}">${t.hoTen}</option>`).join('');
  }

  const filterSelect = document.getElementById('filterTeacher');
  if (filterSelect) {
    filterSelect.innerHTML = '<option value="">Tất cả giáo viên</option>' +
      teachers.map((t) => `<option value="${t.maGiaoVien}">${t.hoTen}</option>`).join('');
  }
}

// Filter schedules
function filterSchedules() {
  const courseFilter = document.getElementById('filterCourse').value;
  const teacherFilter = document.getElementById('filterTeacher').value;

  filteredSchedules = schedules.filter((schedule) => {
    const matchCourse = !courseFilter || schedule.maKhoaHoc == courseFilter;
    const matchTeacher = !teacherFilter || schedule.maGiaoVien == teacherFilter;
    return matchCourse && matchTeacher;
  });

  currentPage = 1;
  renderSchedules();
}

// View schedule
function viewSchedule(id) {
  const schedule = schedules.find((s) => s.maLichTrinh === id);
  if (!schedule) return;

  const course = courses.find((c) => c.maKhoaHoc == schedule.maKhoaHoc);
  const teacher = teachers.find((t) => t.maGiaoVien == schedule.maGiaoVien);
  const conTrong = schedule.soLuongToiDa - (schedule.soLuongHienTai || 0);

  document.getElementById('detailScheduleTitle').textContent = course ? course.tenKhoaHoc : 'N/A';
  document.getElementById('detailScheduleId').textContent = `#${schedule.maLichTrinh}`;
  
  const statusBadge = document.getElementById('detailScheduleStatus');
  if (schedule.trangThai) {
    statusBadge.innerHTML = '<i class="fas fa-check-circle"></i> Đang hoạt động';
    statusBadge.className = 'status-badge badge-success';
  } else {
    statusBadge.innerHTML = '<i class="fas fa-pause-circle"></i> Tạm dừng';
    statusBadge.className = 'status-badge badge-secondary';
  }

  const seatsBadge = document.getElementById('detailScheduleSeats');
  seatsBadge.innerHTML = `<i class="fas fa-users"></i> ${schedule.soLuongHienTai || 0}/${schedule.soLuongToiDa} học viên (Còn ${conTrong} chỗ)`;
  seatsBadge.className = conTrong > 0 ? 'status-badge badge-info' : 'status-badge badge-danger';

  document.getElementById('detailScheduleCourse').textContent = course ? course.tenKhoaHoc : 'N/A';
  document.getElementById('detailScheduleTeacher').textContent = teacher ? teacher.hoTen : 'N/A';
  document.getElementById('detailScheduleDays').textContent = schedule.thuTrongTuan || 'N/A';
  document.getElementById('detailScheduleTime').textContent = `${schedule.gioBatDau} - ${schedule.gioKetThuc}`;
  document.getElementById('detailScheduleLocation').textContent = schedule.diaDiem;

  openScheduleDetailModal();
}

function openScheduleDetailModal() {
  const modal = document.getElementById('scheduleDetailModal');
  if (modal) {
    modal.style.display = 'flex';
    document.body.style.overflow = 'hidden';
  }
}

function closeScheduleDetailModal() {
  const modal = document.getElementById('scheduleDetailModal');
  if (modal) {
    modal.style.display = 'none';
    document.body.style.overflow = 'auto';
  }
}

// Edit schedule
function editSchedule(id) {
  const schedule = schedules.find((s) => s.maLichTrinh === id);
  if (!schedule) return;

  editingScheduleId = id;
  document.getElementById('modalTitle').innerHTML = '<i class="fas fa-calendar-edit"></i> Chỉnh Sửa Lịch Trình';
  document.getElementById('scheduleId').value = schedule.maLichTrinh;
  document.querySelector('select[name="maKhoaHoc"]').value = schedule.maKhoaHoc;
  document.querySelector('select[name="maGiaoVien"]').value = schedule.maGiaoVien;
  document.querySelector('input[name="gioBatDau"]').value = schedule.gioBatDau;
  document.querySelector('input[name="gioKetThuc"]').value = schedule.gioKetThuc;
  document.querySelector('input[name="thuTrongTuan"]').value = schedule.thuTrongTuan || '';
  document.querySelector('textarea[name="diaDiem"]').value = schedule.diaDiem;
  document.querySelector('input[name="soLuongToiDa"]').value = schedule.soLuongToiDa;
  document.querySelector('select[name="trangThai"]').value = schedule.trangThai ? 'true' : 'false';

  openModal('scheduleModal');
}

// Delete schedule
async function deleteSchedule(id) {
  if (!confirm('Bạn có chắc muốn xóa lịch trình này?')) return;

  try {
    showLoading();
    await apiService.delete(`/lichtrinh/${id}`);
    alert('Xóa lịch trình thành công');
    loadSchedules();
  } catch (error) {
    console.error('Error deleting schedule:', error);
    alert('Không thể xóa lịch trình');
    hideLoading();
  }
}

// Handle form submit
document.getElementById('scheduleForm').addEventListener('submit', async (e) => {
  e.preventDefault();

  const formData = new FormData(e.target);
  
  const data = {
    maKhoaHoc: parseInt(formData.get('maKhoaHoc')),
    maGiaoVien: parseInt(formData.get('maGiaoVien')),
    gioBatDau: formData.get('gioBatDau'),
    gioKetThuc: formData.get('gioKetThuc'),
    thuTrongTuan: formData.get('thuTrongTuan'),
    diaDiem: formData.get('diaDiem'),
    soLuongToiDa: parseInt(formData.get('soLuongToiDa')),
    soLuongHienTai: editingScheduleId ? undefined : 0,
    trangThai: formData.get('trangThai') === 'true'
  };

  try {
    showLoading();

    if (editingScheduleId) {
      await apiService.put(`/lichtrinh/${editingScheduleId}`, data);
      alert('Cập nhật lịch trình thành công');
    } else {
      await apiService.post('/lichtrinh', data);
      alert('Thêm lịch trình thành công');
    }

    closeModal('scheduleModal');
    resetForm();
    loadSchedules();
  } catch (error) {
    console.error('Error saving schedule:', error);
    alert('Không thể lưu lịch trình: ' + (error.message || 'Lỗi không xác định'));
    hideLoading();
  }
});

// Reset form
function resetForm() {
  document.getElementById('scheduleForm').reset();
  document.getElementById('scheduleId').value = '';
  document.getElementById('modalTitle').innerHTML = '<i class="fas fa-calendar-plus"></i> Thêm Lịch Trình';
  document.querySelector('select[name="trangThai"]').value = 'true';
  editingScheduleId = null;
}

// Event listeners
function initEventListeners() {
  document.getElementById('filterCourse').addEventListener('change', filterSchedules);
  document.getElementById('filterTeacher').addEventListener('change', filterSchedules);

  document.getElementById('btnAddSchedule').addEventListener('click', () => {
    resetForm();
    openModal('scheduleModal');
  });

  document.querySelectorAll('.close, .close-modal').forEach((btn) => {
    btn.addEventListener('click', () => {
      closeModal('scheduleModal');
      resetForm();
    });
  });
}
