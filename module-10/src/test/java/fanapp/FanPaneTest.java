/*
 * Name: Megan Mosier
 * Date: October 4, 2026
 * Course: CSD-420
 * Assignment: Module 10 - Fan Database
 * Description: Record that holds one row of the "fans" table.
 */
package fanapp;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests the interface: the Display and Update buttons and their messages. */
class FanPaneTest {

    private static final String URL = "jdbc:h2:mem:fanpane;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private FanDao dao;
    private FanPane pane;

    @BeforeAll
    static void startToolkit() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await();
        } catch (IllegalStateException alreadyRunning) {
            // toolkit already started
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        try (Connection c = DriverManager.getConnection(URL); Statement s = c.createStatement()) {
            s.execute("DROP TABLE IF EXISTS fans");
            s.execute("CREATE TABLE fans (ID INT PRIMARY KEY, firstname VARCHAR(25), "
                    + "lastname VARCHAR(25), favoriteteam VARCHAR(25))");
            s.execute("INSERT INTO fans VALUES (1, 'Megan', 'Mosier', 'Cowboys')");
        }
        dao = new FanDao(() -> DriverManager.getConnection(URL));
        runFx(() -> pane = new FanPane(dao));
    }

    private static void runFx(Runnable r) throws InterruptedException {
        CountDownLatch done = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                r.run();
            } finally {
                done.countDown();
            }
        });
        done.await();
    }

    @Test
    void hasTwoButtonsLabeledDisplayAndUpdate() {
        assertEquals("Display", pane.displayButton.getText());
        assertEquals("Update", pane.updateButton.getText());
    }

    @Test
    void displayButtonShowsRecord() throws Exception {
        runFx(() -> {
            pane.idField.setText("1");
            pane.displayButton.fire();
        });
        assertEquals("Megan", pane.firstNameField.getText());
        assertEquals("Mosier", pane.lastNameField.getText());
        assertEquals("Cowboys", pane.teamField.getText());
    }

    @Test
    void displayMissingIdClearsFieldsAndReports() throws Exception {
        runFx(() -> {
            pane.idField.setText("1");
            pane.displayButton.fire();
            pane.idField.setText("42");
            pane.displayButton.fire();
        });
        assertEquals("", pane.firstNameField.getText());
        assertTrue(pane.statusLabel.getText().contains("No record"));
    }

    @Test
    void displayRejectsNonNumericId() throws Exception {
        runFx(() -> {
            pane.idField.setText("abc");
            pane.displayButton.fire();
        });
        assertTrue(pane.statusLabel.getText().contains("whole-number"));
    }

    @Test
    void updateButtonSavesEditedValues() throws Exception {
        runFx(() -> {
            pane.idField.setText("1");
            pane.displayButton.fire();
            pane.firstNameField.setText("Meg");
            pane.teamField.setText("Mammoth");
            pane.updateButton.fire();
        });
        Fan saved = dao.findById(1).orElseThrow();
        assertEquals("Meg", saved.firstName());
        assertEquals("Mosier", saved.lastName());
        assertEquals("Mammoth", saved.favoriteTeam());
        assertTrue(pane.statusLabel.getText().contains("updated"));
    }

    @Test
    void updateMissingIdChangesNothing() throws Exception {
        runFx(() -> {
            pane.idField.setText("77");
            pane.firstNameField.setText("Z");
            pane.updateButton.fire();
        });
        assertTrue(pane.statusLabel.getText().contains("nothing updated"));
    }

    @Test
    void updateRejectsTooLongValue() throws Exception {
        runFx(() -> {
            pane.idField.setText("1");
            pane.firstNameField.setText("x".repeat(26));
            pane.updateButton.fire();
        });
        assertTrue(pane.statusLabel.getText().contains("25"));
        assertEquals("Megan", dao.findById(1).orElseThrow().firstName());
    }
}