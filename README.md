
# RESTful Wallet Management System with Fraud Detection and Analytics  

## Project Overview  

This project is a **Java-based RESTful API** designed to manage wallets, detect fraudulent transactions, and provide analytics. The system is lightweight, fully in-memory (no database), and self-contained, allowing users to interact with it through **command-line tools like `curl`**. 

---

## Key Features  

### Wallet Management  
- **Create Wallet**: Generate a wallet with a unique ID.  
- **View Wallet Details**: Retrieve wallet balance and transaction history.  
- **Deposit Funds**: Add money securely to wallets.  
- **Withdraw Funds**: Withdraw money, ensuring sufficient balance.  
- **Transaction History**: Access detailed logs for any wallet.  

### Fraud Detection  
- **Flagged Transactions**: Identify potential fraud based on customizable rules.  

### Analytics  
- **Top Spenders**: Identify wallets with the highest transaction totals.  

### Security and Access Control  
- **User vs Admin Access**:  
  - Users: Perform wallet operations (create, deposit, withdraw, and view balance).  
  - Admins: Access advanced functionalities like fraud detection, top spenders analytics and all wallets using an **admin key**.  

---

## Technologies Used  
- **Java**: Core programming language for implementation.  
- **RESTful Design**: Adheres to RESTful principles for clean API design.  
- **Unit Testing**: Built and tested with **JUnit** and **Mockito**.  
- **Java Collections and Concurrency**: In-memory storage implemented with thread-safe structures like `ConcurrentHashMap`.  

---

## Project Structure  

The project follows a modular structure for scalability and clarity:  

```
├───src
│   └───main
│       └───java
│           └───com
│               └───walletapi
│                   ├───controllers
│                   │   └───handlers
│                   │       ├───analytics
│                   │       ├───fraud
│                   │       └───wallet
│                   ├───exceptions
│                   ├───models
│                   ├───services
│                   └───utils
│                       └───riskcalculator
│                           └───analyzers
└───test
    └───main
        └───java
            └───com
                └───walletapi
                    ├───controllers
                    │   └───handlers
                    │       ├───analytics
                    │       ├───fraud
                    │       └───wallet
                    ├───services
                    └───utils
                        └───riskcalculator
                            └───analyzers
```

---

## API Endpoints  

### Wallet Management  
- **POST /wallets/create**  
  Create a new wallet.  

  Example:  
  ```bash
  curl -X POST http://localhost:8080/wallets/create
  ```  

- **GET /wallets/balance**  
  Get wallet balance by Id.  

  Example:  
  ```bash
  curl -X GET http://localhost:8080/wallets/balance?walletId=1
  ```  

- **POST /wallets/deposit**  
  Deposit money to a wallet by Id.  

  Example:  
  ```bash
  curl -X POST http://localhost:8080/wallets/deposit -H "Content-Type: text/plain"  -d "1,100.00"
  ```  

- **POST /wallets/withdraw**  
  Withdraw money from a wallet by Id.  

  Example:  
  ```bash
  curl -X POST http://localhost:8080/wallets/withdraw  -H "Content-Type: text/plain"  -d "1,100.00"
  ```
   
- **GET /all-wallets**  
  Get all wallets information (admin only).  

  Example:  
  ```bash
  curl -X GET http://localhost:8080/all-wallets -H "Admin-Key: 1234"//
  ```  

### Fraud Detection 
- **GET /fraud/transactions**  
  Retrieve the fraud risk score and flagged transactions for a wallet (admin only).  

  Example:  
  ```bash
  curl -X GET http://localhost:8080/fraud/transactions -H "Admin-Key: 1234"//only having the admin key we have access to this functionality
  ```  

---

### Analytics  
- **GET /analytics/top-spenders**  
  Retrieve wallets with the highest total transaction amounts (admin only).  

  Example:  
  ```bash
  -X GET http://localhost:8080/analytics/top-spenders?limit=2
  ```  

---

## Testing  

### Manual Testing with `curl`  
1. **Download the Project Files**:  
   Clone or download the repository from GitHub:  
   ```bash
   git clone [https://github.com/your-username/restful-wallet-system.git](https://github.com/manchevatsveti/WalletAPI)
   cd restful-wallet-system
   ```  

2. **Open the Project in IntelliJ IDEA**:  
   - Open IntelliJ IDEA.  
   - Click **File > Open** and select the project directory.  
   - Ensure the **JDK 11+** is configured in IntelliJ under **File > Project Structure > SDKs**.  

3. **Run the Main Application**:  
   - Navigate to `src/main/java/com/walletapi/Main.java` in IntelliJ.  
   - Right-click on `Main.java` and select **Run 'Main'**.  

4. **Test with `curl` Commands**:  
   Use the examples provided above to test API functionalities directly from the command line. For example:  
   ```bash
   curl -X POST http://localhost:8080/wallets/create
   curl -X POST http://localhost:8080/wallets/deposit -H "Content-Type: text/plain"  -d "0,100.00"
   ```  

---

### Automated Testing  
1. **Run Tests in IntelliJ IDEA**:  
   - Navigate to the `test/main/java/com/walletapi` directory in IntelliJ.
   - Configure Junit 5 and Mockito latest versions locally
   - Right-click on the test folder or an individual test class and select **Run 'All Tests'** or **Run 'YourTestClass'**.  

2. **Run Tests Using Maven**:  
   If Maven is configured for the project, run unit tests with the following command:  
   ```bash
   mvn test
   ```  
---

### Notes  
- The code is written in **IntelliJ IDEA**, and the IDE is recommended for seamless setup and testing.  
- Both manual testing (via `curl` commands) and automated testing (via JUnit and Mockito) ensure the functionality and reliability of the API.  

---

## Future Enhancements  
- **User Verification**: Add verification (e.g., PIN or OTP) for withdrawal requests.  
- **Enhanced Analytics**: Introduce more complex transaction behavior analysis.  
- **Database Integration**: Replace in-memory storage with a persistent database.  

---

Enjoy using the RESTful Wallet Management System! 🚀  
