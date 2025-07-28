import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;


public class ProjectA {

    public static void main(String[] args) {

            Account account = new Account("My Personal Account");
            boolean running = true;
            while (running) {
            System.out.println("\n===== Finance Manager =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. Sort Transactions");
            System.out.println("5. Filter Expenses by Category");
            System.out.println("6. Generate Report");
            System.out.println("7. Exit");
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
    public static void addIncome(Account account) {
        System.out.println("Add the income: "); 
        double amount = In.nextDouble();
        System.out.println("Enter contents of description: "); 
        String description = In.nextLine();
        System.out.println("Enter the date (DD-MM-YYYY): ");
        String date = In.nextLine();

        Income income = new Income(amount, description, date);
        account.addTransaction(income);
    }

    public static void addExpense(Account account) {
        System.out.println("Add the income: "); 
        double amount = In.nextDouble();
        System.out.println("Enter contents of description: "); 
        String description = In.nextLine();
        System.out.println("Enter the date (YYYY-MM-DD): ");
        String date = In.nextLine();
        System.out.println("Select a category: ");

        int i = 1;
        for(Category c : Category.values()) {
            System.out.println((i)+ ": " + c.name());
            i++;
        }

        int choice = In.nextInt();
        Category category = Category.values()[choice - 1];

        Expense expense = new Expense(amount, description, date, category);
        account.addTransaction(expense);

    } 
    
    public static void displayTransactions(Account account) {
        List<Transaction> transactions = account.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("There are no Transactions"); 
            return;
        } else for (Transaction i : transactions) {
            i.displayDetails();
        }
    }

    public static void filterByCategory(Account account) {
        System.out.println("Filter By Category: ");
        for (int i = 0; 1 < Category.values().length; i++) {
            System.out.println((i + 1)+ ": " + Category.values()[i]);
        }
        int choice = In.nextInt();
        Category filter = Category.values()[choice - 1];

        for (Transaction t : account.getTransactions()) {
            if (t instanceof Expense) {
                Expense e = (Expense) t;
                if (e.getCategory() == filter) {
                    e.displayDetails();
                }
            }

        }

    }

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

    public void addTransaction(double amount, String description, String date) {
        Income ic = new Income(amount, description, date);
        transactions.add(ic);
        System.out.println("Transaction added successfully");
    }

    public void removeTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
            System.out.println("Transaction removed");
        }
    }

    public void removeTransaction(Income ic) {
        transactions.remove(ic);
        // if (index >= 0 && index < transactions.size()) {
        //     transactions.remove(index);
        //     System.out.println("Transaction removed");
        // }
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

    // made sure to add some throw and catch exception errors if the user has any invalid values
    public Transaction(double amount, String description, String date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("ERROR: Amount value must be a positive number");
        } else if (date == null){
            throw new IllegalArgumentException("ERROR: Valid date is required");
        } else if (description == null ) {
            throw new IllegalArgumentException("ERROR: Valid description is required");
        }
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public abstract void displayDetails(); 

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String toString() {
        return "Amount: " + amount + " Date: " + date + " Description: " + description;
    }
    
}

class Income extends Transaction {

    public Income(double amount, String description, String date) {
        super(amount, description, date);
    }

    @Override
    public void displayDetails() {
        System.out.printf("");
    }
}

class Expense extends Transaction {

    private final Category category;

    public Expense(double amount, String description, String date, Category category) {
        super(amount, description, date);
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

//filters by date in ascending order 
class FinanceComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t, Transaction t2) {
        return t.getDate().compareTo(t2.getDate());
    }

}

class ReportGenerator {
    public static void generateReport(Account account) {

    }

}


