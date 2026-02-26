import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService, TransactionDto } from '../../core/services/transaction.service';

interface UiTransactionRow {
  id: number;
  type: 'SEND' | 'RECEIVE';
  status: string;
  from: string;
  to: string;
  amount: number;
  note: string;
  createdAt: string;
}

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent implements OnInit {
  allTransactions: UiTransactionRow[] = [];
  filteredTransactions: UiTransactionRow[] = [];
  searchTerm = '';

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadTransactions();
  }

  private loadTransactions() {
    this.transactionService.getMyTransactions().subscribe({
      next: res => {
        this.allTransactions = res.map((tx: TransactionDto) => ({
          id: tx.id,
          type: tx.type,
          status: 'COMPLETED',
          from: tx.from,
          to: tx.to,
          amount: tx.amount,
          note: tx.note,
          createdAt: new Date(tx.createdAt).toLocaleString()
        }));
        this.filteredTransactions = [...this.allTransactions];
      },
      error: () => {
        this.allTransactions = [];
        this.filteredTransactions = [];
      }
    });
  }

  applySearch() {
    const term = this.searchTerm.toLowerCase();
    this.filteredTransactions = this.allTransactions.filter(tx =>
      tx.from.toLowerCase().includes(term) ||
      tx.to.toLowerCase().includes(term) ||
      tx.note.toLowerCase().includes(term) ||
      tx.status.toLowerCase().includes(term)
    );
  }
}

