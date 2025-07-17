import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProjectA {

    public static void main(String[] args) {
            //Account account = new Account("My Personal Account");
            boolean running = true;
            while (running) {
            System.out.println("\n===== Personal Finance Manager =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. Generate Report");
            System.out.println("5. Exit");
            System.out.print("Select option: ");
            
        }
    }

}

class Account {
    
}

abstract class Transaction {
    protected final double amount;
    protected final String description;
    protected final String date;

    public Transaction(double amount, String description) {
        this.amount = amount;
        this.description = description;
        this.date = java.time.LocalDate.now().toString();
    }

    public abstract void displayDetails();

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}

class Income extends Transaction {
    public Income(double amount, String description) {
        super(amount, description);
    }

    @Override
    public void displayDetails() {
        System.out.printf("");
    }
}

class Expense extends Transaction {
    private final Category category;

    public Expense(double amount, String description, Category category) {
        super(amount, description);
        this.category = category;
    }

    @Override
    public void displayDetails() {
        System.out.printf("");
    }

    public Category getCategory() {
        return category;
    }
}

enum Category {
    FOOD, TRANSPORT, ENTERTAINMENT, OTHER
}