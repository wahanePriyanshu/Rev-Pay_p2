import { Component,EventEmitter,Output } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss']
})
export class NavbarComponent {
  constructor(
    private router: Router,
    public auth: AuthService
  ) {}

  

  logout() {
    this.auth.logout();
    this.router.navigate(['/auth/login']);
    this.router.navigate(['/login']);
  }

@Output() toggleSidebarEvent = new EventEmitter<void>();

  toggleSidebar() {
    this.toggleSidebarEvent.emit();

  }




  



}