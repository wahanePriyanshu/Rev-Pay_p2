import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface SendMoneyRequest {
  to: string;
  amount: number;
  note?: string;
  pin: string;
}

export interface SendMoneyResponse {
  to: string;
  amount: number;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  send(request: SendMoneyRequest): Observable<SendMoneyResponse> {
    return this.http.post<SendMoneyResponse>(`${this.API}/transfer/send`, request);
  }
}

