import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { authGuard } from './core/guards/auth.guard'
import { guestGuard } from './core/guards/guest.guard';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { WalletComponent } from './features/wallet/wallet.component';
import { TransactionsComponent } from './features/transactions/transactions.component';
import { NotificationsComponent } from './features/notifications/notifications.component';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dashboard'
      },
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'wallet',
        component: WalletComponent
      },
      {
        path: 'transactions',
        component: TransactionsComponent
      },
      {
        path: 'notifications',
        component: NotificationsComponent
      }
      // later: user, business, admin dashboards here
    ]
  },

  {
    path: 'auth',
    component: AuthLayoutComponent,
    canActivate: [guestGuard],
    children: [
      {
        path: 'login',
        loadComponent: () =>
          import('./features/auth/login/login.component').then(m => m.LoginComponent)
      },
      {
        path: 'register',
        loadComponent: () =>
          import('./features/auth/register/register.component').then(m => m.RegisterComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'auth/login'
  }
];