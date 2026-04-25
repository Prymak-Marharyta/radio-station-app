package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Utility class for initializing the database.
 * 
 * Responsible for creating database tables and inserting
 * initial sample data for testing purposes.
 */
public class DatabaseInitializer {

    /**
     * Creates database tables if they do not exist and
     * inserts default data into the database.
     */
    public static void initDB() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Artists (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Tracks (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    artist_id INT NOT NULL,
                    FOREIGN KEY (artist_id) REFERENCES Artists(id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Airplay (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    track_id INT NOT NULL,
                    play_time TIMESTAMP NOT NULL,
                    duration INT NOT NULL,
                    FOREIGN KEY (track_id) REFERENCES Tracks(id)
                );
            """);

            insertDefaultArtists();

        } catch (Exception e) {
            throw new RuntimeException("Database initialization error", e);
        }
    }

    /**
     * Inserts default artists into the Artists table.
     * Uses MERGE to avoid duplicate entries.
     */
    private static void insertDefaultArtists() {
        String sql = """
            MERGE INTO Artists (id, name) KEY(id) VALUES
            (1, 'The Weeknd'),
            (2, 'Dua Lipa'),
            (3, 'Imagine Dragons');
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Default artists insert error", e);
        }
    }
}