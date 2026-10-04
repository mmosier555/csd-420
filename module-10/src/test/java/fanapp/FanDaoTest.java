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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests FanDao against an in-memory H2 database (MySQL mode) with the same table layout. */
class FanDaoTest {

    private static final String URL = "jdbc:h2:mem:fandao;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private FanDao dao;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection c = DriverManager.getConnection(URL); Statement s = c.createStatement()) {
            s.execute("DROP TABLE IF EXISTS fans");
            s.execute("CREATE TABLE fans (ID INT PRIMARY KEY, firstname VARCHAR(25), "
                    + "lastname VARCHAR(25), favoriteteam VARCHAR(25))");
            s.execute("INSERT INTO fans VALUES (1, 'Megan', 'Mosier', 'Broncos')");
            s.execute("INSERT INTO fans VALUES (2, 'Nick', 'Mosier', 'Rockies')");
        }
        dao = new FanDao(() -> DriverManager.getConnection(URL));
    }

    @Test
    void findByIdReturnsExistingRecord() throws SQLException {
        Optional<Fan> fan = dao.findById(1);
        assertTrue(fan.isPresent());
        assertEquals(new Fan(1, "Megan", "Mosier", "Broncos"), fan.get());
    }

    @Test
    void findByIdReturnsEmptyForMissingRecord() throws SQLException {
        assertTrue(dao.findById(99).isEmpty());
    }

    @Test
    void updateChangesOnlyTargetRecord() throws SQLException {
        assertTrue(dao.update(new Fan(1, "Meg", "Smith", "Avalanche")));
        assertEquals(new Fan(1, "Meg", "Smith", "Avalanche"), dao.findById(1).orElseThrow());
        assertEquals(new Fan(2, "Nick", "Mosier", "Rockies"), dao.findById(2).orElseThrow());
    }

    @Test
    void updateReturnsFalseForMissingRecord() throws SQLException {
        assertFalse(dao.update(new Fan(99, "A", "B", "C")));
    }

    @Test
    void updateDoesNotDropOrCreateTable() throws SQLException {
        dao.update(new Fan(1, "X", "Y", "Z"));
        assertTrue(dao.findById(2).isPresent());
    }

    @Test
    void connectionDefaultsMatchAssignment() {
        assertEquals("jdbc:mysql://localhost:3306/databasedb", FanDao.URL);
        assertEquals("student1", FanDao.USER);
        assertEquals("pass", FanDao.PASSWORD);
    }
}