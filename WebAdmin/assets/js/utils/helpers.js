/**
 * Helper Functions
 * Các hàm hỗ trợ cho ứng dụng
 */

// ==================== FORMAT ====================

/**
 * Format currency VND
 */
function formatCurrency(amount) {
  if (!amount && amount !== 0) return "0đ";
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount);
}

/**
 * Format date
 */
function formatDate(date, format = "DD/MM/YYYY") {
  if (!date) return "";

  const d = new Date(date);
  const day = String(d.getDate()).padStart(2, "0");
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const year = d.getFullYear();
  const hours = String(d.getHours()).padStart(2, "0");
  const minutes = String(d.getMinutes()).padStart(2, "0");
  const seconds = String(d.getSeconds()).padStart(2, "0");

  switch (format) {
    case "DD/MM/YYYY":
      return `${day}/${month}/${year}`;
    case "DD/MM/YYYY HH:mm":
      return `${day}/${month}/${year} ${hours}:${minutes}`;
    case "DD/MM/YYYY HH:mm:ss":
      return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    case "YYYY-MM-DD":
      return `${year}-${month}-${day}`;
    case "HH:mm":
      return `${hours}:${minutes}`;
    case "HH:mm:ss":
      return `${hours}:${minutes}:${seconds}`;
    default:
      return `${day}/${month}/${year}`;
  }
}

/**
 * Format time ago (e.g., "2 giờ trước")
 */
function timeAgo(date) {
  if (!date) return "";

  const now = new Date();
  const past = new Date(date);
  const diffInSeconds = Math.floor((now - past) / 1000);

  if (diffInSeconds < 60) {
    return "Vừa xong";
  } else if (diffInSeconds < 3600) {
    const minutes = Math.floor(diffInSeconds / 60);
    return `${minutes} phút trước`;
  } else if (diffInSeconds < 86400) {
    const hours = Math.floor(diffInSeconds / 3600);
    return `${hours} giờ trước`;
  } else if (diffInSeconds < 2592000) {
    const days = Math.floor(diffInSeconds / 86400);
    return `${days} ngày trước`;
  } else if (diffInSeconds < 31536000) {
    const months = Math.floor(diffInSeconds / 2592000);
    return `${months} tháng trước`;
  } else {
    const years = Math.floor(diffInSeconds / 31536000);
    return `${years} năm trước`;
  }
}

/**
 * Format phone number
 */
function formatPhoneNumber(phone) {
  if (!phone) return "";
  // Remove all non-numeric characters
  const cleaned = phone.replace(/\D/g, "");
  // Format as: 0xxx xxx xxx
  const match = cleaned.match(/^(\d{4})(\d{3})(\d{3})$/);
  if (match) {
    return `${match[1]} ${match[2]} ${match[3]}`;
  }
  return phone;
}

// ==================== VALIDATION ====================

/**
 * Validate email
 */
function isValidEmail(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
}

/**
 * Validate phone number (Vietnam)
 */
function isValidPhone(phone) {
  const regex = /^(0|\+84)[0-9]{9}$/;
  return regex.test(phone);
}

/**
 * Validate password
 */
function isValidPassword(password) {
  // At least 6 characters
  return password && password.length >= 6;
}

/**
 * Validate required field
 */
function isRequired(value) {
  return (
    value !== null && value !== undefined && value.toString().trim() !== ""
  );
}

// ==================== STORAGE ====================

/**
 * Set local storage
 */
function setStorage(key, value) {
  try {
    localStorage.setItem(key, JSON.stringify(value));
    return true;
  } catch (error) {
    console.error("Error saving to localStorage:", error);
    return false;
  }
}

/**
 * Get local storage
 */
function getStorage(key) {
  try {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : null;
  } catch (error) {
    console.error("Error reading from localStorage:", error);
    return null;
  }
}

/**
 * Remove from local storage
 */
function removeStorage(key) {
  try {
    localStorage.removeItem(key);
    return true;
  } catch (error) {
    console.error("Error removing from localStorage:", error);
    return false;
  }
}

/**
 * Clear all local storage
 */
function clearStorage() {
  try {
    localStorage.clear();
    return true;
  } catch (error) {
    console.error("Error clearing localStorage:", error);
    return false;
  }
}

// ==================== USER SESSION ====================

/**
 * Save user session
 */
function saveUserSession(userData) {
  setStorage("user", userData);
  if (userData.token) {
    apiService.setAuthToken(userData.token);
  }
}

/**
 * Get user session
 */
function getUserSession() {
  return getStorage("user");
}

/**
 * Clear user session
 */
function clearUserSession() {
  removeStorage("user");
  apiService.setAuthToken(null);
}

/**
 * Check if user is logged in
 */
function isLoggedIn() {
  const user = getUserSession();
  return user && user.maNguoiDung;
}

/**
 * Check user role
 */
function hasRole(role) {
  const user = getUserSession();
  return user && user.vaiTro === role;
}

/**
 * Check if user is admin
 */
function isAdmin() {
  return hasRole("Admin");
}

// ==================== UI HELPERS ====================

/**
 * Show loading
 */
function showLoading(message = "Đang tải...") {
  const loading = document.getElementById("loading");
  if (loading) {
    loading.querySelector(".loading-text").textContent = message;
    loading.style.display = "flex";
  }
}

/**
 * Hide loading
 */
function hideLoading() {
  const loading = document.getElementById("loading");
  if (loading) {
    loading.style.display = "none";
  }
}

/**
 * Show toast notification
 */
function showToast(message, type = "info") {
  // Create toast element
  const toast = document.createElement("div");
  toast.className = `toast toast-${type}`;

  const icon =
    {
      success: '<i class="fas fa-check-circle"></i>',
      error: '<i class="fas fa-exclamation-circle"></i>',
      warning: '<i class="fas fa-exclamation-triangle"></i>',
      info: '<i class="fas fa-info-circle"></i>',
    }[type] || '<i class="fas fa-info-circle"></i>';

  toast.innerHTML = `
        ${icon}
        <span>${message}</span>
    `;

  // Add to body
  document.body.appendChild(toast);

  // Show toast
  setTimeout(() => toast.classList.add("show"), 100);

  // Remove toast after 3 seconds
  setTimeout(() => {
    toast.classList.remove("show");
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

/**
 * Show confirm dialog
 */
function showConfirm(message, onConfirm, onCancel) {
  const result = confirm(message);
  if (result && onConfirm) {
    onConfirm();
  } else if (!result && onCancel) {
    onCancel();
  }
  return result;
}

/**
 * Debounce function
 */
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

/**
 * Throttle function
 */
function throttle(func, limit) {
  let inThrottle;
  return function (...args) {
    if (!inThrottle) {
      func.apply(this, args);
      inThrottle = true;
      setTimeout(() => (inThrottle = false), limit);
    }
  };
}

// ==================== FILE HELPERS ====================

/**
 * Validate file size
 */
function isValidFileSize(file, maxSize = API_CONFIG.UPLOAD.MAX_SIZE) {
  return file.size <= maxSize;
}

/**
 * Validate file type
 */
function isValidFileType(file, allowedTypes = API_CONFIG.UPLOAD.ALLOWED_TYPES) {
  return allowedTypes.includes(file.type);
}

/**
 * Format file size
 */
function formatFileSize(bytes) {
  if (bytes === 0) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + " " + sizes[i];
}

/**
 * Get file extension
 */
function getFileExtension(filename) {
  return filename.slice(((filename.lastIndexOf(".") - 1) >>> 0) + 2);
}

/**
 * Read file as base64
 */
function readFileAsBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result);
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
}

// ==================== URL HELPERS ====================

/**
 * Get query parameter from URL
 */
function getQueryParam(param) {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(param);
}

/**
 * Set query parameter in URL
 */
function setQueryParam(param, value) {
  const url = new URL(window.location);
  url.searchParams.set(param, value);
  window.history.pushState({}, "", url);
}

/**
 * Remove query parameter from URL
 */
function removeQueryParam(param) {
  const url = new URL(window.location);
  url.searchParams.delete(param);
  window.history.pushState({}, "", url);
}

// ==================== TABLE HELPERS ====================

/**
 * Generate table rows HTML
 */
function generateTableRows(data, columns) {
  if (!data || data.length === 0) {
    return (
      '<tr><td colspan="' +
      columns.length +
      '" class="text-center">Không có dữ liệu</td></tr>'
    );
  }

  return data
    .map((item) => {
      const cells = columns
        .map((col) => {
          let value = item[col.key];
          if (col.render) {
            value = col.render(value, item);
          }
          return `<td>${value || ""}</td>`;
        })
        .join("");

      return `<tr>${cells}</tr>`;
    })
    .join("");
}

/**
 * Sort table data
 */
function sortTableData(data, key, order = "asc") {
  return [...data].sort((a, b) => {
    let aVal = a[key];
    let bVal = b[key];

    if (typeof aVal === "string") {
      aVal = aVal.toLowerCase();
      bVal = bVal.toLowerCase();
    }

    if (order === "asc") {
      return aVal > bVal ? 1 : -1;
    } else {
      return aVal < bVal ? 1 : -1;
    }
  });
}

// ==================== STATUS HELPERS ====================

/**
 * Get status badge HTML
 */
function getStatusBadge(status) {
  const statusConfig = {
    "Đặt trước": "warning",
    "Đã hoàn thành": "success",
    "Đã hủy": "danger",
    "Đang xử lý": "info",
    "Chờ thanh toán": "warning",
    "Đã thanh toán": "success",
    "Hoạt động": "success",
    "Không hoạt động": "secondary",
  };

  const type = statusConfig[status] || "secondary";
  return `<span class="badge badge-${type}">${status}</span>`;
}

/**
 * Get role badge HTML
 */
function getRoleBadge(role) {
  const roleConfig = {
    Admin: "danger",
    GiaoVien: "primary",
    HocVien: "success",
  };

  const type = roleConfig[role] || "secondary";
  const label =
    {
      Admin: "Quản trị viên",
      GiaoVien: "Giáo viên",
      HocVien: "Học viên",
    }[role] || role;

  return `<span class="badge badge-${type}">${label}</span>`;
}

// ==================== EXPORT ====================

/**
 * Export data to CSV
 */
function exportToCSV(data, filename = "data.csv") {
  if (!data || data.length === 0) {
    showToast("Không có dữ liệu để xuất", "warning");
    return;
  }

  const headers = Object.keys(data[0]);
  const csvContent = [
    headers.join(","),
    ...data.map((row) => headers.map((header) => row[header]).join(",")),
  ].join("\n");

  const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = filename;
  link.click();
}

/**
 * Print page
 */
function printPage() {
  window.print();
}
