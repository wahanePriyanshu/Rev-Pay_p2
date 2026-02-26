import { Component, Input, EventEmitter, Output, OnInit } from '@angular/core';
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
export class SidebarComponent implements OnInit {

  @Input() isOpen: boolean = true;
  @Output() toggleSidebarEvent = new EventEmitter<void>();

  showProfileMenu = false;
  role: string | null = null;

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.role = this.auth.getRole();
  }

  get isBusiness(): boolean {
    return this.role === 'ROLE_BUSINESS';
  }

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