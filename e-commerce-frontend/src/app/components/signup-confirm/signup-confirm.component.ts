import { ChangeDetectionStrategy, Component, inject } from "@angular/core";
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { AuthService } from "../../services/auth/auth.service";
import { ButtonModule } from "../../common-components/button/button.module";
import { RouterModule } from "@angular/router";
import { SignUpConfirmRequest } from "../../_models/signup-confirm-request";
import { InputStyleDirective } from "../../common-components/input-style/input.directive";
import { InputErrorDirective } from "../../common-components/input-error/input-error.directive";
import { FormFieldComponent } from "../../common-components/form-field/form-field.component";

@Component({
  selector: "app-signup-confirm",
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
  templateUrl: "./signup-confirm.component.html",
  styleUrl: "./signup-confirm.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SignupConfirmComponent {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  signupConfrimForm: FormGroup;
  constructor() {
    this.signupConfrimForm = this.fb.group({
      username: [
        "",
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(30),
        ],
      ],
      name: [
        "",
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(50),
        ],
      ],
      surname: [
        "",
        [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(100),
        ],
      ],
      phoneNumber: [
        "",
        [
          Validators.required,
          Validators.pattern(/^\+[0-9]{2} [0-9]{3} [0-9]{3} [0-9]{3}$/),
        ],
      ],
    });
  }

  submit() {
    if (this.signupConfrimForm.valid) {
      let request: SignUpConfirmRequest = {
        username: this.signupConfrimForm.controls["username"].value,
        name: this.signupConfrimForm.controls["name"].value,
        surname: this.signupConfrimForm.controls["surname"].value,
        phoneNumber: this.signupConfrimForm.controls["phoneNumber"].value,
      };

      this.authService.signupConfirm(request).subscribe({
        next: () => {},
        error: () => {},
      });
    }
  }

  get signupConfirmFormControl() {
    return this.signupConfrimForm.controls;
  }
}
