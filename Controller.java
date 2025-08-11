public class Controller {
    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void addTransaction(Transaction transaction) {
        model.addTransaction(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        model.removeTransaction(transaction);
    }

    public void sortTransactions() {
        model.sortTransactions();
    }

    public String generateReport() {
        return model.generateReport();
    }

    public javafx.collections.ObservableList<Transaction> getTransactions() {
        return model.getTransactions();
    }
}
