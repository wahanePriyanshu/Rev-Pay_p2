import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  regiForm!: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private toast: ToastService
  ) {
    this.regiForm = this.fb.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      role: ['PERSONAL', Validators.required] // PERSONAL or BUSINESS
    });
  }

  submit() {
    if (this.regiForm.invalid) return;

    const { password, confirmPassword, ...payload } = this.regiForm.value;

    if (password !== confirmPassword) {
      this.toast.show('Passwords do not match', 'error');
      return;
    }

    this.loading = true;

    this.auth.register({ ...payload, password }).subscribe({
      next: () => {
        this.loading = false;
        this.toast.show('Registration successful! Please login.', 'success');
        this.router.navigate(['/auth/login']);
      },
      error: (err) => {
        this.loading = false;
        this.toast.show(err?.error?.message || 'Registration failed', 'error');
      }
    });
  }
}