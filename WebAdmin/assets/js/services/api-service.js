/**
 * API Service
 * Service để gọi API với fetch API
 */

class ApiService {
  constructor() {
    this.baseURL = API_CONFIG.BASE_URL;
    this.timeout = API_CONFIG.TIMEOUT;
    this.defaultHeaders = API_CONFIG.HEADERS;
  }

  /**
   * Set Authorization Token
   */
  setAuthToken(token) {
    if (token) {
      this.defaultHeaders["Authorization"] = `Bearer ${token}`;
      localStorage.setItem("authToken", token);
    } else {
      delete this.defaultHeaders["Authorization"];
      localStorage.removeItem("authToken");
    }
  }

  /**
   * Get Authorization Token
   */
  getAuthToken() {
    return localStorage.getItem("authToken");
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated() {
    return !!this.getAuthToken();
  }

  /**
   * Build full URL
   */
  buildURL(endpoint, params = {}) {
    const url = new URL(this.baseURL + endpoint);
    Object.keys(params).forEach((key) => {
      if (params[key] !== null && params[key] !== undefined) {
        url.searchParams.append(key, params[key]);
      }
    });
    return url.toString();
  }

  /**
   * Handle fetch with timeout
   */
  async fetchWithTimeout(url, options) {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), this.timeout);

    try {
      const response = await fetch(url, {
        ...options,
        signal: controller.signal,
      });
      clearTimeout(timeoutId);
      return response;
    } catch (error) {
      clearTimeout(timeoutId);
      if (error.name === "AbortError") {
        throw new Error(ERROR_MESSAGES.TIMEOUT);
      }
      throw error;
    }
  }

  /**
   * Handle response
   */
  async handleResponse(response) {
    const contentType = response.headers.get("content-type");
    const isJson = contentType && contentType.includes("application/json");

    let data;
    if (isJson) {
      data = await response.json();
    } else {
      data = await response.text();
    }

    if (!response.ok) {
      // Handle HTTP errors
      const error = {
        status: response.status,
        message: data.message || ERROR_MESSAGES.UNKNOWN,
        data: data,
      };

      switch (response.status) {
        case HTTP_STATUS.UNAUTHORIZED:
          error.message = ERROR_MESSAGES.UNAUTHORIZED;
          this.setAuthToken(null); // Clear token
          window.location.href = "/login.html"; // Redirect to login
          break;
        case HTTP_STATUS.FORBIDDEN:
          error.message = ERROR_MESSAGES.FORBIDDEN;
          break;
        case HTTP_STATUS.NOT_FOUND:
          error.message = ERROR_MESSAGES.NOT_FOUND;
          break;
        case HTTP_STATUS.INTERNAL_SERVER_ERROR:
          error.message = ERROR_MESSAGES.SERVER_ERROR;
          break;
      }

      throw error;
    }

    return data;
  }

  /**
   * Handle errors
   */
  handleError(error) {
    console.error("API Error:", error);

    if (error.message === "Failed to fetch") {
      return {
        success: false,
        message: ERROR_MESSAGES.NETWORK_ERROR,
        error: error,
      };
    }

    return {
      success: false,
      message: error.message || ERROR_MESSAGES.UNKNOWN,
      error: error,
    };
  }

  /**
   * GET request
   */
  async get(endpoint, params = {}, customHeaders = {}) {
    try {
      const url = this.buildURL(endpoint, params);
      const response = await this.fetchWithTimeout(url, {
        method: HTTP_METHODS.GET,
        headers: {
          ...this.defaultHeaders,
          ...customHeaders,
        },
      });
      return await this.handleResponse(response);
    } catch (error) {
      return this.handleError(error);
    }
  }

  /**
   * POST request
   */
  async post(endpoint, data = {}, customHeaders = {}) {
    try {
      const url = this.buildURL(endpoint);
      const response = await this.fetchWithTimeout(url, {
        method: HTTP_METHODS.POST,
        headers: {
          ...this.defaultHeaders,
          ...customHeaders,
        },
        body: JSON.stringify(data),
      });
      return await this.handleResponse(response);
    } catch (error) {
      return this.handleError(error);
    }
  }

  /**
   * PUT request
   */
  async put(endpoint, data = {}, customHeaders = {}) {
    try {
      const url = this.buildURL(endpoint);
      const response = await this.fetchWithTimeout(url, {
        method: HTTP_METHODS.PUT,
        headers: {
          ...this.defaultHeaders,
          ...customHeaders,
        },
        body: JSON.stringify(data),
      });
      return await this.handleResponse(response);
    } catch (error) {
      return this.handleError(error);
    }
  }

  /**
   * DELETE request
   */
  async delete(endpoint, customHeaders = {}) {
    try {
      const url = this.buildURL(endpoint);
      const response = await this.fetchWithTimeout(url, {
        method: HTTP_METHODS.DELETE,
        headers: {
          ...this.defaultHeaders,
          ...customHeaders,
        },
      });
      return await this.handleResponse(response);
    } catch (error) {
      return this.handleError(error);
    }
  }

  /**
   * Upload file (multipart/form-data)
   */
  async upload(endpoint, formData, onProgress = null) {
    try {
      const url = this.buildURL(endpoint);

      // Create XMLHttpRequest for upload progress
      return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();

        // Progress event
        if (onProgress && xhr.upload) {
          xhr.upload.addEventListener("progress", (e) => {
            if (e.lengthComputable) {
              const percentComplete = (e.loaded / e.total) * 100;
              onProgress(percentComplete);
            }
          });
        }

        // Load event
        xhr.addEventListener("load", () => {
          if (xhr.status >= 200 && xhr.status < 300) {
            try {
              const response = JSON.parse(xhr.responseText);
              resolve(response);
            } catch (e) {
              resolve(xhr.responseText);
            }
          } else {
            reject({
              status: xhr.status,
              message: xhr.statusText,
            });
          }
        });

        // Error event
        xhr.addEventListener("error", () => {
          reject({
            message: ERROR_MESSAGES.NETWORK_ERROR,
          });
        });

        // Abort event
        xhr.addEventListener("abort", () => {
          reject({
            message: "Upload cancelled",
          });
        });

        // Open and send
        xhr.open("POST", url);

        // Set headers (không set Content-Type cho FormData)
        const token = this.getAuthToken();
        if (token) {
          xhr.setRequestHeader("Authorization", `Bearer ${token}`);
        }

        xhr.send(formData);
      });
    } catch (error) {
      return this.handleError(error);
    }
  }
}

// Create singleton instance
const apiService = new ApiService();

// Initialize auth token from localStorage
const savedToken = localStorage.getItem("authToken");
if (savedToken) {
  apiService.setAuthToken(savedToken);
}
