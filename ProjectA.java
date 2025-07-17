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

}

   