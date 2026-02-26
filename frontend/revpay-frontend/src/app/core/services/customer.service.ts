import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CustomerDto {
  id: number;
  name: string;
  email: string;
  address: string;
}

export interface CreateCustomerPayload {
  name: string;
  email: string;
  address: string;
}

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getCustomers(): Observable<CustomerDto[]> {
    return this.http.get<CustomerDto[]>(`${this.API}/customers`);
  }

  createCustomer(payload: CreateCustomerPayload): Observable<CustomerDto> {
    return this.http.post<CustomerDto>(`${this.API}/customers`, payload);
  }

  updateCustomer(id: number, payload: CreateCustomerPayload): Observable<CustomerDto> {
    return this.http.put<CustomerDto>(`${this.API}/customers/${id}`, payload);
  }

  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/customers/${id}`);
  }
}

