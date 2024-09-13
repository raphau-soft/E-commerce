import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ButtonModule } from '../../common-components/button/button.module';
import { InputModule } from '../../common-components/input/input.module';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { SigninRequest } from '../../_models/signin-request';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [ButtonModule, InputModule, ReactiveFormsModule, RouterModule, FormsModule],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SigninComponent {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  signinForm: FormGroup;

  constructor() {
    this.signinForm = this.fb.group({
      email: ['', [Validators.required, Validators.minLength(5), Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false]
    })
  }

  submit() {
    if (this.signinForm.valid) {
      let request: SigninRequest = {
        email: this.signinForm.controls['email'].value,
        password: this.signinForm.controls['password'].value,
      }

      this.authService.signin(request).subscribe({
        next: () => {
          this.authService.setIsAuthenticated(true);
        },
        error: () => {

        }
      })
    }
  };

  get signinFormControl() {
    return this.signinForm.controls;
  }
}
