/*
 * Name: Megan Mosier
 * Date: October 4, 2026
 * Course: CSD-420
 * Assignment: Module 10 - Fan Database
 * Description: Record that holds one row of the "fans" table.
 */
package fanapp;

import java.sql.SQLException;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** The user interface: ID field, three editable fields, Display and Update buttons. */
public class FanPane extends VBox {

    static final int MAX_LEN = 25;

    final TextField idField = new TextField();
    final TextField firstNameField = new TextField();
    final TextField lastNameField = new TextField();
    final TextField teamField = new TextField();
    final Button displayButton = new Button("Display");
    final Button updateButton = new Button("Update");
    final Label statusLabel = new Label(" ");

    private final FanDao dao;

    public FanPane(FanDao dao) {
        this.dao = dao;

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.addRow(0, new Label("ID:"), idField);
        grid.addRow(1, new Label("First name:"), firstNameField);
        grid.addRow(2, new Label("Last name:"), lastNameField);
        grid.addRow(3, new Label("Favorite team:"), teamField);

        HBox buttons = new HBox(10, displayButton, updateButton);
        buttons.setAlignment(Pos.CENTER_LEFT);

        setSpacing(12);
        setPadding(new Insets(15));
        getChildren().addAll(grid, buttons, statusLabel);

        displayButton.setOnAction(e -> display());
        updateButton.setOnAction(e -> update());
    }
    /** Looks up the ID in the ID field and shows that record. */
    void display() {
        Integer id = parseId();
        if (id == null) {
            return;
        }
        try {
            Optional<Fan> fan = dao.findById(id);
            if (fan.isPresent()) {
                firstNameField.setText(fan.get().firstName());
                lastNameField.setText(fan.get().lastName());
                teamField.setText(fan.get().favoriteTeam());
                statusLabel.setText("Record " + id + " displayed.");
            } else {
                clearFields();
                statusLabel.setText("No record found with ID " + id + ".");
            }
        } catch (SQLException ex) {
            statusLabel.setText("Database error: " + ex.getMessage());
        }
    }
        /** Saves the values currently shown to the record with the ID in the ID field. */
        void update() {
            Integer id = parseId();
            if (id == null) {
                return;
            }
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            String team = teamField.getText().trim();
            if (first.length() > MAX_LEN || last.length() > MAX_LEN || team.length() > MAX_LEN) {
                statusLabel.setText("Each field may be at most " + MAX_LEN + " characters.");
                return;
            }
            try {
                boolean changed = dao.update(new Fan(id, first, last, team));
                statusLabel.setText(changed ? "Record " + id + " updated."
                        : "No record found with ID " + id + "; nothing updated.");
            } catch (SQLException ex) {
                statusLabel.setText("Database error: " + ex.getMessage());
            }
        }
    
        private Integer parseId() {
            try {
                return Integer.parseInt(idField.getText().trim());
            } catch (NumberFormatException ex) {
                statusLabel.setText("Please enter a whole-number ID.");
                return null;
            }
        }
    
        private void clearFields() {
            firstNameField.clear();
            lastNameField.clear();
            teamField.clear();
        }
    }