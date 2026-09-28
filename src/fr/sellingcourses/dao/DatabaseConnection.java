package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	public static void main(String[] args) throws Exception {

	}
	
	public static Connection getConnection() throws SQLException {
    	try {
    		Class.forName("org.mariadb.jdbc.Driver");	//Enregistre la class auprès du driver manager : autrement dit charge le pilote
    		    		
    	}catch(ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	// Récupération de la connection à partir d'une url + id + pwd
        String url = "jdbc:mariadb://localhost:3306/sellingCourses";
        String login = "SellingCourses";
        String password = "abcd";
        
        try {
			return DriverManager.getConnection(url, login, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
