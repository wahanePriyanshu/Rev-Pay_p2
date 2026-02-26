import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserProfile {
  fullName: string;
  email: string;
  phone: string;
  role: string;
}

export interface ChangePasswordPayload {
  oldPassword: string;
  newPassword: string;
}

export interface ChangePinPayload {
  oldPin: string;
  newPin: string;
}

export interface SetPinPayload {
  pin: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.API}/profile`);
  }

  changePassword(payload: ChangePasswordPayload): Observable<string> {
    return this.http.post(`${this.API}/profile/change-password`, payload, {
      responseType: 'text'
    });
  }

  changePin(payload: ChangePinPayload): Observable<string> {
    return this.http.post(`${this.API}/profile/change-pin`, payload, {
      responseType: 'text'
    });
  }

  setPin(payload: SetPinPayload): Observable<string> {
    return this.http.post(`${this.API}/profile/set-pin`, payload, {
      responseType: 'text'
    });
  }
}

