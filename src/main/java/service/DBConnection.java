package service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Utility class for managing database connection.
 * Establishes a connection to the H2 database using parameters
 * from the db.properties configuration file.
 */
public class DBConnection {

    private static Connection connection;

    /**
     * Returns a singleton database connection.
     *
     * @return Connection to the database
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                Properties props = new Properties();

                InputStream input = DBConnection.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties");

                props.load(input);

                connection = DriverManager.getConnection(
                        props.getProperty("db.url"),
                        props.getProperty("db.user"),
                        props.getProperty("db.password")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}