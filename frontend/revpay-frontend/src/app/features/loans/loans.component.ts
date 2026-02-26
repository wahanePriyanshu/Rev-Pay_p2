import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoanService, LoanDto } from '../../core/services/loan.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './loans.component.html',
  styleUrls: ['./loans.component.scss']
})
export class LoansComponent implements OnInit {
  loans: LoanDto[] = [];
  loading = false;
  applying = false;
  loanForm!: FormGroup;

  constructor(
    private loanService: LoanService,
    private fb: FormBuilder,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadLoans();
  }

  private buildForm() {
    this.loanForm = this.fb.group({
      amount: [0, [Validators.required, Validators.min(1)]],
      purpose: ['', Validators.required],
      tenureMonths: [12, [Validators.required, Validators.min(1)]]
    });
  }

  private loadLoans() {
    this.loading = true;
    this.loanService.getLoans().subscribe({
      next: res => {
        this.loans = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        // may be 403 for non-business users
      }
    });
  }

  submitLoan() {
    if (this.loanForm.invalid) return;
    this.applying = true;

    this.loanService.createLoan(this.loanForm.value).subscribe({
      next: () => {
        this.applying = false;
        this.toast.show('Loan application submitted', 'success');
        this.loanForm.reset({ amount: 0, purpose: '', tenureMonths: 12 });
        this.loadLoans();
      },
      error: () => {
        this.applying = false;
        this.toast.show('Failed to submit loan application', 'error');
      }
    });
  }
}

