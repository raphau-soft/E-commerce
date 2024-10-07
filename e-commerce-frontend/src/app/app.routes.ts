import { Routes } from "@angular/router";
import { SigninComponent } from "./components/signin/signin.component";
import { SignupComponent } from "./components/signup/signup.component";
import { SignupConfirmComponent } from "./components/signup-confirm/signup-confirm.component";

export const routes: Routes = [
  { path: "signin", component: SigninComponent },
  { path: "signup", component: SignupComponent },
  { path: "signup/confirm", component: SignupConfirmComponent },
];
