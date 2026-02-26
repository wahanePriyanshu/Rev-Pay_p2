import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface LoanDto {
  id: number;
  amount: number;
  purpose: string;
  tenureMonths: number;
  interestRate: number;
  status: string;
  createdAt: string;
}

export interface CreateLoanPayload {
  amount: number;
  purpose: string;
  tenureMonths: number;
}

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getLoans(): Observable<LoanDto[]> {
    return this.http.get<LoanDto[]>(`${this.API}/loans`);
  }

  createLoan(payload: CreateLoanPayload): Observable<LoanDto> {
    return this.http.post<LoanDto>(`${this.API}/loans`, payload);
  }
}

