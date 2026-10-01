package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import fr.sellingcourses.entities.Customer;
import fr.sellingcourses.utils.Functions;

public class CustomerDaoImpl implements CustomerDao{

	@Override
	public Customer findById(Connection connection, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer create(Connection connection, Customer customer) {
		String str = "INSERT INTO customer (customer_name, customer_first_name, customer_mail, customer_adresse, customer_phone) VALUES (?,?,?,?,?)";
		try (PreparedStatement ps = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, customer.getName());
			ps.setString(2, customer.getFirstName());			
			ps.setString(3, customer.getMail());
			ps.setString(4, customer.getAdresse());
			ps.setString(5, customer.getPhone());
			
			if( ps.executeUpdate() == 0)
				throw new SQLException("Échec de l'insertion, aucune ligne affectée.");
			
            // Récupération de l'ID généré
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    //System.out.println("Nouvel ID généré : " + id);
                    
                    customer.setIdCustomer(id);
                } else {
                	Functions.printLogs(Functions.LOG_FILE, "ERREUR Échec de la récupération de l'ID généré lors de la création d'un Customer.");
        	    	
                    throw new SQLException("Échec de la récupération de l'ID généré.");
                }
            }
			return customer;
			
		}catch (SQLException e) {
			e.printStackTrace();
		}	
		return customer;
	}

	
}
