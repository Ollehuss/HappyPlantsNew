package se.myhappyplants.server.services;

import org.mindrot.jbcrypt.BCrypt;
import se.myhappyplants.shared.User;

import java.net.UnknownHostException;
import java.sql.*;

/**
 * Class responsible for calling the database about users.
 * Created by: Frida Jacobsson 2021-03-30
 * Updated by: Frida Jacobsson 2021-05-21
 */
public class UserRepository {

    private IQueryExecutor database;

    public UserRepository(IQueryExecutor database){
        this.database = database;
    }

    /**
     * Method to save a new user using BCrypt.
     *
     * @param user An instance of a newly created User that should be stored in the database.
     * @return A boolean value, true if the user was stored successfully
     */
    public boolean saveUser(User user) {
        boolean success = false;
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String sqlSafeUsername = user.getUsername().replace("'", "''");
        String query = "INSERT INTO public.user (username, email, password, notification_activated) VALUES ('" + sqlSafeUsername + "', '" + user.getEmail() + "', '" + hashedPassword + "','" + 1 + "');";

        try {
            database.executeUpdate(query);
            //database.executeUpdate(sqlSafeUsername, user.getEmail(), hashedPassword, 1, 1);
            success = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return success;
    }

    /**
     * Method to check if a user exists in database.
     * Purpose of method is to make it possible for user to log in
     *
     * @param email    typed email from client and the application
     * @param password typed password from client and the application
     * @return A boolean value, true if the user exist in database and the password is correct
     */
    public boolean checkLogin(String email, String password) {
        boolean isVerified = false;
        String query = "SELECT password FROM public.user WHERE email = '" + email + "';";
        try {
            ResultSet resultSet = database.executeQuery(query);
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString(1);
                isVerified = BCrypt.checkpw(password, hashedPassword);
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return isVerified;
    }

    /**
     * Method to get information (id, username and notification status) about a specific user
     *
     * @param email ??
     * @return a new instance of USer
     */
    public User getUserDetails(String email) {
        User user = null;
        int uniqueID = 0;
        String username = null;
        boolean notificationActivated = false;
        boolean funFactsActivated = false;
        String query = "SELECT id, username, notification_activated FROM public.user WHERE email = '" + email + "';";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                uniqueID = resultSet.getInt(1);
                username = resultSet.getString(2);
                notificationActivated = resultSet.getBoolean(3);
            }
            user = new User(uniqueID, email, username, notificationActivated);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return user;
    }

    /**
     * Method to delete a user and all plants in user library at once
     * author: Frida Jacobsson
     *
     * @param email
     * @param password
     * @return boolean value, false if transaction is rolled back
     * @throws SQLException
     */
    public boolean deleteAccount(String email, String password) {
        boolean accountDeleted = false;
        if (checkLogin(email, password)) {
            String querySelect = "SELECT public.user.id from public.user WHERE public.user.email = '" + email + "';";
            try {
                Statement statement = database.beginTransaction();
                ResultSet resultSet = statement.executeQuery(querySelect);
                if (!resultSet.next()) {
                    throw new SQLException();
                }
                int id = resultSet.getInt(1);
                String queryDeletePlants = "DELETE FROM public.user_plant WHERE user_id = " + id + ";";
                statement.executeUpdate(queryDeletePlants);
                String queryDeleteUser = "DELETE FROM public.user WHERE id = " + id + ";";
                statement.executeUpdate(queryDeleteUser);
                database.endTransaction();
                accountDeleted = true;
            }
            catch (SQLException sqlException) {
                try {
                    database.rollbackTransaction();
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return accountDeleted;
    }

    public boolean changeNotifications(User user, boolean notifications) {
        boolean notificationsChanged = false;
        boolean notificationsActivated = false;
        if (notifications) {
            notificationsActivated = true;
        }
        String query = "UPDATE public.user SET notification_activated = " + notificationsActivated + " WHERE email = '" + user.getEmail() + "';";
        try {
            database.executeUpdate(query);
            notificationsChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return notificationsChanged;
    }

   public boolean changeFunFacts(User user, Boolean funFactsActivated) {
      boolean funFactsChanged = false;
        boolean funFactsBitValue = false;
       if (funFactsActivated) {
           funFactsBitValue = true;
       }
        String query = "UPDATE public.user SET fun_facts_activated = " + funFactsBitValue + " WHERE email = '" + user.getEmail() + "';";
        try {
            database.executeUpdate(query);
            funFactsChanged = true;
       }
       catch (SQLException sqlException) {
            sqlException.printStackTrace();
       }
       return funFactsChanged;
    }
}

