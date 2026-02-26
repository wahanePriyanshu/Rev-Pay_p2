import { Component, Input, EventEmitter, Output } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';


@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.scss']
})
export class SidebarComponent {

  @Input() isOpen: boolean = true;
  @Output() toggleSidebarEvent = new EventEmitter<void>();

  showProfileMenu = false;

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  toggleProfileMenu() {
    this.showProfileMenu = !this.showProfileMenu;
  }

  goToProfile() {
    this.showProfileMenu = false;
    this.router.navigate(['/profile']);
  }

  logout() {
    this.showProfileMenu = false;
    this.auth.logout();
    this.router.navigate(['/auth/login']);
  }

  onToggleSidebar() {
    this.toggleSidebarEvent.emit();
  }
}