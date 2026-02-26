import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface InvoiceItemDto {
  id: number;
  description: string;
  quantity: number;
  unitPrice: number;
  tax: number;
  lineTotal: number;
}

export interface InvoiceDto {
  id: number;
  businessUserId: number;
  customerId: number;
  totalAmount: number;
  status: string;
  dueDate: string;
  createdAt: string;
  items: InvoiceItemDto[];
}

export interface CreateInvoicePayload {
  customerId: number;
  dueDate: string;
  items: Array<{
    description: string;
    quantity: number;
    unitPrice: number;
    tax: number;
  }>;
}

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getInvoices(): Observable<InvoiceDto[]> {
    return this.http.get<InvoiceDto[]>(`${this.API}/invoices`);
  }

  createInvoice(payload: CreateInvoicePayload): Observable<InvoiceDto> {
    return this.http.post<InvoiceDto>(`${this.API}/invoices`, payload);
  }

  sendInvoice(id: number): Observable<InvoiceDto> {
    return this.http.put<InvoiceDto>(`${this.API}/invoices/${id}/send`, {});
  }

  payInvoice(id: number): Observable<InvoiceDto> {
    return this.http.put<InvoiceDto>(`${this.API}/invoices/${id}/pay`, {});
  }

  cancelInvoice(id: number): Observable<InvoiceDto> {
    return this.http.put<InvoiceDto>(`${this.API}/invoices/${id}/cancel`, {});
  }
}

