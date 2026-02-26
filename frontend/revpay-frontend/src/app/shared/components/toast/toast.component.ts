import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 1055;">
      <div *ngIf="toastService.message()" 
           class="toast show"
           [ngClass]="{
             'text-bg-success': toastService.type() === 'success',
             'text-bg-danger': toastService.type() === 'error',
             'text-bg-info': toastService.type() === 'info'
           }">
        <div class="toast-body">
          {{ toastService.message() }}
        </div>
      </div>
    </div>
  `
})
export class ToastComponent {
  constructor(public toastService: ToastService) {}
}