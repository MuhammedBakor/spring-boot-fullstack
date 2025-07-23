import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from '../../models/authentication-response';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // تحقق من أن الكود يعمل في المتصفح فقط
    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      const storedUser = localStorage.getItem('user');

      if (storedUser) {
        const authResponse: AuthenticationResponse = JSON.parse(storedUser);
        const token = authResponse.token;

        if (token) {
          const authReq = req.clone({
            headers: new HttpHeaders({
              Authorization: `Bearer ${token}`
            })
          });
          return next.handle(authReq);
        }
      }
    }

    return next.handle(req);
  }
}
