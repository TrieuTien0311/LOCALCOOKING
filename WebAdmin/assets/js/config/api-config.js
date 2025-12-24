/**
 * API Configuration
 * Cấu hình các endpoints và headers cho API
 */

const API_CONFIG = {
    // Base URL
    BASE_URL: 'http://localhost:8080/api',
    
    // Timeout (ms)
    TIMEOUT: 30000,
    
    // Headers mặc định
    HEADERS: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    },

    // Upload configuration
    UPLOAD: {
        MAX_SIZE: 10 * 1024 * 1024, // 10MB
        ALLOWED_TYPES: ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'video/mp4'],
        HEADERS: {
            'Accept': 'application/json',
            // Content-Type sẽ tự động set khi upload file
        }
    }
};

/**
 * API Endpoints
 * Tất cả các endpoint của hệ thống
 */
const API_ENDPOINTS = {
    
    // ==================== AUTH & USER ====================
    AUTH: {
        LOGIN: '/nguoidung/login',
        REGISTER: '/nguoidung/register',
        CHANGE_PASSWORD_SEND_OTP: '/nguoidung/change-password/send-otp',
        CHANGE_PASSWORD_VERIFY: '/nguoidung/change-password/verify',
        GOOGLE_LOGIN: '/nguoidung/google-login',
    },

    OTP: {
        SEND: '/otp/send',
        VERIFY: '/otp/verify',
    },

    PASSWORD_RESET: {
        FORGOT_PASSWORD: '/quenmatkhau/forgot-password',
        VERIFY_RESET_OTP: '/quenmatkhau/verify-reset-otp',
        RESET_PASSWORD: '/quenmatkhau/reset-password',
    },

    // ==================== NGƯỜI DÙNG ====================
    USERS: {
        GET_ALL: '/nguoidung',
        GET_BY_ID: (id) => `/nguoidung/${id}`,
        CREATE: '/nguoidung',
        UPDATE: (id) => `/nguoidung/${id}`,
        DELETE: (id) => `/nguoidung/${id}`,
    },

    // ==================== GIÁO VIÊN ====================
    TEACHERS: {
        GET_ALL: '/giaovien',
        GET_BY_ID: (id) => `/giaovien/${id}`,
        CREATE: '/giaovien',
        UPDATE: (id) => `/giaovien/${id}`,
        DELETE: (id) => `/giaovien/${id}`,
    },

    // ==================== KHÓA HỌC ====================
    COURSES: {
        GET_ALL: '/khoahoc',
        GET_BY_ID: (id) => `/khoahoc/${id}`,
        SEARCH: '/khoahoc/search', // ?diaDiem=&ngayTimKiem=
        CREATE: '/khoahoc',
        UPDATE: (id) => `/khoahoc/${id}`,
        DELETE: (id) => `/khoahoc/${id}`,
    },

    // ==================== LỊCH TRÌNH LỚP HỌC ====================
    SCHEDULES: {
        GET_ALL: '/lichtrinh',
        GET_BY_ID: (id) => `/lichtrinh/${id}`,
        GET_BY_COURSE: (courseId) => `/lichtrinh/khoahoc/${courseId}`,
        GET_BY_TEACHER: (teacherId) => `/lichtrinh/giaovien/${teacherId}`,
        GET_BY_LOCATION: '/lichtrinh/diadiem', // ?diaDiem=
        GET_ACTIVE: '/lichtrinh/active',
        CHECK_SEATS: '/lichtrinh/check-seats', // ?maLichTrinh=&ngayThamGia=
        CREATE: '/lichtrinh',
        UPDATE: (id) => `/lichtrinh/${id}`,
        DELETE: (id) => `/lichtrinh/${id}`,
    },

    // ==================== DANH MỤC MÓN ĂN ====================
    DISH_CATEGORIES: {
        GET_ALL: '/danhmucmonan',
        GET_BY_ID: (id) => `/danhmucmonan/${id}`,
        GET_BY_COURSE: (courseId) => `/danhmucmonan/lophoc/${courseId}`,
        CREATE: '/danhmucmonan',
        UPDATE: (id) => `/danhmucmonan/${id}`,
        DELETE: (id) => `/danhmucmonan/${id}`,
    },

    // ==================== MÓN ĂN ====================
    DISHES: {
        GET_ALL: '/monan',
        GET_BY_ID: (id) => `/monan/${id}`,
        GET_BY_COURSE: (courseId) => `/monan/lophoc/${courseId}`,
        GET_BY_CATEGORY: (categoryId) => `/monan/danhmuc/${categoryId}`,
        CREATE: '/monan',
        UPDATE: (id) => `/monan/${id}`,
        DELETE: (id) => `/monan/${id}`,
    },

    // ==================== HÌNH ẢNH ====================
    IMAGES: {
        // Hình ảnh món ăn
        DISH_IMAGES: (dishId) => `/hinhanh-monan/monan/${dishId}`,
        CREATE_DISH_IMAGE: '/hinhanh-monan',
        DELETE_DISH_IMAGE: (imageId) => `/hinhanh-monan/${imageId}`,
        
        // Hình ảnh khóa học
        COURSE_IMAGES: '/hinhanhkhoahoc',
        COURSE_IMAGE_BY_ID: (id) => `/hinhanhkhoahoc/${id}`,
        CREATE_COURSE_IMAGE: '/hinhanhkhoahoc',
        UPDATE_COURSE_IMAGE: (id) => `/hinhanhkhoahoc/${id}`,
        DELETE_COURSE_IMAGE: (id) => `/hinhanhkhoahoc/${id}`,
    },

    // ==================== ĐẶT LỊCH ====================
    BOOKINGS: {
        GET_ALL: '/datlich',
        GET_BY_ID: (id) => `/datlich/${id}`,
        GET_BY_STUDENT: (studentId) => `/datlich/hocvien/${studentId}`,
        GET_BY_STUDENT_STATUS: (studentId, status) => `/datlich/hocvien/${studentId}/trangthai/${status}`,
        CREATE: '/datlich',
        UPDATE: (id) => `/datlich/${id}`,
        DELETE: (id) => `/datlich/${id}`,
    },

    // ==================== THANH TOÁN ====================
    PAYMENTS: {
        GET_ALL: '/thanhtoan',
        GET_BY_ID: (id) => `/thanhtoan/${id}`,
        CREATE: '/thanhtoan',
        UPDATE: (id) => `/thanhtoan/${id}`,
        DELETE: (id) => `/thanhtoan/${id}`,
    },

    // ==================== MOMO ====================
    MOMO: {
        CREATE_PAYMENT: '/momo/create-payment',
        CALLBACK: '/momo/callback',
    },

    // ==================== ĐÁNH GIÁ ====================
    REVIEWS: {
        GET_ALL: '/danhgia',
        GET_BY_ID: (id) => `/danhgia/${id}`,
        CHECK_STATUS: (bookingId) => `/danhgia/kiemtra/${bookingId}`,
        GET_BY_BOOKING: (bookingId) => `/danhgia/datlich/${bookingId}`,
        GET_BY_COURSE: (courseId) => `/danhgia/khoahoc/${courseId}`,
        CREATE: '/danhgia/tao',
        DELETE: (id) => `/danhgia/${id}`,
    },

    // ==================== THÔNG BÁO ====================
    NOTIFICATIONS: {
        GET_ALL: '/thongbao',
        GET_BY_ID: (id) => `/thongbao/${id}`,
        GET_BY_USER: (userId) => `/thongbao/user/${userId}`,
        GET_UNREAD: (userId) => `/thongbao/user/${userId}/unread`,
        GET_UNREAD_COUNT: (userId) => `/thongbao/user/${userId}/unread-count`,
        GET_BY_TYPE: (userId, type) => `/thongbao/user/${userId}/type/${type}`,
        CREATE: '/thongbao',
        UPDATE: (id) => `/thongbao/${id}`,
        MARK_READ: (id) => `/thongbao/${id}/mark-read`,
        MARK_ALL_READ: (userId) => `/thongbao/user/${userId}/mark-all-read`,
        DELETE: (id) => `/thongbao/${id}`,
        DELETE_READ: (userId) => `/thongbao/user/${userId}/delete-read`,
    },

    // ==================== ưu đãi ====================
    PROMOTIONS: {
        GET_ALL: '/uudai',
        GET_BY_ID: (id) => `/uudai/${id}`,
        GET_AVAILABLE: '/uudai/available', // ?maHocVien=&soLuongNguoi=
        CREATE: '/uudai',
        APPLY: '/uudai/apply',
        CONFIRM: (id) => `/uudai/confirm/${id}`,
        UPDATE: (id) => `/uudai/${id}`,
        DELETE: (id) => `/uudai/${id}`,
    },

    // ==================== YÊU THÍCH ====================
    FAVORITES: {
        GET_ALL: '/yeuthich',
        GET_BY_ID: (id) => `/yeuthich/${id}`,
        GET_BY_USER: (userId) => `/yeuthich/hocvien/${userId}`,
        ADD: '/yeuthich',
        DELETE: (id) => `/yeuthich/${id}`,
    },

    // ==================== UPLOAD ====================
    UPLOAD: {
        IMAGE: '/upload/image',
        VIDEO: '/upload/video',
        FILE: '/upload/file',
    },
};

/**
 * HTTP Status Codes
 */
const HTTP_STATUS = {
    OK: 200,
    CREATED: 201,
    NO_CONTENT: 204,
    BAD_REQUEST: 400,
    UNAUTHORIZED: 401,
    FORBIDDEN: 403,
    NOT_FOUND: 404,
    INTERNAL_SERVER_ERROR: 500,
};

/**
 * Error Messages
 */
const ERROR_MESSAGES = {
    NETWORK_ERROR: 'Lỗi kết nối mạng. Vui lòng kiểm tra internet.',
    TIMEOUT: 'Yêu cầu quá thời gian chờ. Vui lòng thử lại.',
    UNAUTHORIZED: 'Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.',
    FORBIDDEN: 'Bạn không có quyền truy cập.',
    NOT_FOUND: 'Không tìm thấy dữ liệu.',
    SERVER_ERROR: 'Lỗi máy chủ. Vui lòng thử lại sau.',
    UNKNOWN: 'Đã xảy ra lỗi không xác định.',
};

/**
 * Request Methods
 */
const HTTP_METHODS = {
    GET: 'GET',
    POST: 'POST',
    PUT: 'PUT',
    DELETE: 'DELETE',
    PATCH: 'PATCH',
};
