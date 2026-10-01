package fr.sellingcourses.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import fr.sellingcourses.entities.Customer;
import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Order.StatusValue;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.entities.User;
import fr.sellingcourses.dao.CustomerDao;
import fr.sellingcourses.dao.CustomerDaoImpl;
import fr.sellingcourses.dao.DatabaseConnection;
import fr.sellingcourses.dao.LineOrderDao;
import fr.sellingcourses.dao.LineOrderDaoImpl;
import fr.sellingcourses.dao.OrderDao;
import fr.sellingcourses.dao.OrderDaoImpl;
import fr.sellingcourses.dao.TrainingDao;
import fr.sellingcourses.dao.TrainingDaoImpl;
import fr.sellingcourses.dao.UserDao;
import fr.sellingcourses.dao.UserDaoImpl;


/**
 * Classe pour la partie business de l'application
 */
public class SellingCoursesImpl implements SellingCourses {
	private final TrainingDao trainingDao;
	private final UserDao userDao;
	private final OrderDao orderDao;
	private final LineOrderDao lineOrderDao;
	private final CustomerDao customerDao;
	
	private Connection connection; // = DatabaseConnection.getConnection();
	
	public SellingCoursesImpl() throws SQLException {
		//Connection à la base de données
		this.connection = DatabaseConnection.getConnection();
		
		//DAO
		this.trainingDao = new TrainingDaoImpl();
		this.userDao = new UserDaoImpl();
		this.orderDao = new OrderDaoImpl();
		this.lineOrderDao = new LineOrderDaoImpl();
		this.customerDao = new CustomerDaoImpl();
    }	
	
	//***************** Services concernant les utilisateurs
	
	@Override
	public User authentification(String login, String password) {
		//On va appeler le dao user pour controler login et mot de passe, renvoie null si login et/ou mdp incorrect.
		User user = userDao.authentification(connection, login, password);
		return user;
	}	

	@Override
	public User createAccount(String login, String password) {
		//On va appeler le dao user pour controler login et mot de passe, renvoie null si login et/ou mdp incorrect.
		User user = userDao.createAccount(connection, login, password);
		return user;
	}
	
	@Override
	public boolean verifExistsLogin(String login) {
		//On va appeler le dao user pour controler login et mot de passe, renvoie null si login et/ou mdp incorrect.
		return userDao.verifExistsLogin(connection, login);
	}
	
	@Override
	public User findUserByLogin(String login) {
		//On va appeler le dao user pour controler login et mot de passe, renvoie null si login et/ou mdp incorrect.
		return userDao.findUserByLogin(connection, login);
	}

	
	//******************* Services concernant les formations (Classe Training)
	
	@Override
    public List<Training> findAllTraining() {
        return trainingDao.findAll(connection);
    }

	@Override
	public Training findTrainingById(int id) {
		return trainingDao.findById(connection, id);
	}

	@Override
	public List<Training> findTrainingBySearch(String wordToSearch, int choiceRemote) {
		return trainingDao.findBySearch(connection, wordToSearch, choiceRemote);
	}

	//****************** Services concernant les commandes (Classe Order)

	@Override
	public Order findOrderInProgressByUser(String login) {
		return orderDao.findOrderInProgressByUser(connection, login);
	}	

	@Override
	public List<Order> findLstOrderByUser(String login, StatusValue statusValue) {
		return orderDao.findLstOrderByUser(connection, login, statusValue);
	}
	
	@Override
	public Order createOrder(Order order) {
		return orderDao.create(connection, order);
	}	

	@Override
	public boolean updateOrder(Order order) {
		return orderDao.update(connection, order);
	}
	
	//****************** Services concernant les lignes de commande (Classe LineOrder)

	@Override
	public List<LineOrder> findAllLineOrderByOrder(Order order) {
		return lineOrderDao.findAllByOrder(connection, order);
	}

	@Override
	public LineOrder findLineOrder(int orderId, int training_id){
		return lineOrderDao.findLineOrder(connection, orderId, training_id);
	}	
	
	@Override
	public boolean createLineOrder(LineOrder lineOrder) {
		return lineOrderDao.create(connection, lineOrder);
	}

	@Override
	public boolean updateLineOrder(LineOrder lineOrder) {
		return lineOrderDao.update(connection, lineOrder);
	}

	@Override
	public boolean deleteLineOrder(LineOrder lineOrder) {
		return lineOrderDao.delete(connection, lineOrder);
	}

	@Override
	public boolean isLineOrderExists(int orderId, int training_id) {
		return lineOrderDao.isExists(connection, orderId, training_id);
	}

	//****************** Services concernant les clients (Classe Customer)

	@Override
	public Customer createCustomer(Customer customer) {
		return customerDao.create(connection, customer);
	}

	@Override
	public Customer findCustomerById(int id) {
		return customerDao.findById(connection, id);
	}

	//****************** Service pour fermer la connection sql
	//Mais ça me plait pas de le mettre ici... ça me semble pas logique avec modèle MVC
	@Override
	public void closeConnection() {
		try {
			this.connection.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}	

}
