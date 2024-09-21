import { ChangeDetectionStrategy, Component, inject } from "@angular/core";
import { ButtonModule } from "../../common-components/button/button.module";
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { RouterModule } from "@angular/router";
import { AuthService } from "../../services/auth/auth.service";
import { SignupRequest } from "../../_models/signup-request";
import { confirmPasswordValidator } from "../../utils/password-confirm-validator";
import { InputStyleDirective } from "../../common-components/input-style/input.directive";
import { InputErrorDirective } from "../../common-components/input-error/input-error.directive";
import { FormFieldComponent } from "../../common-components/form-field/form-field.component";
import { passwordPatternValidator } from "../../utils/password-pattern-validator";

@Component({
  selector: "app-signup",
  standalone: true,
  imports: [
    ButtonModule,
    InputStyleDirective,
    InputErrorDirective,
    FormFieldComponent,
    ReactiveFormsModule,
    RouterModule,
    FormsModule,
  ],
  templateUrl: "./signup.component.html",
  styleUrl: "./signup.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SignupComponent {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  signupForm: FormGroup;

  constructor() {
    this.signupForm = this.fb.group({
      email: [
        "",
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(254),
          Validators.email,
        ],
      ],
      password: [
        "",
        [
          Validators.required,
          Validators.minLength(12),
          Validators.maxLength(64),
          passwordPatternValidator,
        ],
      ],
      confirmPassword: ["", [Validators.required, confirmPasswordValidator]],
    });
  }

  submit() {
    if (this.signupForm.valid) {
      let request: SignupRequest = {
        email: this.signupForm.controls["email"].value,
        password: this.signupForm.controls["password"].value,
        confirmPassword: this.signupForm.controls["confirmPassword"].value,
      };

      this.authService.signup(request).subscribe({
        next: () => {},
        error: () => {},
      });
    }
  }

  get signupFormControl() {
    return this.signupForm.controls;
  }
}
