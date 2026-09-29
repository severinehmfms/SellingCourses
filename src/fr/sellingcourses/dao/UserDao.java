package fr.sellingcourses.dao;

import java.sql.Connection;
import fr.sellingcourses.entities.User;

/**
 * Interface des méthodes DAO pour les utilisateurs
 */

public interface UserDao {

    User authentification(Connection connection, String login, String password);
    
    User createAccount(Connection connection, String login, String password);
}
