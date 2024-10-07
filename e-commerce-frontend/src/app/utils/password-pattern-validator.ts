import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

const upperLowerSymbolNumberRegex = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)/;

export const passwordPatternValidator: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const value = control.value;
  if (value && !upperLowerSymbolNumberRegex.test(value)) {
    return {
      weakPassword: true,
    };
  }

  return null;
};
