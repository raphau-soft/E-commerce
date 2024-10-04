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
import { SigninRequest } from "../../_models/signin-request";
import { FormFieldComponent } from "../../common-components/form-field/form-field.component";
import { InputStyleDirective } from "../../common-components/input-style/input.directive";
import { InputErrorDirective } from "../../common-components/input-error/input-error.directive";

@Component({
  selector: "app-signin",
  standalone: true,
  imports: [
    ButtonModule,
    ReactiveFormsModule,
    RouterModule,
    FormsModule,
    InputErrorDirective,
    FormFieldComponent,
    InputStyleDirective,
  ],
  templateUrl: "./signin.component.html",
  styleUrl: "./signin.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SigninComponent {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  signinForm: FormGroup;

  constructor() {
    this.signinForm = this.fb.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required]],
      rememberMe: [false],
    });
  }

  submit() {
    if (this.signinForm.valid) {
      let request: SigninRequest = {
        email: this.signinForm.controls["email"].value,
        password: this.signinForm.controls["password"].value,
      };

      this.authService.signin(request).subscribe({
        next: () => {
          this.authService.setIsAuthenticated(true);
        },
        error: () => {},
      });
    }
  }

  get signinFormControl() {
    return this.signinForm.controls;
  }
}
