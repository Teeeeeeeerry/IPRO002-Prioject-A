import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProjectB extends Application {

    @Override
    public void start(Stage primaryStage) {
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(controller, model, primaryStage);

        primaryStage.setTitle("Finance Manager");
        primaryStage.setScene(new Scene(view.asParent(), 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
