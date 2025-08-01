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
            System.out.println("4. Sort Transactions by time");
            System.out.println("5. Category Filter");
            System.out.println("6. Generate Report");
            System.out.println("7. Delete Transaction");
            System.out.println("8. Print Transaction Summary");
            System.out.println("9. Exit");
            System.out.print("Select option: ");
            
            int option;

            while (true) {
                System.out.print("Select option: ");
                option = In.nextInt();
                if (option >= 1 && option <= 8) {
                    break;
                }
                System.out.println("Invalid option, please enter 1-8");
            }
            
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
                deleteTransaction(account);
            }
            else if (option ==8) {
                printTransactionSummary(account);
            }
            else if (option == 9) {
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

        int choice;
        while (true) {
            System.out.print("Enter category number: ");
            choice = In.nextInt();
            if (choice >= 1 && choice <= Category.values().length) {
                break;
            }
            System.out.println("Invalid category, please enter 1-" + (i-1));
        }
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

        int choice;
        while (true) {
            System.out.print("Enter category number: ");
            choice = In.nextInt();
            if (choice >= 1 && choice <= Category.values().length) {
                break;
            }
            System.out.println("Invalid category, please enter 1-" + (i-1));
        }
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


    public static void deleteTransaction(Account account) {
        boolean validOption = false;
        while (!validOption) {
            try {
                System.out.println("\n===== Delete Transaction =====");
                System.out.println("1. Delete Income");
                System.out.println("2. Delete Expense");
                System.out.println("3. Back to Main Menu");
                System.out.print("Select option (1-3): ");
                
                int option = In.nextInt();
                
                if (option == 1 || option == 2) {
                    boolean validRecord = false;
                    while (!validRecord) {
                        try {
                            if (option == 1) {
                                System.out.println("Current Income Records:");
                                displayIncomes(account);
                                System.out.print("Enter income record number to delete (0 to cancel): ");
                                int incomeIndex = In.nextInt();
                                
                                if (incomeIndex == 0) {
                                    break;
                                }
                                
                                List<Income> incomes = new ArrayList<>();
                                for (Transaction t : account.getTransactions()) {
                                    if (t instanceof Income) {
                                        incomes.add((Income)t);
                                    }
                                }
                                
                                if (incomeIndex > 0 && incomeIndex <= incomes.size()) {
                                    account.removeTransaction(incomes.get(incomeIndex - 1));
                                    validRecord = true;
                                    validOption = true;
                                } else {
                                    System.out.println("Error: Invalid number, please enter 1-" + incomes.size() + " or 0 to cancel");
                                }
                            } else if (option == 2) {
                                System.out.println("Current Expense Records:");
                                displayExpenses(account);
                                System.out.println("Enter expense record number to delete (0 to cancel): ");
                                int expenseIndex = In.nextInt();
                                
                                if (expenseIndex == 0) {
                                    break;
                                }
                                
                                List<Expense> expenses = new ArrayList<>();
                                for (Transaction t : account.getTransactions()) {
                                    if (t instanceof Expense) {
                                        expenses.add((Expense)t);
                                    }
                                }
                                
                                if (expenseIndex > 0 && expenseIndex <= expenses.size()) {
                                    account.removeTransaction(expenses.get(expenseIndex - 1));
                                    validRecord = true;
                                    validOption = true;
                                } else {
                                    System.out.println("Error: Invalid number, please enter 1-" + expenses.size() + " or 0 to cancel");
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                            In.nextLine(); 
                        }
                    }
                } else if (option == 3) {
                    validOption = true;
                } else {
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                In.nextLine();
            }
        }
    }

    // Helper method to display income records
    private static void displayIncomes(Account account) {
        int i = 1;
        for (Transaction t : account.getTransactions()) {
            if (t instanceof Income) {
                System.out.print(i++ + ". ");
                t.displayDetails();
            }
        }
    }
    
    // Helper method to display expense records
    private static void displayExpenses(Account account) {
        int i = 1;
        for (Transaction t : account.getTransactions()) {
            if (t instanceof Expense) {
                System.out.print(i++ + ". ");
                t.displayDetails();
            }
        }
    }

    //interface part
    public static void printTransactionSummary(Account account) {
        for (Transaction t : account.getTransactions()) {
            t.printSummary();
            System.out.println("------");
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

    public void removeTransaction(Income income) {
        transactions.remove(income);
        System.out.println("Transaction removed");
    }

    public void removeTransaction(Expense expense) {
        transactions.remove(expense);
        System.out.println("Expense removed");
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

    // static final Comparator<Transaction> DATE_COMPARATOR = 
    //     Comparator.comparing((Transaction t) -> t.getDate().split("-")[2]) 
    //              .thenComparing(t -> t.getDate().split("-")[1])            
    //              .thenComparing(t -> t.getDate().split("-")[0]);           

    // public void sortTransactions() {
    //     Collections.sort(transactions, DATE_COMPARATOR);
   
}

// interface add
interface Printable {
    void printSummary();
}

abstract class Transaction implements Printable {

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

    //interface part
    @Override
    public void printSummary() {
        System.out.println("Transaction Summary:");
        System.out.println("- Amount: " + amount);
        System.out.println("- Date: " + date);
        System.out.println("- Description: " + description);
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