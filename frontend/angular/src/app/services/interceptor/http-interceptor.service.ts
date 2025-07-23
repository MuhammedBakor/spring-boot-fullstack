import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from '../../models/authentication-response';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor {

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    if (isPlatformBrowser(this.platformId)) {
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        const authResponse: AuthenticationResponse = JSON.parse(storedUser);
        const token = authResponse.token;
        if (token) {
          authReq = req.clone({
            headers: new HttpHeaders({
              Authorization: `Bearer ${token}`
            })
          });
        }
      }
    }
    return next.handle(authReq);
  }
}
