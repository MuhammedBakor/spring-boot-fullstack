import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {MenuBarComponent} from '../menu-bar/menu-bar-component';
import {HeaderBarComponent} from '../header-bar-component/header-bar-component';
import {ButtonDirective, ButtonLabel} from 'primeng/button';
import {Drawer} from 'primeng/drawer';
import {ManageCustomerComponent} from '../manage-customer-component/manage-customer-component';
import {CustomerDTO} from '../../models/Customer-dto';
import {CustomerService} from '../../services/customer/customer-service';
import {CustomerCardComponent} from '../customer-card-component/customer-card-component';
import {NgForOf} from '@angular/common';
import {CustomerRegistrationRequest} from '../../models/customer-registration-request';
import {Toast} from 'primeng/toast';
import {ConfirmationService, MessageService} from 'primeng/api';
import {ConfirmDialog} from 'primeng/confirmdialog';

@Component({
  selector: 'app-customer',
  imports: [
    MenuBarComponent,
    HeaderBarComponent,
    ButtonDirective,
    Drawer,
    ManageCustomerComponent,
    CustomerCardComponent,
    NgForOf,
    Toast,
    ConfirmDialog
  ],
  templateUrl: './customer-component.html',
  styleUrl: './customer-component.scss'
})
export class CustomerComponent implements OnInit {
  display = false;

  operation: 'create' | 'update' = 'create';

  customers: CustomerDTO[] = [];

  customer: CustomerRegistrationRequest = {}

  constructor(
    private customerService: CustomerService,
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    ) {}

  ngOnInit(): void {
    this.findALlCustomers();
    }

    private findALlCustomers(){
    this.customerService.findAll()
      .subscribe({
        next: (data) => {
          this.customers = data;
          console.log(data);
          this.cd.detectChanges();
        }
      })
    }

  save(customer: CustomerRegistrationRequest) {
    if (customer) {
      if (this.operation === 'create') {
        this.customerService.registerCustomer(customer)
          .subscribe({
            next: () => {
              this.findALlCustomers();
              this.display = false;
              this.customer = {}
              this.messageService.add({
                severity: 'success',
                summary: 'Customer saved',
                detail:  `Customer ${customer.name} was successfully saved`});
            }
          });
      } else if (this.operation === 'update') {
        this.customerService.updateCustomer(customer.id, customer)
        .subscribe({
          next: () => {
            this.findALlCustomers();this.display = false;
            this.customer = {}
            this.messageService.add({
              severity: 'success',
              summary: 'Customer updated',
              detail:  `Customer ( ${customer.name} ) was successfully updated`});
          }
        });
      }
    }
  }

  deleteCustomer(customer: CustomerDTO) {
    this.confirmationService.confirm({
      header: 'Delete Customer',
      message: `Are you sure you want to delete ( ${customer.name} )? you can\'t undone this action afterword?`,
      accept: () => {
        this.customerService.deleteCustomer(customer.id)
          .subscribe({
            next: () => {
              this.findALlCustomers();
              this.messageService.add({
                severity: 'success',
                summary: 'Customer deleted',
                detail:  `Customer ( ${customer.name} ) was successfully deleted`}
              );
            }
          })
      }
    })
  }

  updateCustomer(customerDTO: CustomerDTO) {
    this.display = true;
    this.customer = customerDTO;
    this.operation = 'update';
  }

  createCustomer() {
    this.display = true;
    this.customer = {};
    this.operation = 'create';
  }

  cancel() {
    this.display = false;
    this.customer = {};
    this.operation = 'create';
  }
}
