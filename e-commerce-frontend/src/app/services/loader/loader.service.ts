import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class LoaderService {
  private loadingSubject = new BehaviorSubject<boolean>(false);

  constructor() {}

  setLoading(isLoading: boolean) {
    this.loadingSubject.next(isLoading);
  }

  getLoading() {
    return this.loadingSubject.asObservable();
  }
}
