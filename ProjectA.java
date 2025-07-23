import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProjectA {

    public static void main(String[] args) {

            Account account = new Account("My Personal Account");
            boolean running = true;
            while (running) {
            System.out.println("\n===== Personal Finance Manager =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. Generate Report");
            System.out.println("5. Exit");
            System.out.print("Select option: ");
            
            int option = In.nextInt();
            
            if (option == 1) {
                addIncome(account);
            } 
            else if (option == 2) {
                addExpense(account);
            } 
            else if (option == 3) {
                displayTransactions(account);
            } 
            else if (option == 4) {
                account.sortTransactions();
                System.out.println("Transactions sorted by date");
            } 
            else if (option == 5) {
                filterByCategory(account);
            } 
            else if (option == 6) {
                ReportGenerator.generateReport(account);
            } 
            else if (option == 7) {
                running = false;
            } 
            else {
                System.out.println("Invalid option");
            }

        }
    }

    //function part
    

}

class Account {
    
    private final String name;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(String name) {
        this.name = name;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        System.out.println("Transaction added successfully");
    }

    public void removeTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
            System.out.println("Transaction removed");
        }
    }
    
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }


    //get transction by category

    public void sortTransactions() {
        transactions.sort(new FinanceComparator());
    }

    public String getName() {
        return name;
    }

}

abstract class Transaction {

    protected final double amount;
    protected final String description;
    protected final String date;

    public Transaction(double amount, String description) {
        this.amount = amount;
        this.description = description;
        this.date = data;

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