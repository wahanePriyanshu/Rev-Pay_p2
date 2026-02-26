import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  // Signal that holds list of messages
  private _toasts = signal<string[]>([]);

  // Expose readonly signal
  toasts = this._toasts;

  show(message: string, duration: number = 3000) {
    // Add new toast
    this._toasts.update(list => [...list, message]);

    // Auto remove after duration
    setTimeout(() => {
      this._toasts.update(list => list.slice(1));
    }, duration);
  }

  clear() {
    this._toasts.set([]);
  }
}