package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.sellingcourses.business.SellingCourses;
import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.entities.User;
import fr.sellingcourses.entities.Order.StatusValue;
import fr.sellingcourses.utils.Functions;

public class LineOrderDaoImpl implements LineOrderDao{

	/**
	 * Fonction qui permet de créer un objet LineOrder à partir d'un ResultSet
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public LineOrder getLineOrderFromDb(ResultSet resultSet) throws SQLException {
		SellingCourses service = new SellingCoursesImpl();	
		LineOrder lineOrder = null;
		
		try {
			int rsOrderId = resultSet.getInt("order_id"); 
			int rsTrainingId = resultSet.getInt("training_id");
			int rsQuantity = resultSet.getInt("quantity");
			//On récupère l'objet Training de cette ligne de commande
			Training training = null;
			try{
				training = service.findTrainingById(rsTrainingId);
			} catch (Exception e) {
    	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération de la formation associée à l'id.");
    	    	e.printStackTrace();
    	    }	
			
			lineOrder = new LineOrder(training, rsQuantity, rsOrderId);
		}catch(SQLException e) {
			Functions.printLogs(Functions.LOG_FILE, "ERREUR lors de la création d'un objet Training via le ResultSet.");
			e.printStackTrace();
		}
		return lineOrder;
	}
	
	@Override
	public boolean create(Connection connection, LineOrder lineOrder) {
		String str = "INSERT INTO lineorder (order_id, training_id, quantity) VALUES (?,?,?)";
		try (PreparedStatement ps = connection.prepareStatement(str)){
			ps.setInt(1, lineOrder.getOrder_id());
			ps.setInt(2, lineOrder.getTraining().getIdTraining());
			ps.setInt(3, lineOrder.getQuantity());
			
			if( ps.executeUpdate() == 0)
				throw new SQLException("Échec de l'insertion, aucune ligne affectée.");
			
			return true;
			
		}catch (SQLException e) {
			e.printStackTrace();
		}	
		return false;
	}

	@Override
	public boolean update(Connection connection, LineOrder lineOrder) {		
		String str = "UPDATE lineorder SET quantity=? WHERE order_id=? AND training_id=?";
		try (PreparedStatement ps = connection.prepareStatement(str)){
			ps.setInt(1, lineOrder.getQuantity());
			ps.setInt(2, lineOrder.getOrder_id());
			ps.setInt(3, lineOrder.getTraining().getIdTraining());
			
			// On récupère le nombre de lignes affectées par la requête
			int nbLignes = ps.executeUpdate(); 
			
			if (nbLignes == 0) { 
				throw new SQLException("Échec de la mise à jour, aucune ligne affectée."); 
			}
			
			return true;
            
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, LineOrder lineOrder) {
		String strSql = "DELETE FROM lineorder WHERE order_id=? AND training_id=?";
		//System.out.println(strSql);
    	try(PreparedStatement ps = connection.prepareStatement(strSql)){
    		ps.setInt(1, lineOrder.getOrder_id());
			ps.setInt(2, lineOrder.getTraining().getIdTraining());

	        // ps.executeUpdate() = nombre de lignes affectées par la requête
	        return ps.executeUpdate() > 0;
	        	        	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public List<LineOrder> findAllByOrder(Connection connection, Order order) {
		String sql = "SELECT order_id, training_id, quantity FROM lineorder WHERE order_id=?";
        List<LineOrder> lstLineOrder = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)){
    		ps.setInt(1, order.getIdOrder());        		
    		try(ResultSet rs = ps.executeQuery()) {
            	while (rs.next()) {
            		//On récupère l'objet correspondant à ce resultSet
            		LineOrder lineOrder = getLineOrderFromDb(rs);
            		if (lineOrder != null) lstLineOrder.add(lineOrder);
            	}
    		}
        } catch (SQLException e) {
        	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération de la liste des lignes de commande pour une commande.");
            e.printStackTrace();
        }
        Functions.printLogs(Functions.LOG_FILE, "Récupération de la liste des lignes de commande pour une commande.");
        return lstLineOrder;
	}
	
	@Override
	public LineOrder findLineOrder(Connection connection, int orderId, int training_id){
		LineOrder lineOrder = null;
		String strSql = "SELECT order_id, training_id, quantity FROM lineorder WHERE order_id=? AND training_id=?";
		try (PreparedStatement ps = connection.prepareStatement(strSql)){
			ps.setInt(1, orderId);
			ps.setInt(2, training_id);
			//System.out.println(strSql);
        	try(ResultSet resultSet = ps.executeQuery()){
        		
        		if (resultSet.next()) { // On lit la première (et unique) ligne
        			//On récupère l'objet correspondant à ce resultSet
        			lineOrder = getLineOrderFromDb(resultSet);
                } 
        	}        	
		} catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération de la ligne de commande pour cette commande et cette formation.");
	    	e.printStackTrace();
	    }	
		return lineOrder;
	}

	@Override
	public boolean isExists(Connection connection, int orderId, int training_id) {
		String sql = "SELECT COUNT(order_id) AS nbresults FROM lineorder WHERE order_id = ? AND training_id = ? ";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, orderId);
			ps.setInt(2, training_id);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {
	        		if (rs.getInt("nbresults") > 0) {
	        			return true;
	        		}
	             }
	        }
	    } catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération de la ligne de commande pour cette commande et cette formation.");
	    	e.printStackTrace();
	    }
		Functions.printLogs(Functions.LOG_FILE, "Aucune ligne de commande trouvée pour cette commande et cette formation");
	    return false; // Aucune ligne de commande trouvée
		
	}

	
	
}
