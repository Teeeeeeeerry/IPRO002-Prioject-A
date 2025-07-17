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