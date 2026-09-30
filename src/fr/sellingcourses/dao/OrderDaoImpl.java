package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import fr.sellingcourses.business.SellingCourses;
import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.entities.Customer;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.entities.User;
import fr.sellingcourses.entities.Order.StatusValue;
import fr.sellingcourses.utils.Functions;

public class OrderDaoImpl implements OrderDao{

	/**
	 * Fonction qui permet de créer un objet Order à partir d'un ResultSet
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public Order getOrderFromDb(ResultSet resultSet) throws SQLException {
		SellingCourses service = new SellingCoursesImpl();	
		
		Order order = null;
		try {
			int rsId = resultSet.getInt("order_id"); 
			int rsStatus = resultSet.getInt("order_status"); 
			
			//On convertit en LocalDateTime la date récupérée au format sql
			
			LocalDateTime rsDate = null;
			if (resultSet.getTimestamp("order_date") != null) {
				resultSet.getTimestamp("order_date").toLocalDateTime();
			}
			int rsTotalAmount = resultSet.getInt("total_amount");
			
			//On va récupérer l'objet utilisateur et l'objet customer correspondant à l'id
			String rsLoginUser = resultSet.getString("login_app");
			int rsIdCustomer = resultSet.getInt("customer_id");
			
			//On récupère l'utilisateur associé à ce login
			//UserDao userDao = new UserDaoImpl();
			User user = service.findUserByLogin(rsLoginUser);
						
			//On récupère le StatusValue associé au code statut en base
			StatusValue statusValue = StatusValue.IN_PROGRESS;
			if (rsStatus == 1) statusValue = StatusValue.ORDERED;
			
			//TODO On récupère l'objet client associé à customer_id si customer_id différent de 0 (A rajouter quand la partie optionnelle client sera prête)
						
			order =  new Order(rsId, statusValue, rsDate, rsTotalAmount, user);
	   
		}catch(SQLException e) {
			Functions.printLogs(Functions.LOG_FILE, "ERREUR lors de la création d'un objet Training via le ResultSet.");
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public Order findOrderByUserAndStatus(Connection connection, String login, StatusValue status) {
		int codeStatus = 0;
		if (status == StatusValue.ORDERED) codeStatus = 1;
		
		String sql = "SELECT order_id, order_status, order_date, total_amount, login_app, customer_id  FROM orderapp WHERE login_app = ? AND order_status = ? ";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, login);
			ps.setInt(2, codeStatus);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {

	        		Functions.printLogs(Functions.LOG_FILE, "Récupération d'un panier par son id bien effectuée");
	        		return getOrderFromDb(rs);
	        		/*
	        		return new Order(
	                        rs.getInt("order_id"),
	                        rs.getString("order_status"),
	                        rs.getDate("order_date"),
	                        rs.getDouble("total_amount")
	                );*/
	             }
	        }
	    } catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération d'un panier par son id.");
	    	e.printStackTrace();
	    }
		Functions.printLogs(Functions.LOG_FILE, "Aucun panier trouvé pour cet utilisateur et ce statut.");
	    return null; // Aucun panier trouvé
	}	

	
}
