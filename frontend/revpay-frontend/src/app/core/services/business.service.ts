import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface BusinessProfile {
  id: number;
  businessName: string;
  businessType: string;
  taxId: string;
  businessAddress: string;
  verificationStatus: string;
}

export interface BusinessRegisterPayload {
  businessName: string;
  businessType: string;
  taxId: string;
  businessAddress: string;
}

@Injectable({
  providedIn: 'root'
})
export class BusinessService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getMyBusiness(): Observable<BusinessProfile> {
    return this.http.get<BusinessProfile>(`${this.API}/business/me`);
  }

  registerBusiness(payload: BusinessRegisterPayload): Observable<BusinessProfile | any> {
    return this.http.post(`${this.API}/business/register`, payload);
  }
}

