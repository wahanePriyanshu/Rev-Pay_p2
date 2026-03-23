# 💳 RevPay — Full-Stack Monolithic Digital Payment & Financial Management Web Application

RevPay is a full-stack monolithic financial web application that enables secure digital payments and money management for both Personal and Business users.

The platform allows users to send and request money, manage wallets and payment methods, track transactions, and receive real-time notifications through an intuitive web interface. Business users gain advanced capabilities, including invoice management, loan applications, payment acceptance, and business analytics.

The application implements robust security mechanisms, including JWT-based authentication, role-based access control (RBAC), encrypted passwords, transaction PIN verification, and protected REST APIs.

---


## 🌐 Live Application

🚀 The application is deployed and accessible online:

- Frontend: http://13.60.88.223:4201  
- Backend: http://13.60.88.223:8081  

---


# ✨ Key Features

---

## 👤 Personal Account User

### 🔐 Authentication & Security
- Register with full name, email, phone number, password, and security questions
- Log in using email or phone with password
- Encrypted password storage (BCrypt)
- JWT-based authentication
- Set/change transaction PIN
- Change password with current password verification

---

### 📊 Dashboard
- View wallet balance
- View recent transactions
- Quick action buttons (Send, Request, Add Money, Withdraw)

---

### 💸 Money Transfers
- Send money using:
  - Username
  - Email
  - Phone number
  - Account ID
- Add an optional note to transfers
- Real-time balance update

---

### 💰 Money Requests
- Request money with an amount and purpose
- View incoming requests (Pending / Accepted / Declined)
- Accept or decline requests
- View outgoing requests
- Cancel pending requests

---

### 💳 Payment Methods
- Add credit/debit cards
- Store:
  - Card number (secured)
  - Expiry date
  - CVV
  - Billing address
- View all cards
- Edit card details
- Delete card
- Set default payment method

---

### 📜 Transaction Management
- View complete transaction history
- Filter by:
  - Sent
  - Received
  - Added funds
  - Withdrawals
- Filter by:
  - Date range
  - Amount
  - Status
- Search by:
  - Recipient/Sender name
  - Transaction ID
- Export transactions to CSV/PDF

---

### 🏦 Wallet Operations
- Add money to wallet (simulated)
- Withdraw money to bank account (simulated)
- Low balance alerts

---

### 🔔 Notifications
- Receive notifications for:
  - Transactions
  - Money requests
  - Card changes
  - Low balance alerts
- View unread notification count
- Mark notifications as read
- Manage notification preferences
- View notification history

---

### 👤 Profile Management
- Update profile information
- Change password
- Set/update transaction PIN

---

## 🏢 Business Account User

Business accounts include **all personal account features** plus:

---

### 🏢 Business Registration
- Register with:
  - Business name
  - Business type
  - Tax ID
  - Business address
  - Contact information
- Submit verification documents (simulated approval)

---

### 💳 Business Payment Methods
- Add business credit/debit cards
- Add business bank accounts

---

### 💰 Payment Acceptance
- Generate payment requests for customers
- Process incoming payments via:
  - Email
  - Phone
  - Account ID

---

### 🧾 Invoice Management
- Create invoices with:
  - Itemized line items
  - Description
  - Quantity
  - Unit price
  - Tax
- Add customer details
- Set due dates & payment terms
- View invoice statuses:
  - Draft
  - Sent
  - Paid
  - Overdue
  - Cancelled
- Send invoice notifications
- Mark invoice as paid manually
- Track paid and unpaid invoices

---

### 🏦 Loan Management
- Apply for business loans
- Provide:
  - Loan amount
  - Purpose
  - Tenure
  - Financial details
- Upload supporting documents (simulated)
- Track loan status:
  - Pending
  - Approved
  - Rejected
- View:
  - Interest rate
  - EMI
  - Repayment schedule
- Make simulated repayments

---

### 📊 Business Analytics Dashboard
- View key business metrics
- Transaction summaries
- Revenue reports (Daily / Weekly / Monthly)
- Outstanding invoices summary
- Payment trends (charts)
- Top customers by transaction volume

---

# 🏗 Architecture (Monolithic)

Single application containing:

- Authentication & Authorization (RBAC)
- User Management (Personal + Business)
- Wallet & Transactions
- Money Requests
- Invoice Management
- Loan Management
- Notification System
- Business Analytics
- Security Layer (JWT + Spring Security)

## 🛠 Tech Stack

Backend: Java 17+ + Spring Boot (REST APIs), Spring Security, JWT Authentication, BCrypt, JPA/Hibernate  
Database: MySQL  
Frontend: Angular (Router, Interceptors, RxJS, Responsive UI)  
Build Tools: Maven (Backend), Angular CLI (Frontend), Git (Version Control)

# 📌 Standard Functional Scope

- Authentication & Authorization
- Security Implementation (JWT + RBAC + PIN verification)
- Notification System
- Transaction Management
- Invoice & Loan Processing
- Business Analytics
- Data Management
- User Interface & Experience

---
<img width="1916" height="964" alt="Image" src="https://github.com/user-attachments/assets/7ab34873-69be-4095-b1e8-2a58e561c102" />
<img width="1917" height="969" alt="Image" src="https://github.com/user-attachments/assets/2cdd94ae-99ac-4734-99f6-f2312ec40c3c" />
<img width="1917" height="970" alt="Image" src="https://github.com/user-attachments/assets/d8b61c25-57ae-4bde-a6f7-8d3906cdfc77" />
<img width="1913" height="967" alt="Image" src="https://github.com/user-attachments/assets/e3e5ff0a-0f27-4516-b3f6-f2460a83aab5" />
<img width="1918" height="971" alt="Image" src="https://github.com/user-attachments/assets/9485ae50-9aad-47e3-b1f7-d4dc8d6a588e" />
<img width="1919" height="971" alt="Image" src="https://github.com/user-attachments/assets/067d8f5e-ff17-4176-8e09-4a77a749ac82" />
<img width="1919" height="972" alt="Image" src="https://github.com/user-attachments/assets/7e93c731-fb7f-448d-b9bd-7c5a27ed01d5" />
<img width="1685" height="1240" alt="Image" src="https://github.com/user-attachments/assets/ce3d883a-1198-4d2e-98dd-953a448731fe" />



