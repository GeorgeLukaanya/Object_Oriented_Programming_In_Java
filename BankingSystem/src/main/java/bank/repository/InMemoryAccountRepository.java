package bank.repository;

import bank.model.BankAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepositoryInterface {

    private Map<String, BankAccount> accounts = new HashMap<>();

    @Override
    public void save(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void delete(String accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public boolean exists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }
}