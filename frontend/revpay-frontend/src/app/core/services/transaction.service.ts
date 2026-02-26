import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface TransactionDto {
  id: number;
  type: 'SEND' | 'RECEIVE';
  amount: number;
  from: string;
  to: string;
  note: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getMyTransactions(): Observable<TransactionDto[]> {
    return this.http.get<TransactionDto[]>(`${this.API}/transactions/my`);
  }
}

