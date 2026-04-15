package bank.repository;

import bank.model.BankAccount;
import java.util.List;

public interface AccountRepositoryInterface {
    void save(BankAccount account);
    BankAccount findByAccountNumber(String accountNumber);
    List<BankAccount> findAll();
    void delete(String  accountNumber);
    boolean exists(String accountNumber);
}
