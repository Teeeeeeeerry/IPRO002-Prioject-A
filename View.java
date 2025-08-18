import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class View {
    private VBox view;
    private TableView<Transaction> transactionTable;
    private final Controller controller;
    private final Model model;
    private final Stage primaryStage;

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

    private void createAndConfigurePane() {
        view = new VBox(10);
        view.setPadding(new Insets(10));
    }

    private void createAndLayoutControls() {
        //Type
        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue() instanceof Income ? "Income" : "Expense"));

        //Amount
        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleDoubleProperty(c.getValue().getAmount()).asObject());

        //Description
        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDescription()));

        //Date
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDate()));

        //Category（Income/Expense）
        TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Income) {
                return new javafx.beans.property.SimpleStringProperty(((Income) cellData.getValue()).getCategory().name());
            } else {
                return new javafx.beans.property.SimpleStringProperty(((Expense) cellData.getValue()).getCategory().name());
            }
        });

        transactionTable = new TableView<>();
        transactionTable.getColumns().addAll(typeCol, amountCol, descCol, dateCol, categoryCol);
        transactionTable.setItems(controller.getTransactions());

    
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

    private void createTransactionForm(boolean isIncome) {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(isIncome ? "Add Income" : "Add Expense");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        TextField descField = new TextField();
        descField.setPromptText("Description");

        TextField dateField = new TextField();
        dateField.setPromptText("DD-MM-YYYY");

        ComboBox<Category> categoryCombo = new ComboBox<>();

        for (Category c : Category.values()) {
            if ((isIncome && c.isIncome()) || (!isIncome && c.isExpense())) {
                categoryCombo.getItems().add(c);
            }
        }
        categoryCombo.setCellFactory(param -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.name());
            }
        });
        categoryCombo.setButtonCell(new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.name());
            }
        });

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String description = descField.getText();
                String dateInput = dateField.getText().trim();

                //dd-MM-yyyy
                SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY");
                sdf.setLenient(false);
                String date;
                try {
                    Date parsed = sdf.parse(dateInput);
                    date = sdf.format(parsed);
                } catch (ParseException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Date");
                    alert.setHeaderText(null);
                    alert.setContentText("Format incorrect, correct format is: DD-MM-YYYY");
                    alert.showAndWait();
                    return;
                }

                Category category = categoryCombo.getValue();
                if (description.isEmpty() || category == null) {
                    throw new IllegalArgumentException("Please fill all fields");
                }

                Transaction transaction = isIncome
                        ? new Income(amount, description, date, category)
                        : new Expense(amount, description, date, category);

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
        grid.add(dateField, 1, 2);
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
