package fr.sellingcourses.dao;

import java.sql.Connection;
import java.util.List;

import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;

public interface LineOrderDao {
	
	void create(Connection connection, LineOrder lineOrder);

	boolean update(Connection connection, LineOrder lineOrder);
	
    boolean delete(Connection connection, LineOrder lineOrder);
	
	List<LineOrder> findAllByOrder(Connection connection, Order order);
	
	boolean isExists(Connection connection, int orderId, int training_id);

}
