package bank;
import bank.FeeStrategy;

import java.util.ArrayList;
import java.util.List;
import java.lang.classfile.constantpool.FieldRefEntry;


public abstract class BankAccount {
    //Fields(State)
    private String accountNumber;
    private String owner;
    protected double balance;
    private FeeStrategy feeStrategy;
    private List<AccountObserver> observers = new ArrayList<>();

    //Constructor
    public BankAccount(String accountNumber, String owner, double initialBalance, FeeStrategy feeStrategy) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = initialBalance;
        this.feeStrategy = feeStrategy;
    }

    //Concrete methods
    public void deposit(double amount){
        if (amount <= 0) {
            System.out.println("Deposit must be a positive");
            return;
        }
        balance += amount;
        System.out.println("Deposited " + amount);
        notifyObservers();
    }

    public void chargeFee(){
        double fee = feeStrategy.calculateFee(balance);
        balance -= fee;
        System.out.println("Charged " + fee);
        notifyObservers();
    }

    public double setFeeStrategy(FeeStrategy feeStrategy){
        this.feeStrategy = feeStrategy;
        return 0;
    }

    public void addObserver(AccountObserver observer){
        observers.add(observer);
    }

    protected void notifyObservers(){
        for (AccountObserver observer : observers){
            observer.onBalanceChanged(getAccountNumber(), balance);
        }
    }

    public double getBalance(){
        return balance;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public String getOwner(){
        return owner;
    }

    public String toString(){
        return "Account[" + accountNumber + " ], Owner: " + owner + ", Balance: "+ balance;
    }

    //abstract method
    public abstract void withdraw(double amount);

}
