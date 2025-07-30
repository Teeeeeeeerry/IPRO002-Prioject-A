import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;


public class ProjectA {

    public static void main(String[] args) {

            Account account = new Account("My Personal Account");
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
        // Category filter = null;


        Category category;
        while (true) {
            System.out.println("Select a category: ");
            int i = 1;
            for(Category c : Category.values()) {
                System.out.println((i)+ ": " + c.name());
                i++;
            }

            try {
                int choice = In.nextInt();
                if (choice < 1 || choice > Category.values().length) {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + Category.values().length);
                    continue;
                }
                category = Category.values()[choice - 1];
                break;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                In.nextLine();
            }
        }
    
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
            System.out.println((i)+ ": " + c.name());
            i++;
        }

        int choice = In.nextInt();
        Category category = Category.values()[choice - 1];
        // Category filter = null;
        // Category category;
        // while (true) {
        //     System.out.println("Select a category: ");
        //     int i = 1;
        //     for(Category c : Category.values()) {
        //         System.out.println((i)+ ": " + c.name());
        //         i++;
        //     }

        //     try {
        //         int choice = In.nextInt();
        //         if (choice < 1 || choice > Category.values().length) {
        //             System.out.println("Invalid choice. Please enter a number between 1 and " + Category.values().length);
        //             continue;
        //         }
        //         category = Category.values()[choice - 1];
        //         break;
        //     } catch (Exception e) {
        //         System.out.println("Invalid input. Please enter a number.");
        //         In.nextLine(); // clear the invalid input
        //     }
        // }

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

        // while (true) {
        //     try {
        //         int choice = In.nextInt();
        //         if (choice < 1 || choice > Category.values().length) {
        //             System.out.println("Invalid choice. Please enter a number between 1 and " + Category.values().length);
        //             continue;
        //         }
        //         filter = Category.values()[choice - 1];
        //         break;
        //     } catch (Exception e) {
        //         System.out.println("Invalid input. Please enter a number.");
        //         In.nextLine(); // clear the invalid input
        //     }
        // }

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

    // public static void filterByCategory(Account account) {
    //     Category filter = selectCategory("Filter By Category: ");
    //     for (Transaction t : account.getTransactions()) {
    //         if (t instanceof Expense) {
    //             Expense e = (Expense) t;
    //             if (e.getCategory() == filter) {
    //                 e.displayDetails();
    //             }
    //         }
    //     }
    // }

    // private static Category selectCategory(String prompt) {
    //     System.out.println(prompt);
    //     for (int i = 0; i < Category.values().length; i++) {
    //         System.out.println((i + 1)+ ": " + Category.values()[i]);
    //     }
    //     int choice = In.nextInt();
    //     return Category.values()[choice - 1];
    // }

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

    // public void displayDetails() {
    //     String type = this instanceof Income ? "Income" : "Expense";
    //     System.out.println(type + ": $" + amount + " | " + description + " | " + date + " | " + 
    //         (this instanceof Income ? ((Income)this).getCategory() : ((Expense)this).getCategory()));
    // }
    //If we conbine the "displayDetails()" function in class Income and class Expense, we should del "displayDetails()" in both class
    
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
                Category catIn = ((Income)t).getCategory();
                categoryExpenses.put(catIn, categoryExpenses.getOrDefault(catIn, 0.0) + t.getAmount());
            } else if (t instanceof Expense) {
                totalExpense += t.getAmount();
                Category catEx = ((Expense)t).getCategory();
                categoryExpenses.put(catEx, categoryExpenses.getOrDefault(catEx, 0.0) + t.getAmount());
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
            System.out.println("- " + c + ": $" + categoryIncome.getOrDefault(c, 0.0));
        }

        System.out.println("\nExpenses by Category:");
        for (Category c : Category.values()) {
            System.out.println("- " + c + ": $" + categoryExpenses.getOrDefault(c, 0.0));
        }
    }
}

// imterface Printed {
//     void displayDetails();
// }


