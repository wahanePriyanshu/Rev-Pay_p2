import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WalletService } from '../../core/services/wallet.service';
import { TransactionService, TransactionDto } from '../../core/services/transaction.service';

interface UiTransaction {
  id: string;
  date: string;
  description: string;
  amount: number;
  type: 'credit' | 'debit';
  createdAt: string;
}

interface UserRecipient {
  id: number;
  name: string;
  avatarColor: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  userName = 'User';
  userEmail = 'Logged in user';

  walletBalance = 0;
  walletCurrency = 'USD';

  transactions: UiTransaction[] = [];
  totalReceivedLast30 = 0;
  totalSentLast30 = 0;

  favoriteUsers: UserRecipient[] = [
    { id: 1, name: 'Alice', avatarColor: '#4e73df' },
    { id: 2, name: 'Bob', avatarColor: '#1cc88a' },
    { id: 3, name: 'Charlie', avatarColor: '#36b9cc' },
    { id: 4, name: 'Diana', avatarColor: '#f6c23e' }
  ];

  constructor(
    private walletService: WalletService,
    private transactionService: TransactionService
  ) {}

  ngOnInit(): void {
    this.loadUserFromToken();
    this.loadWalletBalance();
    this.loadTransactions();
  }

  private loadUserFromToken(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      return;
    }

    try {
      const payloadPart = token.split('.')[1];
      const decoded = JSON.parse(atob(payloadPart));
      // Backend JWT example had "sub": "rahul@test.com"
      if (decoded.sub) {
        this.userName = decoded.sub;
        this.userEmail = decoded.sub;
      }
    } catch {
      // ignore decode errors, keep defaults
    }
  }

  private loadWalletBalance(): void {
    this.walletService.getBalance().subscribe({
      next: res => {
        this.walletBalance = res.balance;
      },
      error: () => {
        // keep default 0 on error
      }
    });
  }

  private loadTransactions(): void {
    this.transactionService.getMyTransactions().subscribe({
      next: res => {
        this.transactions = res.map((tx: TransactionDto) => ({
          id: `#TXN-${tx.id}`,
          date: new Date(tx.createdAt).toLocaleString(),
          description: this.buildDescription(tx),
          amount: tx.type === 'SEND' ? -tx.amount : tx.amount,
          type: tx.type === 'SEND' ? 'debit' : 'credit',
          createdAt: tx.createdAt
        }));
        this.computeTotals();
      },
      error: () => {
        // leave list empty on error
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

  get formattedBalance(): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: this.walletCurrency
    }).format(this.walletBalance);
  }

  get formattedTotalReceived(): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: this.walletCurrency
    }).format(this.totalReceivedLast30);
  }

  get formattedTotalSent(): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: this.walletCurrency
    }).format(this.totalSentLast30);
  }

  private computeTotals(): void {
    const now = new Date();
    const thirtyDaysAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);

    let received = 0;
    let sent = 0;

    for (const tx of this.transactions) {
      const txDate = new Date(tx.createdAt);
      if (txDate < thirtyDaysAgo) {
        continue;
      }

      if (tx.amount > 0) {
        received += tx.amount;
      } else if (tx.amount < 0) {
        sent += -tx.amount;
      }
    }

    this.totalReceivedLast30 = received;
    this.totalSentLast30 = sent;
  }

  getInitials(name: string): string {
    return name
      .split(' ')
      .map(part => part[0])
      .join('')
      .toUpperCase();
  }

  onSendMoney(user: UserRecipient) {
    // Placeholder: integrate with your send-money flow or navigation later
    console.log('Send money to', user.name);
  }
}
