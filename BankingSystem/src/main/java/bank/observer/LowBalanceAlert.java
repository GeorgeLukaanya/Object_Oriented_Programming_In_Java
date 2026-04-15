package bank.observer;

public class LowBalanceAlert implements AccountObserver{

    private double threshold;

    public LowBalanceAlert(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public void onBalanceChanged(String accountNumber, double newBalance) {
        if(newBalance < threshold) {
            System.out.println("ALERT: Account " + accountNumber + " balance is low: " + newBalance);
        }
    }
}
