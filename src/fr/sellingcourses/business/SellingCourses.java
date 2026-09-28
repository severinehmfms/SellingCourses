package fr.sellingcourses.business;
import java.util.List;

import fr.sellingcourses.entities.Training;

public interface SellingCourses {
	
	List<Training> findAllTraining();
	
	List<Training> findBySearch(String wordToSearch, int choiceRemote);
	
	Training findTrainingById(int id);
	
}
