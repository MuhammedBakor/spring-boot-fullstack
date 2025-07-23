import {ChangeDetectorRef, Component} from '@angular/core';
import {Avatar} from 'primeng/avatar';
import {InputText} from 'primeng/inputtext';
import {ButtonDirective} from 'primeng/button';
import {AuthenticationRequest} from '../../models/authentication-request';
import {FormsModule} from '@angular/forms';
import {AuthenticationService} from '../../services/authentication/authentication-service';
import {Message} from 'primeng/message';
import {NgIf} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login-component',
  imports: [
    Avatar,
    InputText,
    ButtonDirective,
    FormsModule,
    Message,
    NgIf
  ],
  templateUrl: './login-component.html',
  styleUrl: './login-component.scss'
})
export class LoginComponent {
  authenticationRequest: AuthenticationRequest = {};

  errorMsg: string = '';

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    private cd: ChangeDetectorRef) {}

  login() {
    this.errorMsg = '';
    this.authenticationService.login(this.authenticationRequest)
      .subscribe({
        next: (authenticationResponse) => {
          localStorage.setItem('user', JSON.stringify(authenticationResponse));
          this.router.navigate(['customers']);
        },
        error: (err) => {
          if (err.status === 401) {
            console.log('Username or password is incorrect');
            this.errorMsg = 'Username or password is incorrect';
            this.cd.detectChanges();
          }
        }
      });
  }

  register(){
    this.router.navigate(['register']);
  }
}
