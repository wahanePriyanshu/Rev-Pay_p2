import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface MoneyRequestPayload {
  to: string;
  amount: number;
  purpose: string;
}

@Injectable({
  providedIn: 'root'
})
export class MoneyRequestService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  createRequest(payload: MoneyRequestPayload): Observable<any> {
    return this.http.post(`${this.API}/requests`, payload);
  }

  getIncoming(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API}/requests/incoming`);
  }

  getOutgoing(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API}/requests/outgoing`);
  }
}

