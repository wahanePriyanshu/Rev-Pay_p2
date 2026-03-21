import { Component, OnInit, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { WalletService } from '../../core/services/wallet.service';
import { TransactionService, TransactionDto } from '../../core/services/transaction.service';
import { TransferService } from '../../core/services/transfer.service';
import { ToastService } from '../../core/services/toast.service';
import { UserRecipient, UserService } from '../../core/services/user.service';

interface UiTransaction {
  id: string;
  date: string;
  description: string;
  amount: number;
  type: 'credit' | 'debit';
  createdAt: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  userName = 'User';
  userEmail = 'Logged in user';
  walletCurrency = 'INR';

  /* =========================
     SIGNAL STATE
  ========================== */

  private walletBalanceSignal = signal<number>(0);
  walletBalance = computed(() => this.walletBalanceSignal());

  private transactionsSignal = signal<UiTransaction[]>([]);
  transactions = computed(() => this.transactionsSignal());
  recentTransactions = computed(() => this.transactionsSignal().slice(0, 5));

  private totalReceivedSignal = signal<number>(0);
  totalReceivedLast30 = computed(() => this.totalReceivedSignal());

  private totalSentSignal = signal<number>(0);
  totalSentLast30 = computed(() => this.totalSentSignal());

  private recipientsSignal = signal<UserRecipient[]>([]);
  recipients = computed(() => this.recipientsSignal());

  recipientSearch = '';
  recipientsLoading = false;
  walletLoading = false;
  transactionsLoading = false;
  isSubmittingTransfer = false;
  transferError = '';

  transferForm = {
    recipientId: null as number | null,
    amount: null as number | null,
    pin: '',
    note: ''
  };

  constructor(
    private walletService: WalletService,
    private transactionService: TransactionService,
    private transferService: TransferService,
    private userService: UserService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.loadUserFromToken();
    this.loadWalletBalance();
    this.loadTransactions();
    this.loadRecipients();
  }

  /* =========================
     USER FROM TOKEN
  ========================== */

  private loadUserFromToken(): void {
    const token = localStorage.getItem('token');
    if (!token) return;

    try {
      const payloadPart = token.split('.')[1];
      const decoded = JSON.parse(atob(payloadPart));

      if (decoded.sub) {
        this.userName = decoded.sub;
        this.userEmail = decoded.sub;
      }
    } catch {
      // ignore decode errors
    }
  }

  /* =========================
     WALLET
  ========================== */

  private loadWalletBalance(): void {
    this.walletLoading = true;
    this.walletService.getBalance().subscribe({
      next: res => {
        this.walletBalanceSignal.set(res.balance);
        this.walletLoading = false;
      },
      error: () => {
        this.walletLoading = false;
        this.toast.show('Unable to load wallet balance right now.', 'error');
      }
    });
  }

  /* =========================
     TRANSACTIONS
  ========================== */

  private loadTransactions(): void {
    this.transactionsLoading = true;
    this.transactionService.getMyTransactions().subscribe({
      next: res => {
        const mapped: UiTransaction[] = res.map((tx: TransactionDto) => ({
          id: `#TXN-${tx.id}`,
          date: new Date(tx.createdAt).toLocaleString(),
          description: this.buildDescription(tx),
          amount: tx.type === 'SEND' ? -tx.amount : tx.amount,
          type: tx.type === 'SEND' ? 'debit' : 'credit',
          createdAt: tx.createdAt
        }));

        this.transactionsSignal.set(mapped);
        this.computeTotals();
        this.transactionsLoading = false;
      },
      error: () => {
        this.transactionsLoading = false;
        this.toast.show('Unable to load recent transactions.', 'error');
      }
    });
  }

  private loadRecipients(): void {
    this.recipientsLoading = true;

    this.userService.getRecipients().subscribe({
      next: users => {
        this.recipientsSignal.set(users);
        this.recipientsLoading = false;
      },
      error: () => {
        this.recipientsLoading = false;
        this.toast.show('Unable to load available recipients.', 'error');
      }
    });
  }

  private buildDescription(tx: TransactionDto): string {
    if (tx.type === 'SEND') {
      return `Money sent to ${tx.to}`;
    }
    if (tx.type === 'RECEIVE') {
      return `Money received from ${tx.from}`;
    }
    return tx.note || 'Transaction';
  }

  /* =========================
     TOTAL CALCULATION
  ========================== */

  private computeTotals(): void {

    const now = new Date();
    const thirtyDaysAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);

    let received = 0;
    let sent = 0;

    for (const tx of this.transactionsSignal()) {

      const txDate = new Date(tx.createdAt);
      if (txDate < thirtyDaysAgo) continue;

      if (tx.amount > 0) {
        received += tx.amount;
      } else {
        sent += -tx.amount;
      }
    }

    this.totalReceivedSignal.set(received);
    this.totalSentSignal.set(sent);
  }

  /* =========================
     FORMATTERS
  ========================== */

  get formattedBalance(): string {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: this.walletCurrency
    }).format(this.walletBalance());
  }

  get formattedTotalReceived(): string {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: this.walletCurrency
    }).format(this.totalReceivedLast30());
  }

  get formattedTotalSent(): string {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: this.walletCurrency
    }).format(this.totalSentLast30());
  }

  get selectedRecipient(): UserRecipient | undefined {
    return this.recipients().find(user => user.id === this.transferForm.recipientId);
  }

  get filteredRecipients(): UserRecipient[] {
    const query = this.recipientSearch.trim().toLowerCase();

    if (!query) {
      return this.recipients();
    }

    return this.recipients().filter(user =>
      user.name.toLowerCase().includes(query) || user.email.toLowerCase().includes(query)
    );
  }

  getInitials(name: string): string {
    return name
      .split(' ')
      .map(part => part[0])
      .join('')
      .toUpperCase();
  }

  selectRecipient(user: UserRecipient): void {
    this.transferForm.recipientId = user.id;
    this.transferError = '';
  }

  submitTransfer(): void {
    const recipient = this.selectedRecipient;
    const amount = Number(this.transferForm.amount);
    const pin = this.transferForm.pin.trim();
    const note = this.transferForm.note.trim();

    if (!recipient) {
      this.transferError = 'Choose a recipient before sending money.';
      return;
    }

    if (!Number.isFinite(amount) || amount <= 0) {
      this.transferError = 'Enter a valid amount greater than zero.';
      return;
    }

    if (!pin) {
      this.transferError = 'Enter your transaction PIN.';
      return;
    }

    this.isSubmittingTransfer = true;
    this.transferError = '';

    this.transferService.send({
      to: recipient.email,
      amount,
      note: note || 'Dashboard transfer',
      pin
    }).subscribe({
      next: () => {
        this.isSubmittingTransfer = false;
        this.transferForm.amount = null;
        this.transferForm.pin = '';
        this.transferForm.note = '';
        this.loadWalletBalance();
        this.loadTransactions();
        this.toast.show(`Money sent to ${recipient.name} successfully.`, 'success');
      },
      error: err => {
        this.isSubmittingTransfer = false;
        this.transferError = err?.error?.message || 'Transfer failed. Please try again.';
        this.toast.show(this.transferError, 'error');
      }
    });
  }
}
