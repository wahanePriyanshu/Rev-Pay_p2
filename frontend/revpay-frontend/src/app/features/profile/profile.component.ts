import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfileService, UserProfile } from '../../core/services/profile.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profile: UserProfile | null = null;
  passwordForm!: FormGroup;
  pinForm!: FormGroup;
  setPinForm!: FormGroup;

  loadingProfile = false;
  savingPassword = false;
  savingPin = false;
  savingSetPin = false;

  constructor(
    private profileService: ProfileService,
    private fb: FormBuilder,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.buildForms();
  }

  private loadProfile() {
    this.loadingProfile = true;
    this.profileService.getProfile().subscribe({
      next: res => {
        this.profile = res;
        this.loadingProfile = false;
      },
      error: () => {
        this.loadingProfile = false;
        this.toast.show('Failed to load profile', 'error');
      }
    });
  }

  private buildForms() {
    this.passwordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.pinForm = this.fb.group({
      oldPin: ['', [Validators.required, Validators.minLength(4)]],
      newPin: ['', [Validators.required, Validators.minLength(4)]]
    });

    this.setPinForm = this.fb.group({
      pin: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  submitPassword() {
    if (this.passwordForm.invalid) return;
    this.savingPassword = true;

    this.profileService.changePassword(this.passwordForm.value).subscribe({
      next: (msg) => {
        this.savingPassword = false;
        this.toast.show(msg || 'Password changed successfully', 'success');
        this.passwordForm.reset();
      },
      error: (err) => {
        this.savingPassword = false;
        this.toast.show(err?.error || 'Failed to change password', 'error');
      }
    });
  }

  submitChangePin() {
    if (this.pinForm.invalid) return;
    this.savingPin = true;

    this.profileService.changePin(this.pinForm.value).subscribe({
      next: (msg) => {
        this.savingPin = false;
        this.toast.show(msg || 'Transaction PIN changed successfully', 'success');
        this.pinForm.reset();
      },
      error: (err) => {
        this.savingPin = false;
        this.toast.show(err?.error || 'Failed to change PIN', 'error');
      }
    });
  }

  submitSetPin() {
    if (this.setPinForm.invalid) return;
    this.savingSetPin = true;

    this.profileService.setPin(this.setPinForm.value).subscribe({
      next: (msg) => {
        this.savingSetPin = false;
        this.toast.show(msg || 'Transaction PIN set successfully', 'success');
        this.setPinForm.reset();
      },
      error: (err) => {
        this.savingSetPin = false;
        this.toast.show(err?.error || 'Failed to set PIN', 'error');
      }
    });
  }
}

