# 🏥 IUH Pharmacity Management System

Real-time pharmacy management system with Java Swing, JDBC, SQL Server, and FlatLaf for efficient operations and modern user experience. 💊📊

## Introduction

IUH Pharmacity Management System is a comprehensive pharmacy management application built with Java Swing and a layered architecture to enable efficient pharmacy operations. Using JDBC for database connectivity, SQL Server for database management, and FlatLaf for modern UI, the system ensures smooth and professional management of sales, inventory, customers, and suppliers. It is developed using Java 21, Maven, and various modern technologies to provide a robust and scalable solution for pharmacy businesses.

## Prerequisites

Before setting up and running the application, ensure you have the following installed and configured:

- Java Development Kit (JDK) 21 or higher installed.
- Maven 3.8+ build tool installed.
- Microsoft SQL Server 2019+ database system set up and configured.
- SQL Server Management Studio (SSMS) for database management (recommended).
- Git for version control.

## Features

- Real-time Sales Processing: Point of Sale (POS) system for quick and efficient sales transactions.
- Inventory Management: Track products, batches, expiration dates, and stock levels with automatic alerts.
- Customer & Supplier Management: Comprehensive database for managing relationships and transactions.
- Employee Management: Staff registration, role assignment, and performance tracking.
- Financial Reports: Income/expense tracking, revenue analysis, and profit reports.
- Import/Return/Disposal Management: Handle purchase orders, product returns, and expired item disposal.
- Promotion Management: Create and manage discount campaigns and special offers.
- AI ChatBot Assistant: Intelligent assistant for quick queries and system guidance.
- Email Notifications: Automated email alerts for important events and password recovery.
- PDF & Excel Support: Generate professional reports and import/export data efficiently.
- Role-based Access Control: Separate interfaces and permissions for Managers and Staff.
- Modern UI: Clean, intuitive interface with FlatLaf Look & Feel.

## Getting Started

Follow these steps to set up and run the IUH Pharmacity Management System on your local machine.

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/IUH-Pharmacity-Management.git
cd IUH-Pharmacity-Management
```

### 2. Setup SQL Server Database

Open SQL Server Management Studio (SSMS) and execute the database script to create tables and initial data.

### 3. Configure Database Connection

Update `src/main/java/vn/edu/iuh/fit/iuhpharmacitymanagement/connectDB/ConnectDB.java`:

```java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=IUHPharmacityManagement;encrypt=false;trustServerCertificate=true;integratedSecurity=false";
private static final String USER = "YOUR_SQL_USERNAME";
private static final String PASSWORD = "YOUR_SQL_PASSWORD";
```

### 4. Configure Email (Optional - For sending promotions and notifications)

Update `src/main/resources/email.properties`:

```properties
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.auth=true
mail.smtp.starttls.enable=true

sender.email=YOUR_EMAIL@gmail.com
sender.password=YOUR_APP_PASSWORD
sender.name=IUH Pharmacity Store
```

To get Gmail App Password:
1. Enable 2-Factor Authentication on your Google Account
2. Visit https://myaccount.google.com/apppasswords
3. Generate App Password for "Mail"
4. Use the generated 16-character password in email.properties

### 5. Barcode Scanning Setup (Optional - For product scanning)

The system supports two methods for barcode scanning:

Method 1: USB Barcode Scanner (Recommended)
- Connect USB barcode scanner to your computer
- The scanner acts as a keyboard input device
- Simply scan the barcode, it will automatically input the code

Method 2: Software Barcode Scanner
- Use camera-based barcode scanning software
- Install barcode scanner application on your device
- Configure the software to send scanned codes to the application

### 6. Build the Project

```bash
mvn clean install
```

### 7. Run the Application

You can run the application using Maven:

```bash
mvn clean compile exec:java
```

Alternatively, you can run the JAR file:

```bash
mvn clean package
java -jar target/iuhpharmacitymanagement-1.0-SNAPSHOT.jar
```

Or use your IDE (NetBeans/IntelliJ IDEA/Eclipse):
- Main Class: `vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.MainApplication`

## Technologies Used

- Java 21: The primary programming language for the application.
- Maven: Build tool for managing dependencies and building the project.
- Java Swing: Framework for building desktop GUI applications.
- JDBC: Java Database Connectivity for database operations.
- FlatLaf: Modern Look & Feel for professional UI design.
- Microsoft SQL Server: Relational database management system.
- Apache POI: Library for reading and writing Excel files.
- iText: PDF generation library for reports and invoices.
- JavaMail: Email functionality for notifications and password recovery.
- ZXing: QR code generation for products and orders.

## Development Team

- 23633471 - Phạm Văn Trà (Team Leader)
- 23635991 - Tô Nguyễn An Thuyên
- 23633961 - Nguyễn Công Tuyến
- 23636821 - Đỗ Hoài Nhớ
- 23633251 - Phạm Minh Thịnh

Institution: Industrial University of Ho Chi Minh City (IUH)
Faculty: Information Technology
