package fr.sellingcourses.dao;

import java.sql.Connection;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Order.StatusValue;

public interface OrderDao {
	
	/**
	 * DAO associée à la table orderapp
	 */

	//Renvoie l'objet Order en cours qui correspond à l'utilisateur dont le login est en paramètre
	Order findOrderInProgressByUser(Connection connection, String login);
	
	Order create(Connection connection, Order order);
}
