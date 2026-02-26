import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { tap } from 'rxjs';

export interface LoginResponse {
  token: string;
  userId: number;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API = environment.apiBaseUrl;

  // 🔹 Signals (global auth state)
  private _token = signal<string | null>(localStorage.getItem('token'));
  private _role = signal<string | null>(localStorage.getItem('role'));

 isLoggedIn(): boolean {
  return !!localStorage.getItem('token');
}

  constructor(private http: HttpClient) {}

  login(data: { emailOrPhone: string; password: string }) {
    return this.http.post<LoginResponse>(`${this.API}/auth/login`, data).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        localStorage.setItem('userId', res.userId.toString());
        this._token.set(res.token);
        this._role.set(res.role);
      })
    );
  }

  register(data: any) {
    return this.http.post(`${this.API}/auth/register`, data);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    this._token.set(null);
    this._role.set(null);
  }

  getToken(): string | null {
    return this._token();
  }
}