import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Objects;
/**
 * Megan Mosier
 * CSD-420
 * Module 7.2
 * 
 * Displays four circles styled by the external style sheet mystyle.css.
 *  - Circle 1: style class "plaincircle"      (white fill, black stroke)
 *  - Circle 2: style classes "plaincircle" + "circleborder"
 *  - Circle 3: ID "redcircle"                 (red fill)
 *  - Circle 4: ID "greencircle"             
 */
public class CircleStyleDemo extends Application {
 
/** Builds pane holding the four circles (no styling applied yet) */
public static HBox createContent() {
    Circle circle1 = new Circle(50);
    circle1.getStyleClass().add("plaincircle");

    Circle circle2 = new Circle(50);
    circle2.getStyleClass().addAll("plaincircle", "circleborder");

    Circle circle3 = new Circle(50);
    circle3.setId("redcircle");

    Circle circle4 = new Circle(50);
    circle4.setID("greencircle");

    HBox pane = new HBox(20, circle1, circle2, circle3, circle4);
    pane.setAlignment(pos.Center);
    pane.setPadding(new Insets(20));
    return pane;
}

/** Builds the scene and attaches the external style sheet. */
public static Scene createScene() {
    Scene scene = new Scene(createContent());
    String css = Objects.requireNonNull(
        CircleStyleDemo.class.getResource(STYLESHEET),
        "Could not find " + STYLESHEET).toExternalForm();
    scene.getStylesheets().add(css);
    return scene;
}

@Override
public void start(Stage primaryStage) {
    primaryStage.setTitle("Circle Style Demo");
    primaryStage.setScene(createScene())
    primaryStage.show();
}

public static void main(String[] args) {
    launch(args);
}
}