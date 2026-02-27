import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { authGuard } from './core/guards/auth.guard'
import { guestGuard } from './core/guards/guest.guard';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { WalletComponent } from './features/wallet/wallet.component';
import { TransactionsComponent } from './features/transactions/transactions.component';
import { NotificationsComponent } from './features/notifications/notifications.component';
import { ProfileComponent } from './features/profile/profile.component';
import { BusinessComponent } from './features/business/business.component';
import { InvoicesComponent } from './features/invoices/invoices.component';
import { LoansComponent } from './features/loans/loans.component';

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
        component: DashboardComponent,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'wallet',
        component: WalletComponent,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'transactions',
        component: TransactionsComponent,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'notifications',
        component: NotificationsComponent,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'profile',
        component: ProfileComponent,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'business',
        component: BusinessComponent,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'invoices',
        component: InvoicesComponent,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'loans',
        component: LoansComponent,
        runGuardsAndResolvers: 'always'
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