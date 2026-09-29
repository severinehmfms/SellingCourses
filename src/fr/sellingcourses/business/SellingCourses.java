package fr.sellingcourses.business;
import java.util.List;

import fr.sellingcourses.entities.Training;
import fr.sellingcourses.entities.User;

/**
 * Interface pour la partie business de l'application
 */

public interface SellingCourses {
	
	User authentification(String login, String password);
	
	List<Training> findAllTraining();
	
	List<Training> findBySearch(String wordToSearch, int choiceRemote);
	
	Training findTrainingById(int id);
	
}
