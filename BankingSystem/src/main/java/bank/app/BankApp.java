package bank.app;

import bank.repository.AccountRepository;
import bank.repository.AccountRepositoryInterface;
import bank.repository.InMemoryAccountRepository;
import bank.service.BankService;

public class BankApp {
    public  static void main(String[] args) {
        //Wire the layers up
        AccountRepositoryInterface repository = new InMemoryAccountRepository();
        BankService service = new BankService(repository);

        //Open accounts
        System.out.println("===Opening Accounts===");
        service.openCheckingAccount("ACC001", "Mariam", 1000.0);
        service.openSavingsAccount("ACC002", "George", 2000.0);
        service.openSavingsAccount("ACC003", "Arthur", 5000.0);
        service.openCheckingAccount("ACC001", "Mariam", 1000.0); // duplicate - should fail

        System.out.println("\n=== Initial State ===");
        service.printAllAccounts();

        // Deposits and withdrawals
        System.out.println("\n=== Transactions ===");
        service.deposit("ACC001", 500.0);
        service.withdraw("ACC001", 1200.0);
        service.chargeMonthlyFee("ACC001");

        service.deposit("ACC002", 300.0);
        service.withdraw("ACC002", 2100.0);   // triggers low balance alert
        service.applyMonthlyInterest("ACC002");

        // Transfer between accounts
        System.out.println("\n=== Transfer ===");
        service.transfer("ACC003", "ACC001", 300.0);

        // Try interest on checking - should fail gracefully
        System.out.println("\n=== Invalid Operations ===");
        service.applyMonthlyInterest("ACC001");
        service.deposit("ACC999", 100.0);      // non-existent account

        System.out.println("\n=== Final State ===");
        service.printAllAccounts();
    }
}

