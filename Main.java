import java.io.*;
import java.util.*;

class InsufficientFunds extends Exception {}

class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private long accountNumber;
    private String firstName;
    private String lastName;
    private float balance;
    private static long nextAccountNumber = 0;

    public Account() {}

    public Account(String fname, String lname, float balance) {
        nextAccountNumber++;
        this.accountNumber = nextAccountNumber;
        this.firstName = fname;
        this.lastName = lname;
        this.balance = balance;
    }

    public long getAccNo() {
        return accountNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public float getBalance() {
        return balance;
    }

    public void deposit(float amount) {
        this.balance += amount;
    }

    public void withdraw(float amount) throws InsufficientFunds {
        if (this.balance - amount < 500) {
            throw new InsufficientFunds();
        }
        this.balance -= amount;
    }

    public static void setLastAccountNumber(long accountNumber) {
        nextAccountNumber = accountNumber;
    }

    public static long getLastAccountNumber() {
        return nextAccountNumber;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + "\nLast Name: " + lastName + "\nAccount Number: " + accountNumber + "\nBalance: " + balance;
    }
}

class Bank {
    private Map<Long, Account> accounts = new HashMap<>();

    public Bank() {
        try {
            FileInputStream fileIn = new FileInputStream("Bank.data");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            accounts = (HashMap<Long, Account>) in.readObject();
            Account.setLastAccountNumber((long) accounts.size());
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            accounts = new HashMap<>();
        }
    }

    public Account openAccount(String fname, String lname, float balance) {
        Account account = new Account(fname, lname, balance);
        accounts.put(account.getAccNo(), account);
        saveAccounts();
        return account;
    }

    public Account balanceEnquiry(long accountNumber) {
        return accounts.get(accountNumber);
    }

    public Account deposit(long accountNumber, float amount) {
        Account account = accounts.get(accountNumber);
        account.deposit(amount);
        saveAccounts();
        return account;
    }

    public Account withdraw(long accountNumber, float amount) throws InsufficientFunds {
        Account account = accounts.get(accountNumber);
        account.withdraw(amount);
        saveAccounts();
        return account;
    }

    public void closeAccount(long accountNumber) {
        accounts.remove(accountNumber);
        saveAccounts();
    }

    public void showAllAccounts() {
        for (Account account : accounts.values()) {
            System.out.println(account);
        }
    }

    private void saveAccounts() {
        try {
            FileOutputStream fileOut = new FileOutputStream("Bank.data");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(accounts);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);
        Account account;
        System.out.println("i love you");
        while (true) {
            System.out.println("\n***Banking System***");
            System.out.println("1. Open an Account");
            System.out.println("2. Balance Enquiry");
            System.out.println("3. Deposit");
            System.out.println("4. Withdrawal");
            System.out.println("5. Close an Account");
            System.out.println("6. Show All Accounts");
            System.out.println("7. Quit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter First Name: ");
                    String fname = scanner.next();
                    System.out.print("Enter Last Name: ");
                    String lname = scanner.next();
                    System.out.print("Enter initial Balance: ");
                    float balance = scanner.nextFloat();
                    account = bank.openAccount(fname, lname, balance);
                    System.out.println("Congratulations, Account Created:\n" + account);
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    long accountNumber = scanner.nextLong();
                    account = bank.balanceEnquiry(accountNumber);
                    System.out.println("Your Account Details:\n" + account);
                    break;

                case 3:
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.nextLong();
                    System.out.print("Enter Amount: ");
                    float depositAmount = scanner.nextFloat();
                    account = bank.deposit(accountNumber, depositAmount);
                    System.out.println("Amount Deposited:\n" + account);
                    break;

                case 4:
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.nextLong();
                    System.out.print("Enter Amount: ");
                    float withdrawAmount = scanner.nextFloat();
                    try {
                        account = bank.withdraw(accountNumber, withdrawAmount);
                        System.out.println("Amount Withdrawn:\n" + account);
                    } catch (InsufficientFunds e) {
                        System.out.println("Insufficient funds.");
                    }
                    break;

                case 5:
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.nextLong();
                    bank.closeAccount(accountNumber);
                    System.out.println("Account Closed.");
                    break;

                case 6:
                    bank.showAllAccounts();
                    break;

                case 7:
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Enter correct choice.");
                    break;

            }
        }
    }
}
