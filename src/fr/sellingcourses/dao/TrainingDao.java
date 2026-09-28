package fr.sellingcourses.dao;

import java.util.List;

import fr.sellingcourses.entities.Training;

public interface TrainingDao {


    Training findById(int id);
    List<Training> findAll();
	
	
}
