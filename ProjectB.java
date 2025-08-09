import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class ProjectB extends Application {
    private Model model;
    private View view;
    private Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        model = new Model();
        controller = new Controller(model);
        view = new View(controller, model, primaryStage);
        
        primaryStage.setTitle("Finance Manager");
        primaryStage.setScene(new Scene(view.asParent(), 800, 600));
        primaryStage.show();
    }

    // Model class
    static class Model {
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

    // View class
    static class View {
        private VBox view;
        private TableView<Transaction> transactionTable;
        private Controller controller;
        private Model model;
        private Stage primaryStage;

        public View(Controller controller, Model model, Stage primaryStage) {
            this.controller = controller;
            this.model = model;
            this.primaryStage = primaryStage;
            createAndConfigurePane();
            createAndLayoutControls();
        }

        public Parent asParent() {
            return view;
        }

        private void createAndLayoutControls() {
            // Create table columns
            TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
            typeCol.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue() instanceof Income ? "Income" : "Expense"));

            TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
            amountCol.setCellValueFactory(cellData -> 
                new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());

            TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
            descCol.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getDescription()));

            TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getDate()));

            TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
            categoryCol.setCellValueFactory(cellData -> {
                if (cellData.getValue() instanceof Income) {
                    return new SimpleStringProperty(((Income)cellData.getValue()).getCategory().name());
                } else {
                    return new SimpleStringProperty(((Expense)cellData.getValue()).getCategory().name());
                }
            });

            transactionTable = new TableView<>();
            transactionTable.getColumns().addAll(typeCol, amountCol, descCol, dateCol, categoryCol);
            transactionTable.setItems(model.getTransactions());

            // Create buttons
            Button addIncomeBtn = new Button("Add Income");
            addIncomeBtn.setOnAction(e -> createTransactionForm(true));

            Button addExpenseBtn = new Button("Add Expense");
            addExpenseBtn.setOnAction(e -> createTransactionForm(false));

            Button deleteBtn = new Button("Delete Selected");
            deleteBtn.setOnAction(e -> {
                Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    controller.removeTransaction(selected);
                }
            });

            Button sortBtn = new Button("Sort by Date");
            sortBtn.setOnAction(e -> controller.sortTransactions());

            Button reportBtn = new Button("Generate Report");
            reportBtn.setOnAction(e -> showReport());

            HBox buttonRow = new HBox(10, addIncomeBtn, addExpenseBtn, deleteBtn, sortBtn, reportBtn);
            buttonRow.setAlignment(Pos.CENTER);
            buttonRow.setPadding(new Insets(10));

            view.getChildren().addAll(transactionTable, buttonRow);
        }

        private void createAndConfigurePane() {
            view = new VBox(10);
            view.setPadding(new Insets(10));
        }

        private void createTransactionForm(boolean isIncome) {
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(isIncome ? "Add Income" : "Add Expense");

            TextField amountField = new TextField();
            amountField.setPromptText("Amount");

            TextField descField = new TextField();
            descField.setPromptText("Description");

            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText("Date");

            ComboBox<Category> categoryCombo = new ComboBox<>();
            categoryCombo.getItems().addAll(Category.values());
            categoryCombo.setCellFactory(param -> new ListCell<Category>() {
                @Override
                protected void updateItem(Category item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.name());
                    }
                }
            });
            categoryCombo.setButtonCell(new ListCell<Category>() {
                @Override
                protected void updateItem(Category item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.name());
                    }
                }
            });

            // Filter categories based on income/expense
            categoryCombo.getItems().clear();
            for (Category c : Category.values()) {
                if ((isIncome && c.isIncome()) || (!isIncome && c.isExpense())) {
                    categoryCombo.getItems().add(c);
                }
            }

            Button submitBtn = new Button("Submit");
            submitBtn.setOnAction(e -> {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    String description = descField.getText();
                    String date = datePicker.getValue() != null ? 
                        datePicker.getValue().toString() : new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    Category category = categoryCombo.getValue();

                    if (description.isEmpty() || category == null) {
                        throw new IllegalArgumentException("Please fill all fields");
                    }

                    Transaction transaction;
                    if (isIncome) {
                        transaction = new Income(amount, description, date, category);
                    } else {
                        transaction = new Expense(amount, description, date, category);
                    }

                    controller.addTransaction(transaction);
                    stage.close();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            });

            Button cancelBtn = new Button("Cancel");
            cancelBtn.setOnAction(e -> stage.close());

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            grid.add(new Label("Amount:"), 0, 0);
            grid.add(amountField, 1, 0);
            grid.add(new Label("Description:"), 0, 1);
            grid.add(descField, 1, 1);
            grid.add(new Label("Date:"), 0, 2);
            grid.add(datePicker, 1, 2);
            grid.add(new Label("Category:"), 0, 3);
            grid.add(categoryCombo, 1, 3);

            HBox buttons = new HBox(10, submitBtn, cancelBtn);
            buttons.setAlignment(Pos.CENTER);

            VBox root = new VBox(10, grid, buttons);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(10));

            stage.setScene(new Scene(root));
            stage.show();
        }

        private void showReport() {
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Financial Report");

            TextArea reportArea = new TextArea(controller.generateReport());
            reportArea.setEditable(false);
            reportArea.setWrapText(true);

            Button closeBtn = new Button("Close");
            closeBtn.setOnAction(e -> stage.close());

            VBox root = new VBox(10, reportArea, closeBtn);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(10));

            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        }
    }

    // Controller class
    static class Controller {
        private Model model;

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
    }

    // Below are all the classes from Project A with minor adjustments

    static class Account {
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

    static abstract class Transaction {
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

        private int getYear() {
            return Integer.parseInt(date.split("-")[2]);
        }

        private int getMonth() {
            return Integer.parseInt(date.split("-")[1]);
        }

        private int getDay() {
            return Integer.parseInt(date.split("-")[0]);
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

    static class Income extends Transaction {
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

    static class Expense extends Transaction {
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

    static class ReportGenerator {
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
                    Category cat = ((Income)t).getCategory();
                    categoryIncome.put(cat, categoryIncome.getOrDefault(cat, 0.0) + t.getAmount());
                } else if (t instanceof Expense) {
                    totalExpense += t.getAmount();
                    Category cat = ((Expense)t).getCategory();
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

    static class SampleTransactions {
        public static void createSampleData(Account account) {
            account.addTransaction(new Income(5000.00, "Monthly Salary", "15-03-2023", Category.SALARY));
            account.addTransaction(new Income(1000.00, "Yearly Bonus", "20-12-2023", Category.BONUS));
            account.addTransaction(new Income(250.50, "Stock Dividends", "05-07-2023", Category.INVESTMENT));
            account.addTransaction(new Expense(150.75, "Grocery Shopping", "10-01-2023", Category.FOOD));
            account.addTransaction(new Expense(45.00, "Bus Pass", "03-11-2023", Category.TRANSPORT));
            account.addTransaction(new Expense(120.00, "Movie Tickets", "25-05-2023", Category.ENTERTAINMENT));
        }
    }
}
