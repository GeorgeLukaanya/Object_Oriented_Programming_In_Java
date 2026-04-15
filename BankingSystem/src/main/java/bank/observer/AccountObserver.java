package bank.observer;

public interface AccountObserver {
    void onBalanceChanged(String accountNumber, double newBalance);
}
