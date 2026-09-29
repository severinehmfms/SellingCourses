package fr.sellingcourses.dao;

import java.sql.Connection;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Order.StatusValue;

public interface OrderDao {
	
	/**
	 * DAO associée à la table orderapp
	 */

	//Renvoie l'objet Order qui correspond à l'utilisateur dont le login est en paramètre, avec pour statut le statut en paramètre
	Order findOrderByUserAndStatus(Connection connection, String login, StatusValue status);
	
	Order insert(Connection connection, Order order);
}
