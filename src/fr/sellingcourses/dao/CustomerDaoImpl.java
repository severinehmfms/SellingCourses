package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.sellingcourses.entities.Customer;
import fr.sellingcourses.utils.Functions;

public class CustomerDaoImpl implements CustomerDao{

	@Override
	public Customer findById(Connection connection, int id) {
		String sql = "SELECT customer_id, customer_name, customer_first_name, customer_mail, customer_adresse, customer_phone  FROM customer WHERE customer_id = ? ";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {
	        		Functions.printLogs(Functions.LOG_FILE, "Récupération d'un customer par son id bien effectuée");
	        		int rsId = rs.getInt("customer_id"); 
	        		String rsName = rs.getString("customer_name"); 
	        		String rsFirstName = rs.getString("customer_first_name"); 
	        		String rsMail = rs.getString("customer_mail"); 
	        		String rsAddress = rs.getString("customer_adresse"); 
	        		String rsPhone = rs.getString("customer_phone"); 
	        		
	        		Customer customer = new Customer(rsId, rsName, rsFirstName, rsMail, rsAddress, rsPhone);
					return customer;	        		
	             }	        	
	        }
	    } catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération d'un panier par son id.");
	    	e.printStackTrace();
	    }
		Functions.printLogs(Functions.LOG_FILE, "Aucun panier trouvé pour cet utilisateur et ce statut.");
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
