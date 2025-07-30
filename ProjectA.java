import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;


public class ProjectA {

    public static void main(String[] args) {

        Account account = new Account("My Personal Account");

        SampleTransactions.createSampleData(account);

        boolean running = true;
        while (running) {
            System.out.println("\n===== Finance Manager =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. Sort Transactions Filter");
            System.out.println("5. Category Filter");
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
        System.out.println("Select a category: ");

        int  i = 1;
        for (Category c : Category.values()) {
            if (c.isIncome()) {
                System.out.println((i) + ": " + c.name());
                i++;
            }
        }

        int choice = In.nextInt();
        Category category = Category.values()[choice - 1];
    
        Income income = new Income(amount, description, date, category);
        account.addTransaction(income);
    }

    public static void addExpense(Account account) {
        System.out.println("Add the Expense: "); 
        double amount = In.nextDouble();
        System.out.println("Enter contents of description: "); 
        String description = In.nextLine();
        System.out.println("Enter the date (DD-MM-YYYY): ");
        String date = In.nextLine();
        System.out.println("Select a category: ");

        int i = 1;
        for(Category c : Category.values()) {
            if (c.isExpense()){
                System.out.println((i)+ ": " + c.name());
                i++;
            }
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
        } 
         for (Transaction i : transactions) {
            i.displayDetails();
        }
    }

    public static void filterByCategory(Account account) {
        System.out.println("Filter By Category: ");
        for (int i = 0; i < Category.values().length; i++) {
            System.out.println((i + 1)+ ": " + Category.values()[i]);
        }
        int choice = In.nextInt();
        Category filter = Category.values()[choice - 1];

        for (Transaction t : account.getTransactions()) {
            if (t instanceof Income) {
                Income c = (Income) t;
                if (c.getCategory() == filter) {
                    c.displayDetails();
                }
            } else if (t instanceof Expense) {
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

    public void addTransaction(Income income) {
        transactions.add(income);
        System.out.println("Income added successfully");
    }

    public void addTransaction(Expense expense) {
        transactions.add(expense);
        System.out.println("Expense added successfully");
    }
    
    // public void addTransaction(double amount, String description, String date, Category category) {
    //     Transaction transaction = new Transaction (amount, description, date, category);
    //     transactions.add(transaction);
    //     System.out.println("Transaction added successfully");
    // }

    public void removeTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
            System.out.println("Transaction removed");
        }
    }

    public void removeTransaction(Income income) {
        transactions.remove(income);
        System.out.println("Transaction removed");
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

abstract class Transaction /*implements Printed*/ {

    protected final double amount;
    protected final String description;
    protected final String date;

    // made sure to add some throw and catch exception errors if the user has any invalid values
    public Transaction(double amount, String description, String date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("ERROR: Amount value must be a positive number");
        } 
        if(date == null){
            throw new IllegalArgumentException("ERROR: Valid date is required");
        } 
        if (description == null ) {
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

    private final Category category;

    public Income(double amount, String description, String date, Category category) {
        super(amount, description, date);
        this.category = category;
    }

    @Override
    public void displayDetails() {
        System.out.println("Income: $" + amount + " | " + description + " | " + date + " | " + category);
    }

    public Category getCategory() {
        return category;
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
        System.out.println("Expense: $" + amount + " | " + description + " | " + date +  " | " + category);
    }

    public Category getCategory() {
        return category;
    }
}

enum Category {
    // income C
    SALARY(true), 
    BONUS(true), 
    INVESTMENT(true),
    
    // expense C
    FOOD(false), 
    TRANSPORT(false), 
    ENTERTAINMENT(false),
    
    // other C of I&E
    OTHER_INCOME(true),
    OTHER_EXPENSE(false);

    private final boolean isIncome;

    Category(boolean isIncome) {
        this.isIncome = isIncome;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public boolean isExpense() {
        return !isIncome;
    }
}


//filters by date in ascending order 
class FinanceComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        String[] parts1 = t1.getDate().split("-");
        String[] parts2 = t2.getDate().split("-");
        
        int yearCompare = parts1[2].compareTo(parts2[2]);
        if (yearCompare != 0) return yearCompare;
        
        int monthCompare = parts1[1].compareTo(parts2[1]);
        if (monthCompare != 0) return monthCompare;
        
        return parts1[0].compareTo(parts2[0]);
    }
}

class ReportGenerator {
    public static void generateReport(Account account) {
        System.out.println("\n=== FINANCIAL REPORT ===");
        System.out.println("Account: " + account.getName());
        
        // Calculate totals
        double totalIncome = 0;
        double totalExpense = 0;
        HashMap<Category, Double> categoryIncome = new HashMap<>();
        HashMap<Category, Double> categoryExpenses = new HashMap<>();
        
        for (Transaction t : account.getTransactions()) {
            if (t instanceof Income) {
                totalIncome += t.getAmount();
                Category cat = ((Income)t).getCategory();
                categoryIncome.put(cat, categoryIncome.getOrDefault(cat, 0.0) + t.getAmount());
            } else if (t instanceof Expense) {
                totalExpense += t.getAmount();
                Category cat = ((Expense)t).getCategory();
                categoryExpenses.put(cat, categoryExpenses.getOrDefault(cat, 0.0) + t.getAmount());
            }
        }
        
        // Print summary
        System.out.println();
        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expenses: $" + totalExpense);
        System.out.println("Net Balance: $" + (totalIncome - totalExpense));
        
        // Print category breakdown
        System.out.println("\nIncome by Category:");
        for (Category c : Category.values()) {
            if (c.isIncome()) {
                System.out.println("- " + c + ": $" + categoryIncome.getOrDefault(c, 0.0));
            }
        }

        System.out.println("\nExpenses by Category:");
        for (Category c : Category.values()) {
            if (c.isExpense()) {
                System.out.println("- " + c + ": $" + categoryExpenses.getOrDefault(c, 0.0));
            }
        }
    }
}

// imterface Printed {
//     void displayDetails();
// }

//test
class SampleTransactions {
    
    public static void createSampleData(Account account) {
        // Income transactions
        account.addTransaction(new Income(5000.00, "Monthly Salary", "15-03-2023", Category.SALARY));
        account.addTransaction(new Income(1000.00, "Yearly Bonus", "20-12-2023", Category.BONUS));
        account.addTransaction(new Income(250.50, "Stock Dividends", "05-07-2023", Category.INVESTMENT));

        // Expense transactions
        account.addTransaction(new Expense(150.75, "Grocery Shopping", "10-01-2023", Category.FOOD));
        account.addTransaction(new Expense(45.00, "Bus Pass", "03-11-2023", Category.TRANSPORT));
        account.addTransaction(new Expense(120.00, "Movie Tickets", "25-05-2023", Category.ENTERTAINMENT));
    }
}