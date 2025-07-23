import {Component} from '@angular/core';
import {ButtonDirective, ButtonModule} from 'primeng/button';
import {Ripple} from 'primeng/ripple';
import {Avatar} from 'primeng/avatar';
import {Menu} from 'primeng/menu';
import {MenuItem, MenuItemCommandEvent} from 'primeng/api';
import {AuthenticationResponse} from '../../models/authentication-response';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header-bar-component',
  imports: [
    ButtonDirective,
    Ripple, ButtonModule, Avatar, Menu
  ],
  templateUrl: './header-bar-component.html',
  styleUrl: './header-bar-component.scss'
})
export class HeaderBarComponent {

  constructor(
    private router: Router,
  ) {}

  items: Array<MenuItem> = [
    {
      label: 'Profile',
      icon: 'pi pi-user'
    },
    {
      label: 'Settings',
      icon: 'pi pi-cog'
    },
    {
      separator: true
    },
    {
      label: 'Logout',
      icon: 'pi pi-sign-out',
      command: () => {
        localStorage.clear();
        this.router.navigate(['login']);
      }
    }
  ];

  get username(): string {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const authResponse: AuthenticationResponse = JSON.parse(storedUser);
      if (authResponse && authResponse.customerDTO && authResponse.customerDTO.username) {
        return authResponse.customerDTO.username;
      }
    }
    return '--';
  }

  get userRole(): string {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const authResponse: AuthenticationResponse = JSON.parse(storedUser);
      if (authResponse && authResponse.customerDTO && authResponse.customerDTO.roles) {
        return authResponse.customerDTO.roles[0];
      }
    }
    return '--';
  }

}
