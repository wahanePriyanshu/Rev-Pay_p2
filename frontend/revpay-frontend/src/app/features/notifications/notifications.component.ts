import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificationsService, NotificationItem, NotificationPreferences } from '../../core/services/notifications.service';

type NotificationTab = 'all' | 'unread' | 'preferences';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {
  activeTab: NotificationTab = 'all';

  notifications: NotificationItem[] = [];
  preferences: NotificationPreferences = {
    emailEnabled: true,
    smsEnabled: false,
    pushEnabled: true
  };

  loading = false;
  message: string | null = null;
  error: string | null = null;

  constructor(private notificationsService: NotificationsService) {}

  ngOnInit(): void {
    this.loadNotifications();
    this.loadPreferences();
  }

  setTab(tab: NotificationTab) {
    this.activeTab = tab;
    this.message = null;
    this.error = null;
  }

  private loadNotifications() {
    this.notificationsService.getNotifications().subscribe({
      next: res => {
        this.notifications = res;
      },
      error: () => {
        this.error = 'Failed to load notifications';
      }
    });
  }

  private loadPreferences() {
    this.notificationsService.getPreferences().subscribe({
      next: res => {
        this.preferences = res;
      },
      error: () => {
        // keep defaults
      }
    });
  }

  get unreadNotifications(): NotificationItem[] {
    return this.notifications.filter(n => !n.read);
  }

  markAllRead() {
    const ids = this.unreadNotifications.map(n => n.id);
    if (!ids.length) return;

    this.loading = true;
    this.notificationsService.markAllRead(ids).subscribe({
      next: () => {
        this.loading = false;
        this.notifications = this.notifications.map(n => ({ ...n, read: true }));
        this.message = 'All notifications marked as read';
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to mark notifications as read';
      }
    });
  }

  savePreferences() {
    this.loading = true;
    this.notificationsService.updatePreferences(this.preferences).subscribe({
      next: res => {
        this.loading = false;
        this.preferences = res;
        this.message = 'Preferences updated';
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to update preferences';
      }
    });
  }
}

