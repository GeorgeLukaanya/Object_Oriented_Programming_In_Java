package bank;

public interface AccountObserver {
    void onBalanceChanged(String accountNumber, double newBalance);
}
