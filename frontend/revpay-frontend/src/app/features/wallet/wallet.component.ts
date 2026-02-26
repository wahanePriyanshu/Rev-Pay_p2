import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { WalletService } from '../../core/services/wallet.service';
import { TransferService } from '../../core/services/transfer.service';
import { MoneyRequestService } from '../../core/services/money-request.service';

type WalletView =
  | 'overview'
  | 'send'
  | 'add'
  | 'withdraw'
  | 'request'
  | 'incoming'
  | 'outgoing';

@Component({
  selector: 'app-wallet',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './wallet.component.html',
  styleUrls: ['./wallet.component.scss']
})
export class WalletComponent implements OnInit {
  balance = 0;
  currency = 'USD';

  activeView: WalletView = 'overview';

  sendForm!: FormGroup;
  addForm!: FormGroup;
  withdrawForm!: FormGroup;
  requestForm!: FormGroup;

  incomingRequests: any[] = [];
  outgoingRequests: any[] = [];

  loading = false;
  message: string | null = null;
  error: string | null = null;

  constructor(
    private walletService: WalletService,
    private transferService: TransferService,
    private moneyRequestService: MoneyRequestService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadBalance();
    this.buildForms();
  }

  private buildForms(): void {
    this.sendForm = this.fb.group({
      to: ['', [Validators.required]],
      amount: [0, [Validators.required, Validators.min(1)]],
      note: [''],
      pin: ['', [Validators.required, Validators.minLength(4)]]
    });

    this.addForm = this.fb.group({
      amount: [0, [Validators.required, Validators.min(1)]],
      pin: ['', [Validators.required, Validators.minLength(4)]]
    });

    this.withdrawForm = this.fb.group({
      amount: [0, [Validators.required, Validators.min(1)]],
      pin: ['', [Validators.required, Validators.minLength(4)]]
    });

    this.requestForm = this.fb.group({
      to: ['', [Validators.required]],
      amount: [0, [Validators.required, Validators.min(1)]],
      purpose: ['', [Validators.required]]
    });
  }

  setView(view: WalletView) {
    this.activeView = view;
    this.message = null;
    this.error = null;

    if (view === 'incoming') {
      this.loadIncomingRequests();
    }
    if (view === 'outgoing') {
      this.loadOutgoingRequests();
    }
  }

  private loadBalance(): void {
    this.walletService.getBalance().subscribe({
      next: res => {
        this.balance = res.balance;
      },
      error: () => {
        this.error = 'Failed to load wallet balance';
      }
    });
  }

  submitSend() {
    if (this.sendForm.invalid) return;
    this.loading = true;
    this.message = null;
    this.error = null;

    this.transferService.send(this.sendForm.value).subscribe({
      next: res => {
        this.loading = false;
        this.message = res.message || 'Money sent successfully';
        this.loadBalance();
        this.sendForm.reset();
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to send money';
      }
    });
  }

  submitAdd() {
    if (this.addForm.invalid) return;
    this.loading = true;
    this.message = null;
    this.error = null;

    const { amount, pin } = this.addForm.value;

    this.walletService.addMoney(amount, pin).subscribe({
      next: () => {
        this.loading = false;
        this.message = 'Money added successfully';
        this.loadBalance();
        this.addForm.reset();
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to add money';
      }
    });
  }

  submitWithdraw() {
    if (this.withdrawForm.invalid) return;
    this.loading = true;
    this.message = null;
    this.error = null;

    const { amount, pin } = this.withdrawForm.value;

    this.walletService.withdraw(amount, pin).subscribe({
      next: () => {
        this.loading = false;
        this.message = 'Withdrawal successful';
        this.loadBalance();
        this.withdrawForm.reset();
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to withdraw';
      }
    });
  }

  submitRequest() {
    if (this.requestForm.invalid) return;
    this.loading = true;
    this.message = null;
    this.error = null;

    this.moneyRequestService.createRequest(this.requestForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.message = 'Money request sent';
        this.requestForm.reset();
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to request money';
      }
    });
  }

  private loadIncomingRequests() {
    this.moneyRequestService.getIncoming().subscribe({
      next: res => {
        this.incomingRequests = res;
      },
      error: () => {
        this.error = 'Failed to load incoming requests';
      }
    });
  }

  private loadOutgoingRequests() {
    this.moneyRequestService.getOutgoing().subscribe({
      next: res => {
        this.outgoingRequests = res;
      },
      error: () => {
        this.error = 'Failed to load outgoing requests';
      }
    });
  }
}

