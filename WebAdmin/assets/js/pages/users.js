document.addEventListener("DOMContentLoaded", () => {
  loadUsers();

  // Add event listener for status filter
  document.getElementById("statusFilter").addEventListener("change", () => {
    loadUsers();
  });

  // Add event listener for search
  document.getElementById("searchUser").addEventListener(
    "input",
    debounce(() => {
      loadUsers();
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

async function loadUsers() {
  const tbody = document.getElementById("usersTableBody");
  tbody.innerHTML =
    '<tr><td colspan="6" class="text-center">Đang tải dữ liệu...</td></tr>';

  try {
    // Get filter values
    const statusFilter = document.getElementById("statusFilter").value;
    const searchQuery = document
      .getElementById("searchUser")
      .value.toLowerCase()
      .trim();

    // Fetch from API
    const response = await apiService.get("/nguoidung");

    // Check if response is successful
    if (!response || response.success === false) {
      throw new Error(response?.message || "Không thể tải dữ liệu người dùng");
    }

    // Response is an array directly from the controller
    let users = Array.isArray(response) ? response : [];

    // Filter by status
    if (statusFilter) {
      const statusMap = {
        active: "HoatDong",
        blocked: "BiKhoa",
      };
      users = users.filter((u) => u.trangThai === statusMap[statusFilter]);
    }

    // Filter by search query
    if (searchQuery) {
      users = users.filter((u) => {
        const name = (u.hoTen || "").toLowerCase();
        const email = (u.email || "").toLowerCase();
        const phone = (u.soDienThoai || "").toLowerCase();
        return (
          name.includes(searchQuery) ||
          email.includes(searchQuery) ||
          phone.includes(searchQuery)
        );
      });
    }

    if (users.length === 0) {
      tbody.innerHTML =
        '<tr><td colspan="6" class="text-center">Không tìm thấy người dùng</td></tr>';
      return;
    }

    tbody.innerHTML = users
      .map(
        (u) => `
        <tr>
          <td><strong>${u.hoTen || u.tenDangNhap}</strong></td>
          <td>${u.email}</td>
          <td>${u.soDienThoai || "-"}</td>
          <td>${getStatusBadge(u.trangThai)}</td>
          <td>${formatDate(u.ngayTao)}</td>
          <td>
            ${
              u.vaiTro === "Admin"
                ? '<span class="badge badge-primary">Quản trị viên</span>'
                : u.trangThai === "HoatDong"
                ? `<button class="btn-action-lock" onclick="toggleUserStatus(${u.maNguoiDung})" title="Khóa tài khoản"><i class="fas fa-lock"></i></button>`
                : `<button class="btn-action-unlock" onclick="toggleUserStatus(${u.maNguoiDung})" title="Mở khóa tài khoản"><i class="fas fa-unlock"></i></button>`
            }
          </td>
        </tr>
      `
      )
      .join("");
  } catch (error) {
    console.error("Error loading users:", error);
    tbody.innerHTML = `<tr><td colspan="6" class="text-center text-red">Lỗi: ${error.message}</td></tr>`;
  }
}

function getStatusBadge(status) {
  if (status === "HoatDong")
    return '<span class="user-status-active">Hoạt động</span>';
  return '<span class="user-status-blocked">Đã khóa</span>';
}

function formatDate(dateString) {
  if (!dateString) return "-";
  const date = new Date(dateString);
  return date.toLocaleDateString("vi-VN");
}

// Make globally available for onclick
window.toggleUserStatus = async function (userId) {
  try {
    // Get current user data
    const response = await apiService.get(`/nguoidung/${userId}`);

    if (!response) {
      alert("Không tìm thấy người dùng");
      return;
    }

    const user = response;
    const newStatus = user.trangThai === "HoatDong" ? "BiKhoa" : "HoatDong";
    const actionName = newStatus === "HoatDong" ? "mở khóa" : "khóa";

    if (
      confirm(
        `Bạn có chắc muốn ${actionName} tài khoản ${
          user.hoTen || user.tenDangNhap
        }?`
      )
    ) {
      // Update user status
      const updateResponse = await apiService.put(`/nguoidung/${userId}`, {
        ...user,
        trangThai: newStatus,
      });

      if (updateResponse && updateResponse.maNguoiDung) {
        // Re-render
        loadUsers();

        if (typeof showToast === "function") {
          showToast(`Đã ${actionName} tài khoản thành công`, "success");
        } else {
          alert(`Đã ${actionName} tài khoản thành công`);
        }
      } else {
        throw new Error("Cập nhật thất bại");
      }
    }
  } catch (error) {
    console.error("Error toggling user status:", error);
    alert(`Lỗi: ${error.message || "Không thể cập nhật trạng thái"}`);
  }
};
