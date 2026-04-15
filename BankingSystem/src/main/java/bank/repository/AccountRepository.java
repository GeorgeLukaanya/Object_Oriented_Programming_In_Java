//This layer is responsible for storing and retrieving accounts. For now we use an in-memory map —
// later this could be swapped for a real database without touching any other layer.
package bank.repository;

import bank.model.BankAccount;
import java.util.ArrayList;
import java.util.Map; //works like a dictionary
import java.util.HashMap;
import java.util.List;


public class AccountRepository implements AccountRepositoryInterface {
    private Map<String, BankAccount> accounts = new HashMap<>();

    @Override
    public void save(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Saved: " + account.getAccountNumber());
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber){
        return accounts.get(accountNumber);
    }

    @Override
    public List<BankAccount> findAll(){
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void delete(String accountNumber){
        accounts.remove(accountNumber);
        System.out.println("Deleted: " + accountNumber);
    }

    @Override
    public boolean exists(String accountNumber){
        return accounts.containsKey(accountNumber);
    }


}
