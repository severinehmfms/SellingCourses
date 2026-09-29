package fr.sellingcourses.dao;

import java.sql.Connection;
import java.util.List;

import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;

public class LineOrderDaoImpl implements LineOrderDao{

	@Override
	public LineOrder create(Connection connection, LineOrder lineOrder) {
		// TODO Auto-generated method stub
		return lineOrder;		
	}

	@Override
	public boolean update(Connection connection, LineOrder lineOrder) {
		// TODO Auto-generated method stub
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
