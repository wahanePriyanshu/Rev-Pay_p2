import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface WalletBalanceResponse {
  walletId: number;
  balance: number;
  pin: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getBalance(): Observable<WalletBalanceResponse> {
    return this.http.get<WalletBalanceResponse>(`${this.API}/wallet/balance`);
  }

  addMoney(amount: number, pin: string): Observable<any> {
    return this.http.post(`${this.API}/wallet/add`, { amount, pin });
  }

  withdraw(amount: number, pin: string): Observable<any> {
    return this.http.post(`${this.API}/wallet/withdraw`, { amount, pin });
  }
}

