import {Routes} from '@angular/router';
import {CustomerComponent} from './components/customer/customer-component';
import {LoginComponent} from './components/login-component/login-component';
import {AccessGuardService} from './services/guard/access-guard-service';
import {RegisterComponent} from './components/register-component/register-component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'customers',
    component: CustomerComponent,
    canActivate: [AccessGuardService]
  }

];
