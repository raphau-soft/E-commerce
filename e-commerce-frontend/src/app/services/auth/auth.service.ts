import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { SignupRequest } from "../../_models/signup-request";
import { SigninRequest } from "../../_models/signin-request";
import { SignUpConfirmRequest } from "../../_models/signup-confirm-request";

@Injectable({
  providedIn: "root",
})
export class AuthService {
  private url: string = environment.apiUrl + "/auth";
  private isAuthenticated = false;

  constructor(private http: HttpClient) {}

  signup(request: SignupRequest): Observable<any> {
    return this.http.post(this.url + "/signup", request);
  }

  signupConfirm(request: SignUpConfirmRequest): Observable<any> {
    return this.http.post(this.url + "/signup/confirm", request);
  }

  signin(request: SigninRequest): Observable<any> {
    return this.http.post(this.url + "/token", request);
  }

  getIsAuthenticated(): boolean {
    return this.isAuthenticated;
  }

  setIsAuthenticated(isAuthenticated: boolean) {
    this.isAuthenticated = isAuthenticated;
  }
}
