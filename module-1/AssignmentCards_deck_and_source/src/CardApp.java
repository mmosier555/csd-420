import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CardApp
 *
 * Displays four images randomly selected from a 52-card deck.
 * A "Refresh" button below the cards selects and displays four new
 * random cards. Card images are loaded from the "cards" subdirectory
 * (1.png through 52.png) located alongside the compiled classes. Created own 
 * images since I could not get the zip images to load.
 *
 * Author: Megan Mosier
 * Course: CSD-420
 */
public class CardApp extends Application {

    private static final int DECK_SIZE = 52;
    private static final int CARDS_TO_SHOW = 4;
    private static final int IMAGE_WIDTH = 120;

    // Four ImageViews that always stay on screen; refresh just swaps their images.
    private final ImageView[] cardViews = new ImageView[CARDS_TO_SHOW];

    @Override
    public void start(Stage primaryStage) {
        HBox cardRow = new HBox(15);
        cardRow.setAlignment(Pos.CENTER);

        for (int i = 0; i < CARDS_TO_SHOW; i++) {
            ImageView iv = new ImageView();
            iv.setFitWidth(IMAGE_WIDTH);
            iv.setPreserveRatio(true);
            cardViews[i] = iv;
            cardRow.getChildren().add(iv);
        }

        Button refreshButton = new Button("Refresh");
        // Lambda expression for the button's event handler.
        refreshButton.setOnAction(event -> drawRandomCards());

        VBox root = new VBox(20, cardRow, refreshButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        // Initial deal.
        drawRandomCards();

        Scene scene = new Scene(root, 620, 420);
        primaryStage.setTitle("Random Card Draw - CSD-420");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Picks four unique random card numbers from the deck (1-52) and
     * updates each ImageView with the corresponding image.
     * Uses lambda expressions to build the shuffled deck and to load images.
     */
    private void drawRandomCards() {
        List<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= DECK_SIZE; i++) {
            deck.add(i);
        }
        Collections.shuffle(deck);

        // Lambda expression: map each of the first four shuffled deck numbers
        // to its image file and apply it to the matching ImageView.
        List<Integer> chosen = deck.subList(0, CARDS_TO_SHOW);
        for (int i = 0; i < CARDS_TO_SHOW; i++) {
            final int index = i;
            java.util.function.Function<Integer, Image> loadImage =
                    cardNumber -> new Image(
                            getClass().getResourceAsStream("cards/" + cardNumber + ".png")
                    );
            cardViews[index].setImage(loadImage.apply(chosen.get(index)));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
