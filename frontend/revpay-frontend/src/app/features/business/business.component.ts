import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BusinessService, BusinessProfile } from '../../core/services/business.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-business',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './business.component.html',
  styleUrls: ['./business.component.scss']
})
export class BusinessComponent implements OnInit {
  business: BusinessProfile | null = null;
  loading = false;
  registering = false;
  registerForm!: FormGroup;

  constructor(
    private businessService: BusinessService,
    private fb: FormBuilder,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadBusiness();
  }

  private buildForm() {
    this.registerForm = this.fb.group({
      businessName: ['', Validators.required],
      businessType: ['', Validators.required],
      taxId: ['', Validators.required],
      businessAddress: ['', Validators.required]
    });
  }

  private loadBusiness() {
    this.loading = true;
    this.businessService.getMyBusiness().subscribe({
      next: res => {
        this.business = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        // if 404/403, just show registration form
      }
    });
  }

  submitRegister() {
    if (this.registerForm.invalid) return;
    this.registering = true;

    this.businessService.registerBusiness(this.registerForm.value).subscribe({
      next: () => {
        this.registering = false;
        this.toast.show('Business registration submitted', 'success');
        this.loadBusiness();
      },
      error: () => {
        this.registering = false;
        this.toast.show('Failed to register business', 'error');
      }
    });
  }
}

