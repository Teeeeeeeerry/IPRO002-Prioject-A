import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        dateField.setPromptText("YYYY-MM-DD");

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

        // repleace Alert
        Label msgLabel = new Label();
        msgLabel.setWrapText(true);
        msgLabel.getStyleClass().add("form-msg");
        msgLabel.setVisible(false);
        msgLabel.setManaged(false);

        Runnable clearMsg = () -> {
            msgLabel.setText("");
            msgLabel.setVisible(false);
            msgLabel.setManaged(false);
            msgLabel.getStyleClass().remove("error");
            msgLabel.getStyleClass().remove("info");
        };
        java.util.function.Consumer<String> showError = (text) -> {
            msgLabel.setText(text);
            if (!msgLabel.getStyleClass().contains("error")) msgLabel.getStyleClass().add("error");
            msgLabel.getStyleClass().remove("info");
            msgLabel.setVisible(true);
            msgLabel.setManaged(true);
        };

        // text-base alert
        amountField.setOnKeyTyped(e -> clearMsg.run());
        descField.setOnKeyTyped(e -> clearMsg.run());
        dateField.setOnKeyTyped(e -> clearMsg.run());
        categoryCombo.valueProperty().addListener((obs, o, n) -> clearMsg.run());

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String description = descField.getText();
                String dateInput = dateField.getText().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
                sdf.setLenient(false);
                String date;
                try {
                    Date parsed = sdf.parse(dateInput);
                    date = sdf.format(parsed);
                } catch (ParseException ex) {
                    showError.accept("Format incorrect, correct format is: YYYY-MM-DD");
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
                showError.accept("Invalid Input");
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> stage.close());

        // new Pane layout
        Pane grid = new Pane();
        grid.setPadding(new Insets(20));

        // Row 0
        Label lblAmount = new Label("Amount:");
        lblAmount.setLayoutX(0);
        lblAmount.setLayoutY(0);
        amountField.setLayoutX(100);
        amountField.setLayoutY(0);

        // Row 1
        Label lblDesc = new Label("Description:");
        lblDesc.setLayoutX(0);
        lblDesc.setLayoutY(35);
        descField.setLayoutX(100);
        descField.setLayoutY(35);

        // Row 2
        Label lblDate = new Label("Date:");
        lblDate.setLayoutX(0);
        lblDate.setLayoutY(70);
        dateField.setLayoutX(100);
        dateField.setLayoutY(70);

        // Row 3
        Label lblCategory = new Label("Category:");
        lblCategory.setLayoutX(0);
        lblCategory.setLayoutY(105);
        categoryCombo.setLayoutX(100);
        categoryCombo.setLayoutY(105);

        grid.getChildren().addAll(
            lblAmount, amountField,
            lblDesc,   descField,
            lblDate,   dateField,
            lblCategory, categoryCombo
        );

        HBox buttons = new HBox(10, submitBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, grid, msgLabel, buttons);
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
