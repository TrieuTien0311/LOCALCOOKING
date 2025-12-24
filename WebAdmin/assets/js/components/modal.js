class Modal {
  constructor(modalId) {
    this.modal = document.getElementById(modalId);
    if (!this.modal) return;

    this.closeBtn = this.modal.querySelector(".close");
    this.cancelBtns = this.modal.querySelectorAll(".close-modal");

    this.init();
  }

  init() {
    if (this.closeBtn) {
      this.closeBtn.addEventListener("click", () => this.hide());
    }
    if (this.cancelBtns && this.cancelBtns.length > 0) {
      this.cancelBtns.forEach(btn => {
        btn.addEventListener("click", () => this.hide());
      });
    }

    // Close when clicking outside
    window.addEventListener("click", (e) => {
      if (e.target === this.modal) {
        this.hide();
      }
    });
  }

  show() {
    this.modal.classList.add("show");
  }

  hide() {
    this.modal.classList.remove("show");
  }
}
