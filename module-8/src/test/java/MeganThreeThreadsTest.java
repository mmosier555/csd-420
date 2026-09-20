import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class MeganThreeThreadsTest {

    @BeforeAll
    static void startToolkit() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException alreadyRunning) {
            // JavaFX was already started - fine
        }
    }

    // ---------- helpers ----------

    /** Runs code on the JavaFX thread and returns its result. */
    private static <T> T onFxThread(Supplier<T> supplier) throws Exception {
        CompletableFuture<T> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future.get(60, TimeUnit.SECONDS);
    }

    /** Waits for every thread to finish, failing the test if one gets stuck. */
    private static void joinAll(List<Thread> threads) throws InterruptedException {
        for (Thread t : threads) {
            t.join(60_000);
            assertFalse(t.isAlive(), "Thread " + t.getName() + " did not finish");
        }
    }

    /** Runs all three threads and returns every character they produced, in order. */
    private static String runAndCollect(int count, int delayMillis) throws InterruptedException {
        StringBuffer collected = new StringBuffer();   // StringBuffer is safe for many threads
        joinAll(MeganThreeThreads.startThreads(count, delayMillis, c -> collected.append(c)));
        return collected.toString();
    }

    /** 0 = letter, 1 = digit, 2 = symbol, -1 = anything else. */
    private static int kind(char c) {
        if (MeganThreeThreads.LETTERS.indexOf(c) >= 0) return 0;
        if (MeganThreeThreads.DIGITS.indexOf(c) >= 0) return 1;
        if (MeganThreeThreads.SYMBOLS.indexOf(c) >= 0) return 2;
        return -1;
    }

    /** Returns {letters, digits, symbols} counts for the text. */
    private static int[] countKinds(String text) {
        int[] counts = new int[3];
        for (char c : text.toCharArray()) {
            int k = kind(c);
            assertTrue(k >= 0, "Unexpected character: " + c);
            counts[k]++;
        }
        return counts;
    }

    /** How many times the type of character changes from one character to the next. */
    private static int countSwitches(String text) {
        int switches = 0;
        for (int i = 1; i < text.length(); i++) {
            if (kind(text.charAt(i)) != kind(text.charAt(i - 1))) {
                switches++;
            }
        }
        return switches;
    }

    // ---------- tests for the random methods ----------

    @Test
    void randomLetterReturnsOnlyLowercaseLetters() {
        for (int i = 0; i < 1000; i++) {
            char c = MeganThreeThreads.randomLetter();
            assertTrue(c >= 'a' && c <= 'z', "Not a lowercase letter: " + c);
        }
    }

    @Test
    void randomDigitReturnsOnlyDigits() {
        for (int i = 0; i < 1000; i++) {
            char c = MeganThreeThreads.randomDigit();
            assertTrue(c >= '0' && c <= '9', "Not a digit: " + c);
        }
    }

    @Test
    void randomSymbolReturnsOnlyAllowedSymbols() {
        for (int i = 0; i < 1000; i++) {
            char c = MeganThreeThreads.randomSymbol();
            assertTrue(MeganThreeThreads.SYMBOLS.indexOf(c) >= 0, "Not an allowed symbol: " + c);
        }
    }

    @Test
    void randomMethodsUseTheWholeSetOfCharacters() {
        Set<Character> letters = new HashSet<>();
        Set<Character> digits = new HashSet<>();
        Set<Character> symbols = new HashSet<>();
        for (int i = 0; i < 2000; i++) {
            letters.add(MeganThreeThreads.randomLetter());
            digits.add(MeganThreeThreads.randomDigit());
            symbols.add(MeganThreeThreads.randomSymbol());
        }
        assertEquals(26, letters.size());
        assertEquals(10, digits.size());
        assertEquals(MeganThreeThreads.SYMBOLS.length(), symbols.size());
    }

    // ---------- tests for the threads ----------

    @Test
    void startThreadsCreatesThreeNamedThreads() throws Exception {
        List<Thread> threads = MeganThreeThreads.startThreads(1, 0, c -> { });
        assertEquals(3, threads.size());
        assertEquals("Letters", threads.get(0).getName());
        assertEquals("Digits", threads.get(1).getName());
        assertEquals("Symbols", threads.get(2).getName());
        joinAll(threads);
    }

    @Test
    void createThreadWaitsForStartSignalThenWritesExactCount() throws Exception {
        StringBuffer collected = new StringBuffer();
        CountDownLatch startSignal = new CountDownLatch(1);
        Thread thread = MeganThreeThreads.createThread("Test", MeganThreeThreads::randomDigit,
                25, 0, startSignal, c -> collected.append(c));
        thread.start();

        Thread.sleep(100);
        assertEquals(0, collected.length(), "Thread should wait for the start signal");

        startSignal.countDown();
        thread.join(10_000);
        assertEquals(25, collected.length());
    }

    @Test
    void eachThreadWritesTenThousandCharacters() throws Exception {
        String text = runAndCollect(MeganThreeThreads.CHARACTER_COUNT, 0);
        int[] counts = countKinds(text);
        assertEquals(30_000, text.length());
        assertEquals(10_000, counts[0], "letters");
        assertEquals(10_000, counts[1], "digits");
        assertEquals(10_000, counts[2], "symbols");
    }

    @Test
    void charactersFromThreadsAreMixedTogether() throws Exception {
        String text = runAndCollect(200, 1);
        int switches = countSwitches(text);
        assertTrue(switches > 20, "Characters were not mixed. Type changes: " + switches);
    }

    // ---------- tests for the text area ----------

    @Test
    void createTextAreaIsReadOnlyAndEmpty() throws Exception {
        TextArea area = onFxThread(MeganThreeThreads::createTextArea);
        assertFalse(area.isEditable());
        assertTrue(area.isWrapText());
        assertEquals("", area.getText());
    }

    @Test
    void textAreaOutputAppendsEachCharacterInOrder() throws Exception {
        TextArea area = onFxThread(MeganThreeThreads::createTextArea);
        MeganThreeThreads.textAreaOutput(area).accept('a');
        MeganThreeThreads.textAreaOutput(area).accept('1');
        MeganThreeThreads.textAreaOutput(area).accept('#');
        assertEquals("a1#", onFxThread(area::getText));
    }

    @Test
    void allThirtyThousandCharactersReachTheTextArea() throws Exception {
        TextArea area = onFxThread(MeganThreeThreads::createTextArea);
        joinAll(MeganThreeThreads.startThreads(MeganThreeThreads.CHARACTER_COUNT, 0,
                MeganThreeThreads.textAreaOutput(area)));
        String text = onFxThread(area::getText);
        int[] counts = countKinds(text);
        assertEquals(30_000, text.length());
        assertEquals(10_000, counts[0]);
        assertEquals(10_000, counts[1]);
        assertEquals(10_000, counts[2]);
    }
}
