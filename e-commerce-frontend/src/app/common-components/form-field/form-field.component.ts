import { Component, ContentChild, OnInit } from "@angular/core";
import { ERROR_MESSAGES } from "../../utils/constants";
import { InputErrorDirective } from "../input-error/input-error.directive";
import { TranslateModule, TranslateService } from "@ngx-translate/core";

@Component({
  selector: "e-form-field",
  standalone: true,
  imports: [TranslateModule],
  templateUrl: "./form-field.component.html",
  styleUrl: "./form-field.component.scss",
})
export class FormFieldComponent implements OnInit {
  @ContentChild(InputErrorDirective, { static: true })
  errorDirective: InputErrorDirective | undefined;

  ngOnInit() {
    if (!this.errorDirective) {
      throw new Error("InputErrorDirective is required!");
    }
  }

  get errorMessage(): string | null {
    const control = this.errorDirective?.ngControl?.control;

    if (!control?.touched || !control.errors) {
      return null;
    }

    const errors = Object.entries(control.errors);
    if (!errors.length) {
      return null;
    }

    const [errorKey, errorDetails] = errors[0];
    let errorMessage = ERROR_MESSAGES[errorKey];

    if (errorMessage && typeof errorDetails === "object") {
      Object.entries(errorDetails).forEach(
        ([placeholderKey, placeholderValue]) => {
          const regex = new RegExp(`{{${placeholderKey}}}`, "g");
          errorMessage = errorMessage.replace(regex, String(placeholderValue));
        },
      );
    }

    return errorMessage;
  }
}
