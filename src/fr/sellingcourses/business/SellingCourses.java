package fr.sellingcourses.business;
import java.util.List;

import fr.sellingcourses.entities.Training;

public interface SellingCourses {
	
	List<Training> findAllTraining();
	
	Training findTrainingById(int id);
	
}
