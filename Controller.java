public class Controller {
    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    // MVC Step 2 (Controller): validate/convert input; orchestrate Model call
    public void addTransaction(Transaction transaction) {
        // MVC Step 3 (Model): mutate domain state
        model.addTransaction(transaction);
    }

    // MVC Step 2 (Controller): validate/convert input; orchestrate Model call
    public void removeTransaction(Transaction transaction) {
        // MVC Step 3 (Model): mutate domain state
        model.removeTransaction(transaction);
    }

    // MVC Step 2 (Controller): command Model to sort
    public void sortTransactions() {
        model.sortTransactions();
    }

    // MVC Step 2 (Controller): query Model; no UI code here
    public String generateReport() {
        return model.generateReport();
    }

    // Expose Model's observable list to View (binding source)
    public javafx.collections.ObservableList<Transaction> getTransactions() {
        return model.getTransactions();
    }
}
