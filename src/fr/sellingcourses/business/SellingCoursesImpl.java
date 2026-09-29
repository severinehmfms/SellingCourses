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
import fr.sellingcourses.dao.DatabaseConnection;
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
	private Connection connection = DatabaseConnection.getConnection();
	
	public SellingCoursesImpl() throws SQLException {
		//Connection à la base de données
		this.connection = DatabaseConnection.getConnection();
		
		//DAO
		this.trainingDao = new TrainingDaoImpl();
		this.userDao = new UserDaoImpl();
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
	public Order findOrderByUserAndStatus(String login, StatusValue status) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Order insertOrder(Order order) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//****************** Services concernant les lignes de commande (Classe LineOrder)

	@Override
	public List<LineOrder> findAllLineOrderByOrder(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineOrder createLineOrder(LineOrder lineOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateLineOrder(LineOrder lineOrder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteLineOrder(LineOrder lineOrder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLineOrderExists(int orderId, int training_id) {
		// TODO Auto-generated method stub
		return false;
	}

	//****************** Services concernant les clients (Classe Customer)

	@Override
	public List<Customer> findAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer findCustomerById(int id) {
		// TODO Auto-generated method stub
		return null;
	}	
}
