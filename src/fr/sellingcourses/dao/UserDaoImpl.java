package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.sellingcourses.entities.User;
import fr.sellingcourses.utils.Functions;

public class UserDaoImpl implements UserDao{

	@Override
	public User authentification(Connection connection, String login, String password) {
		String sql = "SELECT login_app, password_app FROM userapp WHERE login_app = ? AND password_app = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, login);
			ps.setString(2, password);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {

	        		Functions.printLogs(Functions.LOG_FILE, "Authentification bien effectuée");
	        		
	        		return new User(
	                        rs.getString("login_app"),
	                        rs.getString("password_app")
	                );
	             }
	        }
	    } catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de l'authentification.");
	    	e.printStackTrace();
	    }
		Functions.printLogs(Functions.LOG_FILE, "Aucun utilisateur trouvé avec ce login et ce mot de passe");
	    return null; // Aucun utilisateur trouvé
	}

	@Override
	public User findUserByLogin(Connection connection, String login) {
		String sql = "SELECT login_app, password_app FROM userapp WHERE login_app = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, login);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {

	        		Functions.printLogs(Functions.LOG_FILE, "Récupération de l'utilisateur bien effectuée");
	        		
	        		return new User(
	                        rs.getString("login_app"),
	                        rs.getString("password_app")
	                );
	             }
	        }
	    } catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération de l'utilisateur.");
	    	e.printStackTrace();
	    }
		Functions.printLogs(Functions.LOG_FILE, "Aucun utilisateur trouvé pour ce login");
	    return null; // Aucun utilisateur trouvé
	}
	
	@Override
	public User createAccount(Connection connection, String login, String password) {
		String str = "INSERT INTO userapp (login_app, password_app) VALUES (?,?)";
		try (PreparedStatement ps = connection.prepareStatement(str)){
			ps.setString(1, login);
			ps.setString(2, password);
			if( ps.executeUpdate() == 0) {
				throw new SQLException("Échec de l'insertion, aucune ligne affectée.");
			}else {
				Functions.printLogs(Functions.LOG_FILE, "Création du compte bien effectuée");
				return new User(login,password);
			}
		}catch (SQLException e) {
			Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la création du compte.");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean verifExistsLogin(Connection connection, String login) {
		String sql = "SELECT login_app FROM userapp WHERE login_app = ? ";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, login);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {
	        		Functions.printLogs(Functions.LOG_FILE, "Ce login existe déjà dans la base.");
	        		
	        		return true;
	             }else {
	            	Functions.printLogs(Functions.LOG_FILE, "Ce login n'existe pas déjà dans la base.");
	            	 
	            	return false;
	             }
	        }
	    } catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de l'authentification.");
	    	e.printStackTrace();
	    }
	    return true; // Comme la vérification n'a pas pu se faire on renvoie true pour être sur
	}

}
