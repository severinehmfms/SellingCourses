package fr.sellingcourses.dao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.sellingcourses.utils.Functions;

public class DatabaseConnection {
	public static void main(String[] args) throws Exception {

	}
	
	public static Connection getConnection() throws SQLException {
    	try {
    		Class.forName("org.mariadb.jdbc.Driver");	//Enregistre la class auprès du driver manager : autrement dit charge le pilote
    		Functions.printLogs(Functions.LOG_FILE, "Chargement du pilote JDBC bien effectué");
    		
    	}catch(ClassNotFoundException e) {
    		Functions.printLogs(Functions.LOG_FILE, "ERREUR lors du chargement du pilote JDBC.");
    		//On affiche l'erreur de l'exception
    		e.printStackTrace();
    	}
    	
    	// Récupération de la connection à partir d'une url + id + pwd
        String url = "jdbc:mariadb://localhost:3306/sellingCourses";
        String login = "SellingCourses";
        String password = "abcd";
        
        try {
        	Functions.printLogs(Functions.LOG_FILE, "Connection à la base de données bien effectuée");
			return DriverManager.getConnection(url, login, password);
		} catch (SQLException e) {
			Functions.printLogs(Functions.LOG_FILE, "ERREUR lors de la Connection à la base de données.");
			e.printStackTrace();
			return null;
		}
	}
}
