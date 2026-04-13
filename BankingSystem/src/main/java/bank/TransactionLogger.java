package bank;

public class TransactionLogger implements AccountObserver{

    @Override
    public void onBalanceChanged(String accountNumber, double newBalance){
        System.out.println("LOG: Account " + accountNumber + " new balance: " + newBalance);
    }
}
