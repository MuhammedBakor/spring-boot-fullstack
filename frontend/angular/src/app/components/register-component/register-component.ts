import {ChangeDetectorRef, Component} from '@angular/core';
import {Avatar} from "primeng/avatar";
import {ButtonDirective} from "primeng/button";
import {InputText} from "primeng/inputtext";
import {Message} from "primeng/message";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Router} from '@angular/router';
import {CustomerRegistrationRequest} from '../../models/customer-registration-request';
import {CustomerService} from '../../services/customer/customer-service';
import {AuthenticationService} from '../../services/authentication/authentication-service';
import {AuthenticationRequest} from '../../models/authentication-request';

@Component({
  selector: 'app-register-component',
  imports: [
    Avatar,
    ButtonDirective,
    InputText,
    Message,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './register-component.html',
  styleUrl: './register-component.scss'
})
export class RegisterComponent {

  errorMsg = '';

  customer: CustomerRegistrationRequest = {}

  constructor(
    private router: Router,
    private customerService: CustomerService,
    private authenticationService: AuthenticationService,
    private cd: ChangeDetectorRef
    ) {}


  login() {
    this.router.navigate(['login']);
  }

  createAccount() {
    this.customerService.registerCustomer(this.customer)
    .subscribe({
      next: () =>{
        const authReq: AuthenticationRequest = {
          username: this.customer.email,
          password: this.customer.password
        }
        this.authenticationService.login(authReq)
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
        })
    }
      }
    );
  }
}
