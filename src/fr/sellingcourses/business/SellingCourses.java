package fr.sellingcourses.business;
import java.sql.SQLException;
import java.util.List;

import fr.sellingcourses.entities.Customer;
import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.entities.User;

/**
 * Interface pour la partie business de l'application
 */

public interface SellingCourses {
	
	//Services concernant la gestion des utilisateurs (Classe User)
	User authentification(String login, String password);
	
	User createAccount(String login, String password);
	
	boolean verifExistsLogin(String login);
	
	User findUserByLogin(String login);
	
	//Services concernant les formations (Classe Training)
	List<Training> findAllTraining();
	
	List<Training> findTrainingBySearch(String wordToSearch, int choiceRemote);
	
	Training findTrainingById(int id);
	
	//Services concernant les commandes (Classe Order)
	Order findOrderInProgressByUser(String login);
	
	Order createOrder(Order order);
	
	//Services concernant les lignes de commandes (Classe LineOrder)
	List<LineOrder> findAllLineOrderByOrder(Order order);
	
	LineOrder findLineOrder(int orderId, int training_id) throws SQLException;
	
	boolean createLineOrder(LineOrder lineOrder);

	boolean updateLineOrder(LineOrder lineOrder);
	
    boolean deleteLineOrder(LineOrder lineOrder);
	
	boolean isLineOrderExists(int orderId, int training_id);
	
	//Services concernant les clients (Classe Customer)
	List<Customer> findAllCustomers();
	
	Customer findCustomerById(int id);
	
}
