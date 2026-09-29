package fr.sellingcourses.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
	
	//******************* Services pour les formations
	
	@Override
    public List<Training> findAllTraining() {
        return trainingDao.findAll(connection);
    }

	@Override
	public Training findTrainingById(int id) {
		return trainingDao.findById(connection, id);
	}

	@Override
	public List<Training> findBySearch(String wordToSearch, int choiceRemote) {
		return trainingDao.findBySearch(connection, wordToSearch, choiceRemote);
	}
}
