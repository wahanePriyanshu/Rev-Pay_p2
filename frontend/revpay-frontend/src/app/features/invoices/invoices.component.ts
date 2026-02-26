import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InvoiceService, InvoiceDto } from '../../core/services/invoice.service';
import { CustomerService, CustomerDto } from '../../core/services/customer.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-invoices',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.scss']
})
export class InvoicesComponent implements OnInit {
  invoices: InvoiceDto[] = [];
  customers: CustomerDto[] = [];

  loading = false;
  creating = false;

  invoiceForm!: FormGroup;

  constructor(
    private invoiceService: InvoiceService,
    private customerService: CustomerService,
    private fb: FormBuilder,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadInvoices();
    this.loadCustomers();
  }

  private buildForm() {
    this.invoiceForm = this.fb.group({
      customerId: [null, Validators.required],
      dueDate: ['', Validators.required],
      description: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      unitPrice: [0, [Validators.required, Validators.min(0)]],
      tax: [0, [Validators.required, Validators.min(0)]]
    });
  }

  private loadInvoices() {
    this.loading = true;
    this.invoiceService.getInvoices().subscribe({
      next: res => {
        this.invoices = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        // could be 403 if user is not business; ignore for now
      }
    });
  }

  private loadCustomers() {
    this.customerService.getCustomers().subscribe({
      next: res => {
        this.customers = res;
      },
      error: () => {
        // may be 403 if customers are restricted
      }
    });
  }

  submitInvoice() {
    if (this.invoiceForm.invalid) return;

    const { customerId, dueDate, description, quantity, unitPrice, tax } = this.invoiceForm.value;

    this.creating = true;
    this.invoiceService.createInvoice({
      customerId,
      dueDate,
      items: [{ description, quantity, unitPrice, tax }]
    }).subscribe({
      next: () => {
        this.creating = false;
        this.toast.show('Invoice created', 'success');
        this.invoiceForm.reset({ quantity: 1, unitPrice: 0, tax: 0 });
        this.loadInvoices();
      },
      error: () => {
        this.creating = false;
        this.toast.show('Failed to create invoice', 'error');
      }
    });
  }

  sendInvoice(id: number) {
    this.invoiceService.sendInvoice(id).subscribe({
      next: () => {
        this.toast.show('Invoice sent', 'success');
        this.loadInvoices();
      },
      error: () => {
        this.toast.show('Failed to send invoice', 'error');
      }
    });
  }

  markPaid(id: number) {
    this.invoiceService.payInvoice(id).subscribe({
      next: () => {
        this.toast.show('Invoice marked as paid', 'success');
        this.loadInvoices();
      },
      error: () => {
        this.toast.show('Failed to mark as paid', 'error');
      }
    });
  }

  cancelInvoice(id: number) {
    this.invoiceService.cancelInvoice(id).subscribe({
      next: () => {
        this.toast.show('Invoice cancelled', 'success');
        this.loadInvoices();
      },
      error: () => {
        this.toast.show('Failed to cancel invoice', 'error');
      }
    });
  }
}

