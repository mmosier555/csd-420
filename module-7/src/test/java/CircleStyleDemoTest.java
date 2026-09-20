import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.*;

class CircleStyleDemoTest {

    private Scene scene;
    private HBox root;
    private List<Circle> circles;

    @BeforeAll
    static void startToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException alreadyRunning) {
            // toolkit already started by anther test class - fine
        }
    }

    /** Runs code on the JavaFX Application Thread and returns its result. */
    private static <T> T onFxThread(Supplier<T> supplier) throws Exception {
        CompletableFuture<T> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future.get(10, TimeUnit.SECONDS);
    }

    @BeforeEach
    void buildScene() throws Exception {
        onFxThread(() -> {
            scene = CircleStyleDemo.createScene();
            root = (HBox) scene.getRoot();
            root.applyCss();          // resolve the style sheet without showing a window
            root.layout();
            circles = root.getChildren().stream()
                    .map(n -> (Circle) n)
                    .toList();
            return null;
        });
    }
    @Test
    void stylesheetExistsAndIsAttached() {
        assertNotNull(CircleStyleDemo.class.getResource("/mystyle.css"));
        assertEquals(1, scene.getStylesheets().size());
        assertTrue(scene.getStylesheets().get(0).endsWith("mystyle.css"));
    }

    @Test
    void fourCirclesAreDisplayed() {
        assertEquals(4, root.getChildren().size());
        root.getChildren().forEach(n -> assertInstanceOf(Circle.class, n));
    }

    @Test
    void firstTwoCirclesUseTheStyleClass() {
        assertTrue(circles.get(0).getStyleClass().contains("plaincircle"));
        assertTrue(circles.get(1).getStyleClass().contains("plaincircle"));
    }

    @Test
    void styleClassGivesWhiteFillAndBlackStroke() {
        for (int i = 0; i < 2; i++) {
            assertEquals(Color.WHITE, circles.get(i).getFill(), "fill of circle " + (i + 1));
            assertEquals(Color.BLACK, circles.get(i).getStroke(), "stroke of circle " + (i + 1));
        }
    }

    @Test
    void lastTwoCirclesUseIds() {
        assertEquals("redcircle", circles.get(2).getId());
        assertEquals("greencircle", circles.get(3).getId());
    }

    @Test
    void idsGiveRedAndGreenFill() {
        assertEquals(Color.RED, circles.get(2).getFill());
        assertEquals(Color.GREEN, circles.get(3).getFill());
    }

    @Test
    void idsAreUniqueInTheScene() {
        Set<Node> reds = root.lookupAll("#redcircle");
        Set<Node> greens = root.lookupAll("#greencircle");
        assertEquals(1, reds.size());
        assertEquals(1, greens.size());
    }

    @Test
    void styleClassSelectorMatchesExactlyTwoCircles() {
        assertEquals(2, root.lookupAll(".plaincircle").size());
    }

}