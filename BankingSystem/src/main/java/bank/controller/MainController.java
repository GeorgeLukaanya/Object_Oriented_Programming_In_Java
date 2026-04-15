package bank.controller;

import bank.model.BankAccount;
import bank.model.CheckingAccount;
import bank.model.SavingsAccount;
import bank.service.AccountAlreadyExistsException;
import bank.service.BankService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // ── Injected by MainApp ──
    private BankService bankService;

    // ── Navigation panels ──
    @FXML private VBox dashboardPanel;
    @FXML private VBox newAccountPanel;
    @FXML private VBox depositPanel;
    @FXML private VBox withdrawPanel;
    @FXML private VBox transferPanel;

    // ── Header / status ──
    @FXML private Label statusLabel;
    @FXML private Label bottomStatus;
    @FXML private Label accountCountLabel;

    // ── Dashboard ──
    @FXML private TableView<AccountRow> accountsTable;
    @FXML private TableColumn<AccountRow, String> colNumber;
    @FXML private TableColumn<AccountRow, String> colOwner;
    @FXML private TableColumn<AccountRow, String> colType;
    @FXML private TableColumn<AccountRow, String> colBalance;

    // ── New Account form ──
    @FXML private TextField tfAccountNumber;
    @FXML private TextField tfOwner;
    @FXML private TextField tfInitialBalance;
    @FXML private ComboBox<String> cbAccountType;
    @FXML private Label newAccountMessage;

    // ── Deposit form ──
    @FXML private TextField tfDepositAccount;
    @FXML private TextField tfDepositAmount;
    @FXML private Label depositMessage;

    // ── Withdraw form ──
    @FXML private TextField tfWithdrawAccount;
    @FXML private TextField tfWithdrawAmount;
    @FXML private Label withdrawMessage;

    // ── Transfer form ──
    @FXML private TextField tfFromAccount;
    @FXML private TextField tfToAccount;
    @FXML private TextField tfTransferAmount;
    @FXML private Label transferMessage;

    // ── Table data ──
    private ObservableList<AccountRow> tableData = FXCollections.observableArrayList();

    // ────────────────────────────────────────
    // Initialise
    // ────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Wire table columns to AccountRow properties
        colNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        accountsTable.setItems(tableData);

        // Populate account type dropdown
        cbAccountType.setItems(FXCollections.observableArrayList(
                "Checking Account", "Savings Account", "Premium Savings Account"));
        cbAccountType.getSelectionModel().selectFirst();
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
        refreshAccounts();
    }

    // ────────────────────────────────────────
    // Navigation
    // ────────────────────────────────────────
    @FXML private void showDashboard() {
        showPanel(dashboardPanel);
        refreshAccounts();
    }

    @FXML private void showNewAccount()  { showPanel(newAccountPanel); }
    @FXML private void showDeposit()     { showPanel(depositPanel); }
    @FXML private void showWithdraw()    { showPanel(withdrawPanel); }
    @FXML private void showTransfer()    { showPanel(transferPanel); }

    private void showPanel(VBox panel) {
        dashboardPanel.setVisible(false);
        newAccountPanel.setVisible(false);
        depositPanel.setVisible(false);
        withdrawPanel.setVisible(false);
        transferPanel.setVisible(false);
        panel.setVisible(true);
    }

    // ────────────────────────────────────────
    // Dashboard
    // ────────────────────────────────────────
    @FXML private void refreshAccounts() {
        tableData.clear();
        List<BankAccount> accounts = bankService.getAllAccounts();
        for (BankAccount account : accounts) {
            String type = account instanceof CheckingAccount
                    ? "Checking"
                    : account instanceof SavingsAccount
                      ? "Savings" : "Unknown";
            tableData.add(new AccountRow(
                    account.getAccountNumber(),
                    account.getOwner(),
                    type,
                    String.format("%.2f", account.getBalance())
            ));
        }
        accountCountLabel.setText("Accounts: " + tableData.size());
        setStatus("Accounts refreshed", false);
    }

    // ────────────────────────────────────────
    // New Account
    // ────────────────────────────────────────
    @FXML private void createAccount() {
        String number  = tfAccountNumber.getText().trim();
        String owner   = tfOwner.getText().trim();
        String balText = tfInitialBalance.getText().trim();
        String type    = cbAccountType.getValue();

        if (number.isEmpty() || owner.isEmpty() || balText.isEmpty()) {
            showMessage(newAccountMessage, "Please fill in all fields.", true);
            return;
        }

        double initialBalance;
        try {
            initialBalance = Double.parseDouble(balText);
            if (initialBalance < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showMessage(newAccountMessage, "Enter a valid positive balance.", true);
            return;
        }

        try {
            switch (type) {
                case "Checking Account" ->
                        bankService.openCheckingAccount(number, owner, initialBalance);
                case "Savings Account" ->
                        bankService.openSavingsAccount(number, owner, initialBalance);
                case "Premium Savings Account" ->
                        bankService.openPremiumSavingsAccount(number, owner, initialBalance);
            }
            showMessage(newAccountMessage,
                    "Account " + number + " created successfully.", false);
            clearNewAccountForm();
            setStatus("Account created: " + number, false);
        } catch (AccountAlreadyExistsException e) {
            showMessage(newAccountMessage, e.getMessage(), true);
        }
    }

    @FXML private void clearNewAccountForm() {
        tfAccountNumber.clear();
        tfOwner.clear();
        tfInitialBalance.clear();
        cbAccountType.getSelectionModel().selectFirst();
        newAccountMessage.setText("");
    }

    // ────────────────────────────────────────
    // Deposit
    // ────────────────────────────────────────
    @FXML private void performDeposit() {
        String number  = tfDepositAccount.getText().trim();
        String amtText = tfDepositAmount.getText().trim();

        if (number.isEmpty() || amtText.isEmpty()) {
            showMessage(depositMessage, "Please fill in all fields.", true);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showMessage(depositMessage, "Enter a valid positive amount.", true);
            return;
        }

        boolean success = bankService.deposit(number, amount);
        if (success) {
            showMessage(depositMessage,
                    "Deposited " + amount + " to " + number, false);
            tfDepositAmount.clear();
            setStatus("Deposit successful", false);
        } else {
            showMessage(depositMessage,
                    "Account not found: " + number, true);
        }
    }

    // ────────────────────────────────────────
    // Withdraw
    // ────────────────────────────────────────
    @FXML private void performWithdraw() {
        String number  = tfWithdrawAccount.getText().trim();
        String amtText = tfWithdrawAmount.getText().trim();

        if (number.isEmpty() || amtText.isEmpty()) {
            showMessage(withdrawMessage, "Please fill in all fields.", true);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showMessage(withdrawMessage, "Enter a valid positive amount.", true);
            return;
        }

        boolean success = bankService.withdraw(number, amount);
        if (success) {
            showMessage(withdrawMessage,
                    "Withdrew " + amount + " from " + number, false);
            tfWithdrawAmount.clear();
            setStatus("Withdrawal successful", false);
        } else {
            showMessage(withdrawMessage,
                    "Transaction failed for: " + number, true);
        }
    }

    // ────────────────────────────────────────
    // Transfer
    // ────────────────────────────────────────
    @FXML private void performTransfer() {
        String from    = tfFromAccount.getText().trim();
        String to      = tfToAccount.getText().trim();
        String amtText = tfTransferAmount.getText().trim();

        if (from.isEmpty() || to.isEmpty() || amtText.isEmpty()) {
            showMessage(transferMessage, "Please fill in all fields.", true);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showMessage(transferMessage, "Enter a valid positive amount.", true);
            return;
        }

        boolean success = bankService.transfer(from, to, amount);
        if (success) {
            showMessage(transferMessage,
                    "Transferred " + amount + " from " + from + " to " + to, false);
            tfTransferAmount.clear();
            setStatus("Transfer successful", false);
        } else {
            showMessage(transferMessage,
                    "Transfer failed. Check account numbers and balance.", true);
        }
    }

    // ────────────────────────────────────────
    // Helpers
    // ────────────────────────────────────────
    private void showMessage(Label label, String message, boolean isError) {
        label.setText(message);
        label.getStyleClass().removeAll("success", "error");
        label.getStyleClass().add(isError ? "error" : "success");
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("success", "error");
        statusLabel.getStyleClass().add(isError ? "error" : "success");
    }

    // ────────────────────────────────────────
    // Inner class - table row data model
    // ────────────────────────────────────────
    public static class AccountRow {
        private final String accountNumber;
        private final String owner;
        private final String type;
        private final String balance;

        public AccountRow(String accountNumber, String owner,
                          String type, String balance) {
            this.accountNumber = accountNumber;
            this.owner = owner;
            this.type = type;
            this.balance = balance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getOwner()         { return owner; }
        public String getType()          { return type; }
        public String getBalance()       { return balance; }
    }
}