package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.utils.Functions;


/**
 * Méthodes DAO pour la classe Training associée à la table Training de la base de données
 */
public class TrainingDaoImpl implements TrainingDao{
	
	/**
	 * Fonction qui permet de créer un objet Training à partir d'un ResultSet
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public Training getTrainingFromDb(ResultSet resultSet) throws SQLException {
		Training training = null;
		try {
			int rsId = resultSet.getInt("training_id"); 
			String rsName = resultSet.getString("training_name"); 
			String rsDescription = resultSet.getString("training_description");
			int rsLength = resultSet.getInt("training_length");
			boolean rsRemoteTraining = resultSet.getBoolean("remote_training");
			double rsPrice = resultSet.getDouble("training_price");
			
			training = new Training(rsId, rsName, rsDescription, rsLength, rsRemoteTraining, rsPrice);
	   
		}catch(SQLException e) {
			Functions.printLogs(Functions.LOG_FILE, "ERREUR lors de la création d'un objet Training via le ResultSet.");
			e.printStackTrace();
		}
		return training;
	}	

	/**
	 * Fonction qui permet de récupérer en base de données la formation dont l'id est passé en paramètre
	 */
	@Override
	public Training findById(Connection connection, int id) {
		String sql = "SELECT training_id, training_name, training_description, training_length, remote_training, training_price FROM Training WHERE training_id = ?";
		System.out.println(sql);
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {

	        		Functions.printLogs(Functions.LOG_FILE, "Récupération d'une formation par son id bien effectuée");
	        		return getTrainingFromDb(rs);
	        		/*
	        		return new Training(
	                        rs.getInt("training_id"),
	                        rs.getString("training_name"),
	                        rs.getString("training_description"),
	                        rs.getInt("training_length"),
	                        rs.getBoolean("remote_training")
	                );*/
	             }
	        }
	    } catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération d'une formation par son id.");
	    	e.printStackTrace();
	    }
		Functions.printLogs(Functions.LOG_FILE, "Aucune formation trouvée avec cet id"+id);
	    return null; // Aucune formation trouvée
	}

	/**
	 * Fonction qui permet de récupérer toutes les formations en base de données
	 */
	@Override
	public List<Training> findAll(Connection connection) {
		String sql = "SELECT training_id, training_name, training_description, training_length, remote_training, training_price FROM Training ORDER BY training_name";
        List<Training> lstTrainings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            	Training training = getTrainingFromDb(rs);
            	lstTrainings.add(training);
            	/*
            	lstTrainings.add(new Training(
            			rs.getInt("training_id"),
                        rs.getString("training_name"),
                        rs.getString("training_description"),
                        rs.getInt("training_length"),
                        rs.getBoolean("remote_training")
                ));*/
            }
        } catch (SQLException e) {
        	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération de la liste des formations.");
            e.printStackTrace();
        }
        Functions.printLogs(Functions.LOG_FILE, "Récupération de la liste des formations bien effectuée");
        return lstTrainings;
	}

	/**
	 * Fonction qui renvoie la liste des formations par critère :
	 * String wordToSearch : Chaine vide si pas de mot à rechercher, mot clé à rechercher sinon
	 * int choice_remote : 
	 * 0 si pas d'informations pour ce critère, 1 si on cherche les formations en présentiel, 2 si on cherche les formations à distance
	 */
	@Override
	public List<Training> findBySearch(Connection connection, String wordToSearch, int choiceRemote) {
		String sql = "SELECT training_id, training_name, training_description, training_length, remote_training, training_price FROM Training ";
	
		//Si au moins un critère de recherche est renseigné, on ajoute la clause WHERE
		if  ( (choiceRemote != 0) || (!wordToSearch.equals("")) ) sql += " WHERE ";
		
		//Si on a choisi un critère présentiel/distanciel
		//On recherche les formations en présentiel
		if (choiceRemote == 1) {
			sql += "remote_training = FALSE ";
		}
		//On recherche les formations en distanciel
		if (choiceRemote == 2) {
			sql += "remote_training = TRUE ";
		}				 
		//On recherche un mot clé dans le nom et la description de la formation
		if (!wordToSearch.equals("")) {
			if (choiceRemote != 0) sql += "AND ";
			sql += "(training_name LIKE ? OR training_description LIKE ?) ";
		}
		//On trie par le nom de la formation
		sql += "ORDER BY training_name";
		
		//System.out.println(sql);
		
        List<Training> lstTrainings = new ArrayList<>();
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
        	if (!wordToSearch.equals("")) {
        		ps.setString(1, "%" + wordToSearch + "%");
        		ps.setString(2, "%" + wordToSearch + "%");
        	}
            
        	try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	            	Training training = getTrainingFromDb(rs);
	            	lstTrainings.add(training);
	            }
        	}
        } catch (SQLException e) {
        	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération d'une sélection de formations.");
            e.printStackTrace();
        }
        Functions.printLogs(Functions.LOG_FILE, "Récupération d'une sélection de formations bien effectuée");
        return lstTrainings;
	}
	
	
}
