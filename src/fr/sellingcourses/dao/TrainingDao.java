package fr.sellingcourses.dao;

import java.util.List;
import java.sql.Connection;

import fr.sellingcourses.entities.Training;

public interface TrainingDao {

    Training findById(Connection connection, int id);
    List<Training> findAll(Connection connection);	
}
