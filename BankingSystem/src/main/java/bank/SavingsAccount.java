package bank;

public class SavingsAccount extends BankAccount{

    public double interestRate;

    public SavingsAccount(String accountNumber, String owner, double initialBalance, double interestRate, FeeStrategy feeStrategy) {
        super(accountNumber, owner, initialBalance, feeStrategy);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount){
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }

        if(amount > balance){
            System.out.println("Insufficient funds. Savings cannot go negative.");
            return;
        }
        balance -= amount;
        notifyObservers();
    }

    public void applyInterest(){
        double interest = balance * interestRate;
        balance += interest;
        System.out.println("Interest applied: " + interest);
        notifyObservers();
    }

    public String toString(){
        return "Savings[" + getAccountNumber() + "], Owner: " + getOwner() + ", Balance: " + balance + " Interest Rate: " + interestRate;
    }
}
