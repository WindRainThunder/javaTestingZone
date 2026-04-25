package encapsulation;

public class BankAccount {
    private String owner;
    private double balance;

    public BankAccount(String owner, double initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setOwner(String owner) {
        if (owner != null && !owner.trim().isEmpty()) {
            this.owner = owner;
        } else {
            System.out.println("Incorrect owner name");
        }
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount + " PLN");
        } else {
            System.out.println("Incorrect deposit amount");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount + " PLN");
            return true;
        } else {
            System.out.println("Cannot withdraw (insufficient funds)");
            return false;
        }
    }
}