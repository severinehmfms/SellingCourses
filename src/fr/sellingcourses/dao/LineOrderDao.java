package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;

public interface LineOrderDao {
	
	boolean create(Connection connection, LineOrder lineOrder);

	boolean update(Connection connection, LineOrder lineOrder);
	
    boolean delete(Connection connection, LineOrder lineOrder);
	
	List<LineOrder> findAllByOrder(Connection connection, Order order);
	
	LineOrder findLineOrder(Connection connection, int orderId, int training_id) throws SQLException;
	
	boolean isExists(Connection connection, int orderId, int training_id);
}
