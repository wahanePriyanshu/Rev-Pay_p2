import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserRecipient {
  id: number;
  name: string;
  email: string;
  avatarColor: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getRecipients(): Observable<UserRecipient[]> {
    return this.http.get<UserRecipient[]>(`${this.API}/users`);
  }
}
