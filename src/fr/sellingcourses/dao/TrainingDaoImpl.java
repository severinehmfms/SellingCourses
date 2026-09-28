package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.sellingcourses.dao.DatabaseConnection;
import fr.sellingcourses.entities.Training;

public class TrainingDaoImpl implements TrainingDao{

	/**
	 * Fonction qui permet de récupérer en base de données la formation dont l'id est passé en paramètre
	 */
	@Override
	public Training findById(Connection connection, int id) {
		String sql = "SELECT training_id, training_name, training_description, training_length, remote_training FROM Training WHERE training_id = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {
	        		return new Training(
	                        rs.getInt("training_id"),
	                        rs.getString("training_name"),
	                        rs.getString("training_description"),
	                        rs.getInt("training_length"),
	                        rs.getBoolean("remote_training")
	                );
	             }
	        }
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }

	    return null; // Aucune formation trouvée
	}

	/**
	 * Fonction qui permet de récupérer toutes les formations en base de données
	 */
	@Override
	public List<Training> findAll(Connection connection) {
		String sql = "SELECT training_id, training_name, training_description, training_length, remote_training FROM Training ORDER BY training_name";
        List<Training> lstTrainings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            	lstTrainings.add(new Training(
            			rs.getInt("training_id"),
                        rs.getString("training_name"),
                        rs.getString("training_description"),
                        rs.getInt("training_length"),
                        rs.getBoolean("remote_training")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lstTrainings;
	}
}
