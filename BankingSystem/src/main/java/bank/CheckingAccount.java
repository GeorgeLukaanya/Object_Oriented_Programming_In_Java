package bank;

public class CheckingAccount extends BankAccount{
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, String owner, double initialBalance, double overdraftLimit, FeeStrategy feeStrategy){
        super(accountNumber, owner, initialBalance, feeStrategy);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive");
            return;
        }
        if(balance - amount < -overdraftLimit){
            System.out.println("Exceeds overdraft limit.");
        }
        balance -= amount;
        System.out.println("Withdrawn amount: " + amount);
        notifyObservers();
    }

    public String toString() {
        return "Checking[" + getAccountNumber() + "] Owner: " + getOwner() +
                " Balance: " + balance + " Overdraft Limit: " + overdraftLimit;
    }
}
