package bank;

public class Bank {
    public static void main(String[] args){

        //Factory Pattern
        CheckingAccount checking = AccountFactory.createCheckingAccount("ACC001", "Mariam", 1000.0);
        SavingsAccount savings = AccountFactory.createSavingsAccount("ACC002", "George", 5000);
        SavingsAccount premium = AccountFactory.createPremiumSavingsAccount("ACC003", "John", 5000);

        //Observer Pattern - attach listeners
        LowBalanceAlert alert = new LowBalanceAlert(200.0);
        TransactionLogger logger = new TransactionLogger();

        checking.addObserver(alert);
        checking.addObserver(logger);
        savings.addObserver(alert);
        savings.addObserver(alert);

        System.out.println("=== Initial state ===");
        System.out.println(checking);
        System.out.println(savings);
        System.out.println(premium);
        System.out.println("---");

        //Strategy Pattern - fees calculated differently per account
        System.out.println("=== Transactions ===");
        checking.deposit(500.0);
        checking.withdraw(1200);
        checking.chargeFee();

        savings.deposit(300.0);
        savings.withdraw(1900.0);       // triggers low balance alert
        savings.applyInterest();

        premium.deposit(1000.0);        // no observers - silent
        premium.applyInterest();

        System.out.println("---");
        System.out.println("=== Final State ===");
        System.out.println(checking);
        System.out.println(savings);
        System.out.println(premium);

        System.out.println("---");

        // Strategy Pattern - swap fee strategy at runtime
        System.out.println("=== Strategy Swap ===");
        checking.setFeeStrategy(new PercentageFeeStrategy(0.02));
        checking.chargeFee();           // now charges 2% instead of flat 10
        System.out.println(checking);
    }
}
