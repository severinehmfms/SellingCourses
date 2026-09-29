package fr.sellingcourses.dao;

import java.util.List;
import java.sql.Connection;

import fr.sellingcourses.entities.Training;


/**
 * Interface des méthodes DAO pour les Formations (Training)
 */

public interface TrainingDao {

    Training findById(Connection connection, int id);
    List<Training> findAll(Connection connection);
    List<Training> findBySearch(Connection connection, String wordToSearch, int choice_remote);
    
}
