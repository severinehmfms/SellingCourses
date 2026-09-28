package fr.sellingcourses;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import fr.sellingcourses.business.SellingCourses;
import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.dao.TrainingDao;
import fr.sellingcourses.dao.TrainingDaoImpl;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.utils.Functions;

public class Application {
	
	//On initialise le scanner
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws SQLException {
		
		//TODO Pour test : pour l'instant l'application ne gère pas l'authentification
		//Quand l'authentification sera possible, on mettra ce booléen à true quand le visiteur aura été authentifié
		boolean isConnect = false;
		
		TrainingDao trainingDao = new TrainingDaoImpl();
		SellingCourses service = new SellingCoursesImpl(trainingDao);		
		
		String[] menu = {
				"Affichage de toutes les formations",
				"Recherche des formations par critères",
				"Me connecter",
			    "Créer un compte"
			};
		
		int choice_user = -1;
		while (choice_user != 0) {
			//On demande à l'utilisateur son choix par rapport au menu proposé
			choice_user = Functions.ask_user_choice(scanner, menu);
			switch(choice_user) {
				case 1:				
					//Affichage de toutes les formations
					showTraining(service);
					break;
				case 2:				
					//Affichage des formations par critère
					showTrainingByCriterion(service);
					break;
				case 3:				
					//Me connecter
					System.out.println("Me connecter");
					System.out.println("Fonctionnalité non implémentée pour l'instant");
					break;				
				case 4:				
					//Créer un compte
					System.out.println("Créer un compte");
					System.out.println("Fonctionnalité non implémentée pour l'instant");
					break;
				case 0:
					System.out.println("Au-revoir et à bientôt !");
					break;
			}
		}
		
		//On referme le scanner
		scanner.close();
	}
	
	
	/**
	 * Méthode qui permet d'afficher les formations
	 * @param service
	 */
	public static void showTraining(SellingCourses service) {
		System.out.println("Affichage de toutes les formations");
		
		ArrayList<Training> lstTrainings = (ArrayList) service.findAllTraining();
		for (Training t : lstTrainings) {
			System.out.println(t); 
		}
	}
	
	/**
	 * Méthode qui permet de rechercher des formations par critère
	 * @param service
	 */
	public static void showTrainingByCriterion(SellingCourses service) {
		//TODO Saisie du critère mot clé recherché
		//TODO Demande si présentiel ou non
		
		String wordToSearch = "Java";
		int choiceRemote = 1;
		
		ArrayList<Training> lstTrainings = (ArrayList) service.findBySearch(wordToSearch,choiceRemote);
		for (Training t : lstTrainings) {
			System.out.println(t); 
		}
	}
	
	

}
