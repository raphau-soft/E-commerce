import { HttpEvent, HttpHandlerFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { finalize, Observable } from "rxjs";
import { LoaderService } from "../../services/loader/loader.service";

var totalRequests = 0;

export function loadingInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
): Observable<HttpEvent<unknown>> {
  const loadingService = inject(LoaderService);
  totalRequests++;
  loadingService.setLoading(true);

  return next(req).pipe(
    finalize(() => {
      totalRequests--;
      if (totalRequests == 0) {
        loadingService.setLoading(false);
      }
    }),
  );
}
