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
            
            switch (In.nextInt()) {
                case 1:
                    addIncome(account);
                    break;
                case 2:
                    addExpense(account);
                    break;
                case 3:
                    displayTransactions(account);
                    break;
                case 4:
                    ReportGenerator.generateReport(account);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

public class Account {
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

    public List<Transaction> getTransactionsByCategory(Category category) {
        return transactions.stream()
                .filter(t -> t instanceof Expense)
                .filter(e -> ((Expense) e).getCategory() == category)
                .toList();
    }

    public void sortTransactions() {
        transactions.sort(new FinanceComparator());
    }
}


    private static void addIncome(Account account) {
        System.out.pri
import java.util.Locale.Category;nt("Enter amount: ");
        double amount = In.nextDouble();
        System.out.print("Enter description: ");
        String desc = In.nextLine();
        account.addTransaction(new Income(amount, desc));
    }

    private static void addExpense(Account account) {
        System.out.print("Enter amount: ");
        double amount = In.nextDouble();
        System.out.print("Enter description: ");
        String desc = In.nextLine();
        System.out.println("Categories: FOOD, TRANSPORT, ENTERTAINMENT, OTHER");
        System.out.print("Select category: ");
        Category category = Category.valueOf(In.nextLine().toUpperCase());
        account.addTransaction(new Expense(amount, desc, category));
    }

    private static void displayTransactions(Account account) {
        System.out.println("\n--- All Transactions ---");
        account.getTransactions().forEach(Transaction::displayDetails);
    }


}

public abstract class Transaction {
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
