package fr.sellingcourses.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import fr.sellingcourses.entities.Training;
import fr.sellingcourses.dao.DatabaseConnection;
import fr.sellingcourses.dao.TrainingDao;

public class SellingCoursesImpl implements SellingCourses {
	private final TrainingDao trainingDao;
	private Connection connection = DatabaseConnection.getConnection();
	
	public SellingCoursesImpl(TrainingDao trainingDao) throws SQLException {
        this.trainingDao = trainingDao;
		this.connection = DatabaseConnection.getConnection();
    }	
	
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
