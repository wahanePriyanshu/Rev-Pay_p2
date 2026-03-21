import { Component,OnInit } from '@angular/core';
import {CustomerService,CustomerDto} from '../../core/services/customer.service';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-customers',
  imports: [CommonModule, FormsModule],
  templateUrl: './customers.html',
  styleUrl: './customers.scss',
})
export class Customers implements OnInit {


  customers : CustomerDto[]=[];
  newCustomer: Partial<CustomerDto> = {};
  loading = false;
  error: string | null = null;
  

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers() {
    this.customerService.getCustomers().subscribe({
      next : res => this.customers = res,
      error : () => this.error = 'Failed to load customers'
    });
  }


  addCustomer() {
    if (!this.newCustomer.name || !this.newCustomer.email) {
      this.error = 'Name and email are required';
      return;
    }

    this.customerService.createCustomer(this.newCustomer as CustomerDto).subscribe({
      next : (res)=>{
        this.customers.push(res);
        this.newCustomer = {};
        this.error = null;
      },

      error : () => this.error = 'Failed to create customer'
    });
  }

  deleteCustomer(id: number) {
    this.customerService.deleteCustomer(id).subscribe({
    next : () => 
      this.customers = this.customers.filter(c => c.id !== id),

    error : () => this.error = 'Failed to delete customer'
    });
  }


}
