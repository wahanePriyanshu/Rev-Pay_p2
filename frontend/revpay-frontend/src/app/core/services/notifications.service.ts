import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface NotificationItem {
  id: number;
  title: string;
  message: string;
  read: boolean;
  createdAt: string;
}

export interface NotificationPreferences {
  TransactionAlert: boolean;
  RequestAlert: boolean;
  LowBalanceAlert: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {
  private readonly API = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getNotifications(): Observable<NotificationItem[]> {
    return this.http.get<NotificationItem[]>(`${this.API}/notifications`);
  }

  markRead(id: number): Observable<any> {
    return this.http.post(`${this.API}/notifications/${id}/read`, {});
  }

  markAllRead(ids: number[]): Observable<any[]> {
  const requests = ids.map(id => this.markRead(id));
  return forkJoin(requests);
}

  getPreferences(): Observable<NotificationPreferences> {
    return this.http.get<NotificationPreferences>(`${this.API}/notification-preferences`);
  }

  updatePreferences(prefs: NotificationPreferences): Observable<NotificationPreferences> {
    return this.http.put<NotificationPreferences>(`${this.API}/notification-preferences`, prefs);
  }
}

