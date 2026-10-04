/*
 * Name: Megan Mosier
 * Date: October 4, 2026
 * Course: CSD-420
 * Assignment: Module 10 - Fan Database
 * Description: Record that holds one row of the "fans" table.
 */
package fanapp;

/**
 * Simple value object representing one row of the "fans" table.
 */
public record Fan(int id, String firstName, String lastName, String favoriteTeam) {
}