class ImageUpload {
  constructor(inputId, previewId) {
    this.input = document.getElementById(inputId);
    this.preview = document.getElementById(previewId);

    if (!this.input || !this.preview) return;

    this.init();
  }

  init() {
    // Trigger file input when clicking preview area
    this.preview.addEventListener("click", () => {
      this.input.click();
    });

    // Handle file selection
    this.input.addEventListener("change", (e) => {
      const file = e.target.files[0];
      if (file) {
        this.handleFile(file);
      }
    });
  }

  handleFile(file) {
    if (!file.type.startsWith("image/")) {
      alert("Vui lòng chọn file hình ảnh!");
      return;
    }

    const reader = new FileReader();
    reader.onload = (e) => {
      // Update preview with image
      this.preview.innerHTML = `<img src="${e.target.result}" style="width:100%; height:100%; object-fit:cover; border-radius:8px;">`;

      // Add remove button logic if needed (handled in parent or here)
      // For now, simple replace
    };
    reader.readAsDataURL(file);
  }

  // Helper to upload to server
  async upload(apiEndpoint) {
    const file = this.input.files[0];
    if (!file) return null;

    const formData = new FormData();
    formData.append("file", file);

    // Use ApiService if available, or fetch directly
    // This relies on the global ApiService or just fetch
    try {
      // Example integration
      // const response = await ApiService.post(apiEndpoint, formData, true);
      // return response;
      return null; // Placeholder implementation
    } catch (error) {
      console.error("Upload failed", error);
      throw error;
    }
  }
}
