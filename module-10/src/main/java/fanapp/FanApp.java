package fanapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** JavaFX entry point. */
public class FanApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Fan Information");
        stage.setScene(new Scene(new FanPane(new FanDao()), 380, 260));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}