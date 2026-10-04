/*
 * Name: Megan Mosier
 * Date: October 4, 2026
 * Course: CSD-420
 * Assignment: Module 10 - Fan Database
 * Description: Record that holds one row of the "fans" table.
 */
package fanapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data access for the existing "fans" table. This class never creates or
 * deletes the table; it only reads and updates rows.
 */
public class FanDao {

    public static final String URL = "jdbc:mysql://localhost:3306/databasedb";
    public static final String USER = "student1";
    public static final String PASSWORD = "pass";

    @FunctionalInterface
    public interface ConnectionProvider {
        Connection get() throws SQLException;
    }

    private final ConnectionProvider provider;

    public FanDao() {
        this(() -> DriverManager.getConnection(URL, USER, PASSWORD));
    }

    public FanDao(ConnectionProvider provider) {
        this.provider = provider;
    }

    /** Returns the fan with the given ID, or empty if there is none. */
    public Optional<Fan> findById(int id) throws SQLException {
        String sql = "SELECT ID, firstname, lastname, favoriteteam FROM fans WHERE ID = ?";
        try (Connection c = provider.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Fan(rs.getInt("ID"), rs.getString("firstname"),
                            rs.getString("lastname"), rs.getString("favoriteteam")));
                }
                return Optional.empty();
            }
        }
    }

    /**
     * Updates the names and team of the fan with fan.id(). Returns true if a row
     * changed.
     */
    public boolean update(Fan fan) throws SQLException {
        String sql = "UPDATE fans SET firstname = ?, lastname = ?, favoriteteam = ? WHERE ID = ?";
        try (Connection c = provider.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fan.firstName());
            ps.setString(2, fan.lastName());
            ps.setString(3, fan.favoriteTeam());
            ps.setInt(4, fan.id());
            return ps.executeUpdate() > 0;
        }
    }
}
