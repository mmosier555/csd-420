/*
 * Name:        Megan Mosier
 * Course:      CSD-420
 * Assignment:  Module 8 - MeganThreeThreads
 * Date:        September 20, 2026
 *
 * Purpose: Starts three threads that each write random characters to a
 *          TextArea as soon as they are generated:
 *            Thread 1 (Letters) - random lowercase letters a-z
 *            Thread 2 (Digits)  - random digits 0-9
 *            Thread 3 (Symbols) - random symbols  ! @ # $ % ^ & *
 *          Each thread writes 10,000 characters (30,000 in total), so the
 *          three kinds of characters end up mixed together on screen.
 */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MeganThreeThreads extends Application {

    /** How many characters each thread writes. */
    public static final int CHARACTER_COUNT = 10_000;

    /** Pause (in milliseconds) after each character so the threads mix and the text can be watched. */
    public static final int DELAY_MILLIS = 1;

    // The characters each thread may choose from.
    static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    static final String DIGITS = "0123456789";
    static final String SYMBOLS = "!@#$%^&*";

    /** Returns a random lowercase letter. */
    public static char randomLetter() {
        return pick(LETTERS);
    }

    /** Returns a random digit character, 0 through 9. */
    public static char randomDigit() {
        return pick(DIGITS);
    }

    /** Returns a random symbol from the SYMBOLS list. */
    public static char randomSymbol() {
        return pick(SYMBOLS);
    }

    /** Picks one random character out of the given text. */
    private static char pick(String choices) {
        return choices.charAt(ThreadLocalRandom.current().nextInt(choices.length()));
    }

    /**
     * Builds (but does not start) one worker thread.
     *
     * @param name        a label for the thread
     * @param generator   hands back one new random character each time it is called
     * @param count       how many characters to produce
     * @param delayMillis pause after each character (0 = just yield to the other threads)
     * @param startSignal the thread waits for this "starting gun" before it begins
     * @param output      receives each character as soon as it is generated
     */
    static Thread createThread(String name, Supplier<Character> generator, int count,
                               int delayMillis, CountDownLatch startSignal,
                               Consumer<Character> output) {
        Thread thread = new Thread(() -> {
            try {
                startSignal.await();
                for (int i = 0; i < count; i++) {
                    output.accept(generator.get());
                    if (delayMillis > 0) {
                        Thread.sleep(delayMillis);
                    } else {
                        Thread.yield();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, name);
        thread.setDaemon(true);   // lets the program exit when the window is closed
        return thread;
    }

    /** Creates the three threads, starts them, then fires the starting gun. */
    public static List<Thread> startThreads(int count, int delayMillis, Consumer<Character> output) {
        CountDownLatch startSignal = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();
        threads.add(createThread("Letters", MeganThreeThreads::randomLetter, count, delayMillis, startSignal, output));
        threads.add(createThread("Digits", MeganThreeThreads::randomDigit, count, delayMillis, startSignal, output));
        threads.add(createThread("Symbols", MeganThreeThreads::randomSymbol, count, delayMillis, startSignal, output));
        for (Thread thread : threads) {
            thread.start();
        }
        startSignal.countDown();
        return threads;
    }

    /** Creates the read-only text area the characters are shown in. */
    static TextArea createTextArea() {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setWrapText(true);
        return area;
    }

    /** Returns an "output" that adds each character to the text area on the JavaFX thread. */
    static Consumer<Character> textAreaOutput(TextArea area) {
        return c -> Platform.runLater(() -> area.appendText(String.valueOf(c)));
    }

    @Override
    public void start(Stage primaryStage) {
        TextArea area = createTextArea();
        BorderPane root = new BorderPane(area);
        root.setPadding(new Insets(10));

        primaryStage.setTitle("MeganThreeThreads");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();

        startThreads(CHARACTER_COUNT, DELAY_MILLIS, textAreaOutput(area));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
