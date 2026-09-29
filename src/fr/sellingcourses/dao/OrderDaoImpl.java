package fr.sellingcourses.dao;

import java.sql.Connection;
import java.util.List;

import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Order.StatusValue;

public class OrderDaoImpl implements OrderDao{

	@Override
	public Order insert(Order order, Connection connection) {
		// TODO Auto-generated method stub
		return order;
	}

	@Override
	public Order findOrderByUserAndStatus(Connection connection, String login, StatusValue status) {
		// TODO Auto-generated method stub
		return null;
	}
}
