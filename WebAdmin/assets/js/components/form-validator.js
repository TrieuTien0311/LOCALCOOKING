const FormValidator = {
  validate(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;

    let isValid = true;
    const requiredInputs = form.querySelectorAll("[required]");

    requiredInputs.forEach((input) => {
      if (!input.value.trim()) {
        this.showError(input, "Trường này là bắt buộc");
        isValid = false;
      } else {
        this.clearError(input);
      }
    });

    return isValid;
  },

  showError(input, message) {
    const formGroup = input.closest(".form-group");
    let error = formGroup.querySelector(".error-message");
    if (!error) {
      error = document.createElement("span");
      error.className = "error-message text-red text-sm";
      formGroup.appendChild(error);
    }
    error.textContent = message;
    input.classList.add("border-red");
  },

  clearError(input) {
    const formGroup = input.closest(".form-group");
    const error = formGroup.querySelector(".error-message");
    if (error) {
      error.remove();
    }
    input.classList.remove("border-red");
  },
};
