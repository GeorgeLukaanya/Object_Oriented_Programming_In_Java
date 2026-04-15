//It contains all the business rules and orchestrates operations. It uses the repository to find accounts and applies
// logic to them.
// Notice it knows nothing about how data is displayed or stored — it only knows about business operations.
package bank.service;

import bank.model.BankAccount;
import bank.model.CheckingAccount;
import bank.model.SavingsAccount;
import bank.observer.LowBalanceAlert;
import bank.observer.TransactionLogger;
import bank.repository.AccountRepository;
import bank.repository.AccountRepositoryInterface;
import bank.strategy.AccountFactory;

public class BankService {
    private AccountRepositoryInterface repository;

    public BankService(AccountRepositoryInterface repository){
        this.repository = repository;
    }

    public CheckingAccount openCheckingAccount(String accountNumber, String owner, double initialBalance){
        if(repository.exists(accountNumber)){
            System.out.println("Account already exists: " + accountNumber);
            return null;
        }
        CheckingAccount account = AccountFactory.createCheckingAccount(accountNumber, owner, initialBalance);
        account.addObserver(new LowBalanceAlert(200.0));
        account.addObserver(new TransactionLogger());
        repository.save(account);
        return account;
    }

    public SavingsAccount openSavingsAccount(String accountNumber, String owner, double initialBalance){
        if(repository.exists(accountNumber)){
            System.out.println("Account already exists: " + accountNumber);
            return  null;
        }
        SavingsAccount account = AccountFactory.createSavingsAccount(accountNumber, owner, initialBalance);
        account.addObserver(new LowBalanceAlert(200.0));
        account.addObserver(new TransactionLogger());
        repository.save(account);
        return  account;
    }

    public void deposit(String accountNumber, double amount){
        BankAccount account = repository.findByAccountNumber(accountNumber);
        if(account == null){
            System.out.println("Account not found: " + accountNumber);
            return;
        }
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount){
        BankAccount account = repository.findByAccountNumber(accountNumber);
        if(account == null){
            System.out.println("Account not found: " + accountNumber);
            return;
        }
        account.withdraw(amount);
    }

    public void transfer(String fromAccountNumber, String  toAccountNumber, double amount){
        BankAccount from = repository.findByAccountNumber(fromAccountNumber);
        BankAccount to = repository.findByAccountNumber(toAccountNumber);

        if (from == null || to == null) {
            System.out.println("One or both accounts not found");
            return;
        }

        if(from.getBalance() < amount){
            System.out.println("Insufficient funds for transfer");
            return;
        }
        from.withdraw(amount);
        to.deposit(amount);
        System.out.println("Transferred " + amount + " from " + fromAccountNumber + " to " + toAccountNumber);
    }

    public void applyMonthlyInterest(String accountNumber){
        BankAccount account = repository.findByAccountNumber(accountNumber);
        if(account instanceof  SavingsAccount){
            ((SavingsAccount) account).applyInterest();
        }else{
            System.out.println("Interest only applies to savings accounts.");
        }
    }

    public void chargeMonthlyFee(String accountNumber){
        BankAccount account = repository.findByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found: " + accountNumber);
            return;
        }
        account.chargeFee();
    }

    public void printStatement(String accountNumber){
        BankAccount account = repository.findByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found: " + accountNumber);
            return;
        }
        System.out.println(account);
    }

    public void printAllAccounts(){
        for (BankAccount account : repository.findAll()) {
            System.out.println(account);
        }
    }
}
