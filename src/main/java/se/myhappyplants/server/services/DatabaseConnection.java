package se.myhappyplants.server.services;

import se.myhappyplants.server.services.PasswordsAndKeys;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Properties;

/**
 * Class for handling connection with a specific database
 * Created by: Frida Jacobsson 2021-05-21
 */
public class DatabaseConnection implements IDatabaseConnection {

    private java.sql.Connection conn;
    private final String databaseName;

    public DatabaseConnection(String databaseName) {
        this.databaseName = databaseName;
    }

    private java.sql.Connection createConnection() throws SQLException, UnknownHostException {
        String dbServerIp = PasswordsAndKeys.dbServerIp;
        String dbServerPort = PasswordsAndKeys.dbServerPort;
        String dbUser = PasswordsAndKeys.dbUsername;
        String dbPassword = PasswordsAndKeys.dbPassword;
        DriverManager.registerDriver(new org.postgresql.Driver());// com.microsoft.sqlserver.jdbc.SQLServerDriver());

        if (InetAddress.getLocalHost().getHostName().equals(PasswordsAndKeys.dbHostName)) {
            dbServerIp = "localhost";
        }
        //String dbURL = String.format("jdbc:postgresql://%s:%s/databaseName=" + databaseName + "/user=%s;password=%s", dbServerIp, dbServerPort, dbUser, dbPassword);
        String dbURL = String.format("jdbc:postgresql://%s:%s/%s", dbServerIp, dbServerPort, databaseName);
        // Set properties for username and password
        Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", dbPassword);

        try {
            // Establish the connection
            this.conn = DriverManager.getConnection(dbURL, props);
            String[] tables = new String[] {"User", "User_plant"};
            checkAndCreateTable(tables);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //this.conn = DriverManager.getConnection(dbURL);
        return conn;
    }

    private void checkAndCreateTable(String[] tableNames) throws SQLException {
        for (int i = 0; i < tableNames.length; i++) {
            String checkTableExistsQuery = "SELECT to_regclass('public." + tableNames[i] + "')";
            try (Statement stmt = conn.createStatement();
                 ResultSet userRS = stmt.executeQuery(checkTableExistsQuery)) {
                if (!userRS.next() || userRS.getString(1) == null) {
                    // Table does not exist
                    createTable(tableNames[i]);
                }
            }
        }
    }

    private void createTable(String tableName) throws SQLException {
        String createTableSQL = "";
        switch (tableName) {
            case "User":
                createTableSQL = "CREATE TABLE IF NOT EXISTS public.User (" +
                                 "id SERIAL PRIMARY KEY, " +
                                 "email VARCHAR(255) UNIQUE, " +
                                 "username VARCHAR(255), " +
                                 "password VARCHAR(255), " +
                                 "avatar_url TEXT, " +
                                 "notification_activated BOOLEAN, " +
                                 "fun_facts_activated BOOLEAN)";
                break;
            case "User_plant":
                createTableSQL = "CREATE TABLE IF NOT EXISTS public.User_plant (" +
                                 "plant_id SERIAL PRIMARY KEY, " +
                                 "common_name VARCHAR(255), " +
                                 "scientific_name VARCHAR(255), " +
                                 "family_name VARCHAR(255), " +
                                 "image_url TEXT, " +
                                 "nickname VARCHAR(255), " +
                                 "last_watered DATE, " +
                                 "water_frequency BIGINT," +
                                 "user_id INTEGER, " +
                                 "FOREIGN KEY (user_id) REFERENCES public.user(id))";
        }
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public java.sql.Connection getConnection() {
        if(conn==null) {
            try {
                conn = createConnection();
            }
            catch (UnknownHostException e) {
                e.printStackTrace();

            }
            catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return conn;
    }

    @Override
    public void closeConnection() {
        try {
            conn.close();
        }
        catch (SQLException sqlException) {
           //do nothing when this occurs, we don't care about this exception
        }
        conn = null;
    }
}
