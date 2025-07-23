import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Card} from 'primeng/card';
import {PrimeTemplate} from 'primeng/api';
import {Badge} from 'primeng/badge';
import {ButtonDirective} from 'primeng/button';
import {CustomerDTO} from '../../models/Customer-dto';

@Component({
  selector: 'app-customer-card-component',
  imports: [
    Card,
    PrimeTemplate,
    Badge,
    ButtonDirective
  ],
  templateUrl: './customer-card-component.html',
  styleUrl: './customer-card-component.scss'
})
export class CustomerCardComponent {

  @Input()
  customer: CustomerDTO = {};

  @Input()
  customerIndex = 0;

  @Output()
  delete: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>();

 @Output()
  update: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>();

  get customerImage(): string {
    const gender = this.customer.gender === 'MALE' ? 'men' : 'women';
    return `https://randomuser.me/api/portraits/${gender}/${this.customerIndex}.jpg`;
  }

  onDelete() {
    this.delete.emit(this.customer);
  }

  onUpdate() {
    this.update.emit(this.customer);
  }
}
