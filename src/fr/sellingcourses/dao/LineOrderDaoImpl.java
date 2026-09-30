package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;

public class LineOrderDaoImpl implements LineOrderDao{

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<LineOrder> findAllByOrder(Connection connection, Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExists(Connection connection, int orderId, int training_id) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
