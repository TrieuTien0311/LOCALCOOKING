document.addEventListener("DOMContentLoaded", () => {
  loadTeachers();
  const teacherModal = new Modal("teacherModal");

  // Add Teacher Button
  document.getElementById("btnAddTeacher").addEventListener("click", () => {
    document.getElementById("modalTitle").textContent = "Thêm Giáo Viên Mới";
    document.getElementById("teacherForm").reset();
    document.getElementById("teacherId").value = "";
    teacherModal.show();
  });

  // Search functionality
  document.getElementById("searchTeacher").addEventListener(
    "input",
    debounce(() => {
      loadTeachers();
    }, 500)
  );

  // Form Submit
  document.getElementById("teacherForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    await saveTeacher();
    teacherModal.hide();
  });
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

async function loadTeachers() {
  const tbody = document.getElementById("teachersTableBody");
  tbody.innerHTML =
    '<tr><td colspan="6" class="text-center">Đang tải dữ liệu...</td></tr>';

  try {
    const searchQuery = document
      .getElementById("searchTeacher")
      .value.toLowerCase()
      .trim();

    // Fetch from API
    const response = await apiService.get("/giaovien");

    if (!response || response.success === false) {
      throw new Error(response?.message || "Không thể tải dữ liệu giáo viên");
    }

    let teachers = Array.isArray(response) ? response : [];

    // Filter by search query
    if (searchQuery) {
      teachers = teachers.filter((t) => {
        const name = (t.hoTen || "").toLowerCase();
        const email = (t.email || "").toLowerCase();
        const phone = (t.soDienThoai || "").toLowerCase();
        const expertise = (t.chuyenMon || "").toLowerCase();
        return (
          name.includes(searchQuery) ||
          email.includes(searchQuery) ||
          phone.includes(searchQuery) ||
          expertise.includes(searchQuery)
        );
      });
    }

    if (teachers.length === 0) {
      tbody.innerHTML =
        '<tr><td colspan="6" class="text-center">Không tìm thấy giáo viên</td></tr>';
      return;
    }

    tbody.innerHTML = teachers
      .map(
        (t) => `
        <tr>
          <td>
            <div class="teacher-name">
              <strong>${t.hoTen || "-"}</strong>
            </div>
          </td>
          <td>
            <span class="text-muted">${t.email || "-"}</span>
          </td>
          <td>
            <span class="text-muted">${t.soDienThoai || "-"}</span>
          </td>
          <td>
            <span class="badge badge-primary">${t.chuyenMon || "-"}</span>
          </td>
          <td>
            <span class="text-truncate" title="${escapeHtml(t.moTa || "-")}">${truncateText(t.moTa || "-", 40)}</span>
          </td>
          <td class="text-center">
            <div class="action-buttons">
              <button class="btn-action btn-action-edit" onclick="editTeacher(${t.maGiaoVien})" title="Chỉnh sửa">
                <i class="fas fa-edit"></i>
              </button>
              <button class="btn-action btn-action-delete" onclick="deleteTeacher(${t.maGiaoVien}, '${escapeHtml(t.hoTen)}')" title="Xóa">
                <i class="fas fa-trash-alt"></i>
              </button>
            </div>
          </td>
        </tr>
      `
      )
      .join("");
  } catch (error) {
    console.error("Error loading teachers:", error);
    tbody.innerHTML = `<tr><td colspan="6" class="text-center text-red">Lỗi: ${error.message}</td></tr>`;
  }
}

function truncateText(text, maxLength) {
  if (!text || text === "-") return "-";
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + "...";
}

function escapeHtml(text) {
  if (!text) return "";
  return text.replace(/'/g, "\\'").replace(/"/g, "&quot;");
}

async function saveTeacher() {
  try {
    const teacherId = document.getElementById("teacherId").value;
    const formData = {
      hoTen: document.getElementById("hoTen").value.trim(),
      email: document.getElementById("email").value.trim(),
      soDienThoai: document.getElementById("soDienThoai").value.trim(),
      chuyenMon: document.getElementById("chuyenMon").value.trim(),
      moTa: document.getElementById("moTa").value.trim(),
    };

    // Validate
    if (!formData.hoTen || !formData.email || !formData.soDienThoai || !formData.chuyenMon) {
      alert("Vui lòng điền đầy đủ thông tin bắt buộc");
      return;
    }

    let response;
    if (teacherId) {
      // Update
      response = await apiService.put(`/giaovien/${teacherId}`, {
        maGiaoVien: parseInt(teacherId),
        ...formData,
      });
    } else {
      // Create
      response = await apiService.post("/giaovien", formData);
    }

    if (response && (response.maGiaoVien || response.success !== false)) {
      if (typeof showToast === "function") {
        showToast(
          teacherId ? "Cập nhật giáo viên thành công" : "Thêm giáo viên thành công",
          "success"
        );
      } else {
        alert(teacherId ? "Cập nhật giáo viên thành công" : "Thêm giáo viên thành công");
      }
      loadTeachers();
    } else {
      throw new Error(response?.message || "Lưu thất bại");
    }
  } catch (error) {
    console.error("Error saving teacher:", error);
    alert(`Lỗi: ${error.message || "Không thể lưu giáo viên"}`);
  }
}

window.editTeacher = async function (teacherId) {
  try {
    const response = await apiService.get(`/giaovien/${teacherId}`);

    if (!response) {
      alert("Không tìm thấy giáo viên");
      return;
    }

    const teacher = response;

    // Fill form
    document.getElementById("modalTitle").textContent = "Chỉnh Sửa Giáo Viên";
    document.getElementById("teacherId").value = teacher.maGiaoVien;
    document.getElementById("hoTen").value = teacher.hoTen || "";
    document.getElementById("email").value = teacher.email || "";
    document.getElementById("soDienThoai").value = teacher.soDienThoai || "";
    document.getElementById("chuyenMon").value = teacher.chuyenMon || "";
    document.getElementById("moTa").value = teacher.moTa || "";

    const teacherModal = new Modal("teacherModal");
    teacherModal.show();
  } catch (error) {
    console.error("Error loading teacher:", error);
    alert(`Lỗi: ${error.message || "Không thể tải thông tin giáo viên"}`);
  }
};

window.deleteTeacher = async function (teacherId, teacherName) {
  if (!confirm(`Bạn có chắc muốn xóa giáo viên "${teacherName}"?\n\nLưu ý: Các lớp học liên quan sẽ bị ảnh hưởng.`)) {
    return;
  }

  try {
    const response = await apiService.delete(`/giaovien/${teacherId}`);

    // Check if delete was successful (some APIs return empty response on success)
    if (response === null || response === undefined || response.success !== false) {
      if (typeof showToast === "function") {
        showToast("Xóa giáo viên thành công", "success");
      } else {
        alert("Xóa giáo viên thành công");
      }
      loadTeachers();
    } else {
      throw new Error(response?.message || "Xóa thất bại");
    }
  } catch (error) {
    console.error("Error deleting teacher:", error);
    alert(`Lỗi: ${error.message || "Không thể xóa giáo viên"}`);
  }
};
