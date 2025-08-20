import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.HashMap;

public class Model {
    private final Account account;
    private final ObservableList<Transaction> transactions;

    public Model() {
        this.account = new Account("My Personal Account");
        this.transactions = FXCollections.observableArrayList();
        SampleTransactions.createSampleData(account);
        transactions.setAll(account.getTransactions());
    }

    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction instanceof Income) {
            account.addTransaction((Income) transaction);
        } else {
            account.addTransaction((Expense) transaction);
        }
        transactions.setAll(account.getTransactions());
    }

    public void removeTransaction(Transaction transaction) {
        if (transaction instanceof Income) {
            account.removeTransaction((Income) transaction);
        } else {
            account.removeTransaction((Expense) transaction);
        }
        transactions.setAll(account.getTransactions());
    }

    public void sortTransactions() {
        account.sortTransactions();
        transactions.setAll(account.getTransactions());
    }

    public String generateReport() {
        return ReportGenerator.generateReportString(account);
    }
}

class Account {
    private final String name;
    private final java.util.List<Transaction> transactions = new java.util.ArrayList<>();

    public Account(String name) {
        this.name = name;
    }

    public void addTransaction(Income income) {
        transactions.add(income);
    }

    public void addTransaction(Expense expense) {
        transactions.add(expense);
    }

    public void removeTransaction(Income income) {
        transactions.remove(income);
    }

    public void removeTransaction(Expense expense) {
        transactions.remove(expense);
    }

    public java.util.List<Transaction> getTransactions() {
        return java.util.Collections.unmodifiableList(transactions);
    }

    public void sortTransactions() {
        transactions.sort(Transaction.DATE_COMPARATOR);
    }

    public String getName() {
        return name;
    }
}

abstract class Transaction {
    static final Comparator<Transaction> DATE_COMPARATOR =
            Comparator.comparing(Transaction::getYear)
                    .thenComparing(Transaction::getMonth)
                    .thenComparing(Transaction::getDay);

    protected final double amount;
    protected final String description;
    protected final String date;

    public Transaction(double amount, String description, String date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("ERROR: Amount value must be a positive number");
        }
        if (date == null) {
            throw new IllegalArgumentException("ERROR: Valid date is required");
        }
        if (description == null) {
            throw new IllegalArgumentException("ERROR: Valid description is required");
        }
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    private int extractNumber(int start, int end) {
        int value = 0;
        for (int i = start; i <= end; i++) {
            char c = date.charAt(i);
            if (c != '-') { // ignore dashes
                value = value * 10 + (c - '0');
            }
        }
        return value;
    }

    private int getYear() {
        return extractNumber(0, 3); // YYYY
    }

    private int getMonth() {
        return extractNumber(5, 6); // MM
    }

    private int getDay() {
        return extractNumber(8, 9); // DD
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

    @Override
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
        System.out.println("Expense: $" + amount + " | " + description + " | " + date + " | " + category);
    }

    public Category getCategory() {
        return category;
    }
}

enum Category {
    SALARY(true), BONUS(true), INVESTMENT(true),
    FOOD(false), TRANSPORT(false), ENTERTAINMENT(false),
    OTHER_INCOME(true), OTHER_EXPENSE(false);

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

class ReportGenerator {
    public static String generateReportString(Account account) {
        StringBuilder report = new StringBuilder();
        report.append("\n=== FINANCIAL REPORT ===\n");
        report.append("Account: ").append(account.getName()).append("\n\n");

        double totalIncome = 0;
        double totalExpense = 0;
        HashMap<Category, Double> categoryIncome = new HashMap<>();
        HashMap<Category, Double> categoryExpenses = new HashMap<>();

        for (Transaction t : account.getTransactions()) {
            if (t instanceof Income) {
                totalIncome += t.getAmount();
                Category cat = ((Income) t).getCategory();
                categoryIncome.put(cat, categoryIncome.getOrDefault(cat, 0.0) + t.getAmount());
            } else if (t instanceof Expense) {
                totalExpense += t.getAmount();
                Category cat = ((Expense) t).getCategory();
                categoryExpenses.put(cat, categoryExpenses.getOrDefault(cat, 0.0) + t.getAmount());
            }
        }

        report.append("Total Income: $").append(totalIncome).append("\n");
        report.append("Total Expenses: $").append(totalExpense).append("\n");
        report.append("Net Balance: $").append(totalIncome - totalExpense).append("\n\n");

        report.append("Income by Category:\n");
        for (Category c : Category.values()) {
            if (c.isIncome()) {
                report.append("- ").append(c).append(": $").append(categoryIncome.getOrDefault(c, 0.0)).append("\n");
            }
        }

        report.append("\nExpenses by Category:\n");
        for (Category c : Category.values()) {
            if (c.isExpense()) {
                report.append("- ").append(c).append(": $").append(categoryExpenses.getOrDefault(c, 0.0)).append("\n");
            }
        }

        return report.toString();
    }
}

class SampleTransactions {
    public static void createSampleData(Account account) {
        account.addTransaction(new Income(5000.00, "Monthly Salary", "2023-03-15", Category.SALARY));
        account.addTransaction(new Income(1000.00, "Yearly Bonus", "2023-12-03", Category.BONUS));
        account.addTransaction(new Income(250.50, "Stock Dividends", "2023-05-07", Category.INVESTMENT));
        account.addTransaction(new Expense(150.75, "Grocery Shopping", "2023-01-10", Category.FOOD));
        account.addTransaction(new Expense(45.00, "Bus Pass", "2023-11-03", Category.TRANSPORT));
        account.addTransaction(new Expense(120.00, "Movie Tickets", "2023-05-25", Category.ENTERTAINMENT));
    }
}
