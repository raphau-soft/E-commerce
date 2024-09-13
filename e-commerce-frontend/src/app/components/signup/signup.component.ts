import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ButtonModule } from '../../common-components/button/button.module';
import { InputModule } from '../../common-components/input/input.module';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { SignupRequest } from '../../_models/signup-request';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ButtonModule, InputModule, ReactiveFormsModule, RouterModule, FormsModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SignupComponent {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  signupForm: FormGroup;

  constructor() {
    this.signupForm = this.fb.group({
      email: ['', [Validators.required, Validators.minLength(5), Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
    })
  }

  submit() {
    if (this.signupForm.valid) {
      let request: SignupRequest = {
        email: this.signupForm.controls['email'].value,
        password: this.signupForm.controls['password'].value,
        confirmPassword: this.signupForm.controls['confirmPassword'].value
      }

      this.authService.signup(request).subscribe({
        next: () => {

        },
        error: () => {

        }
      })
    }
  };

  get signinFormControl() {
    return this.signupForm.controls;
  }
}
