import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// entry point, connect to MVC: Model → Controller(model) → View(controller, model).
// no business logic here.
public class ProjectB extends Application {

    @Override
    public void start(Stage primaryStage) {
        // create Model
        Model model = new Model();
        // create Controller with Model (no UI code here)
        Controller controller = new Controller(model);
        // create View with Controller + Model (View handles UI only)
        View view = new View(controller, model, primaryStage);

        // Show primary stage: UI now observes Model via bindings/ObservableList

        primaryStage.setTitle("Finance Manager");
        primaryStage.setScene(new Scene(view.asParent(), 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
