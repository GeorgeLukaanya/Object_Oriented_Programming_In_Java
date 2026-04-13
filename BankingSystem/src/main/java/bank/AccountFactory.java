package bank;

public class AccountFactory {

    public static CheckingAccount createCheckingAccount(String accountNumber, String owner, double initialBalance){
        return new CheckingAccount(accountNumber, owner, initialBalance, 500.0, new FlatFeeStrategy(10.0));
    }

    public static SavingsAccount createSavingsAccount(String accountNumber, String owner, double initialBalance){
        return new SavingsAccount(accountNumber, owner, initialBalance, 0.05, new NoFeeStrategy());
    }

    public static SavingsAccount createPremiumSavingsAccount(String accountNumber, String owner, double initialBalance){
        return  new SavingsAccount(accountNumber, owner, initialBalance, 0.08, new NoFeeStrategy());
    }
}
