import { Component } from '@angular/core';
import {Avatar} from 'primeng/avatar';
import {MenuItem} from 'primeng/api';
import {NgForOf} from '@angular/common';
import {MenuItemComponent} from '../menu-item-component/menu-item-component';

@Component({
  selector: 'app-menu-bar',
  imports: [
    Avatar,
    NgForOf,
    MenuItemComponent,
  ],
  templateUrl: './menu-bar-component.html',
  styleUrl: './menu-bar-component.scss'
})
export class MenuBarComponent {
  menu: Array<MenuItem> = [
    {label: 'Home', icon: 'pi pi-home',link: 'home'},
    {label: 'Customers', icon: 'pi pi-users',link: 'home'},
    {label: 'Settings', icon: 'pi pi-cog',link: 'home'},
  ];
}
