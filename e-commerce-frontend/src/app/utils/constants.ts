export const ERROR_MESSAGES: Record<string, string> = {
  required: "This field is required",
  minlength: "The entered value should be longer than {{requiredLength}}",
  maxlength: "The entered value should be shorter than {{requiredLength}}",
  passwordsMismatch: "Passwords should match",
  weakPassword:
    "Password must contain uppercase, lowercase, number, and special character",
  email: "Email should be valid",
};
