package fr.sellingcourses;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import fr.sellingcourses.business.SellingCourses;
import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.dao.TrainingDao;
import fr.sellingcourses.dao.TrainingDaoImpl;
import fr.sellingcourses.dao.UserDao;
import fr.sellingcourses.dao.UserDaoImpl;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.entities.User;
import fr.sellingcourses.utils.Functions;


/**
 * Application Vente de formations - partie IHM
 */

public class Application {
	
	//On initialise le scanner
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws SQLException {
		
		//TODO Pour test : pour l'instant l'application ne gère pas l'authentification
		//Quand l'authentification sera possible, on mettra ce booléen à true quand le visiteur aura été authentifié
		boolean isConnect = false;
		
		//Service (Partie métier de l'application)
		SellingCourses service = new SellingCoursesImpl();		
		
		User user = null;
		
		int choice_user = -1;
		while (choice_user != 0) {
			if (user != null) System.out.println("Utilisateur : " + user.getLogin() + "\n");
			
			String strMenuConnect = isConnect ? "Me déconnecter" : "Me connecter";
			String[] menu = {
					"Affichage de toutes les formations",
					"Recherche des formations par critères",
					strMenuConnect,
				    "Créer un compte"
				};
					
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
					if (isConnect) {
						System.out.println("Déconnection");
						user = null;
						isConnect = false;
					//Me déconnecter
					}else {
						System.out.println("Authentification");
						user = authentification(service);
						if (user != null) isConnect = true;
					}
					break;				
				case 4:				
					//Créer un compte
					System.out.println("Créer un compte");
					user = createAccount(service);
					if (user != null) isConnect = true;
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
		String wordToSearch = Functions.input_string(scanner, "Entrez le mot clé à rechercher", true);
		int choiceRemote = Functions.input_int(scanner, "Recherche de tout type de formation, tapez 0, Présentiel tapez 1, Distanciel tapez 2", 0, 2);
		
		ArrayList<Training> lstTrainings = (ArrayList) service.findBySearch(wordToSearch,choiceRemote);
		for (Training t : lstTrainings) {
			System.out.println(t); 
		}
	}
	
	/**
	 * Méthode pour demander à l'utilisateur de s'authentifier
	 */
	public static User authentification(SellingCourses service) {
		String login = Functions.input_string(scanner, "Login");
		String password = Functions.input_string(scanner, "Password");
		
		User user = service.authentification(login, password);
		if (user == null) {
			System.out.println("ERREUR Aucun utilisateur n'existe avec ce login et ce mot de passe");
		}
		return user;		
	}
	
	/** 
	 * Fonction qui permet de demander une saisie d'un login à l'utilisateur
	 * prompt = Prompt qui demande à l'utilisateur de saisir 
	 */
	public static String input_login(Scanner scanner, String prompt, SellingCourses service) {
		boolean is_input_ok = false;
		String input_user = "";
		while (!is_input_ok) {
			System.out.println(prompt);
			input_user = scanner.nextLine();
			
			if (input_user.trim().isEmpty()) {
				System.out.println("ERREUR - La saisie ne peut pas être à vide");
				is_input_ok = false;
			}else if (service.verifExistsLogin(input_user)) {
				System.out.println("ERREUR - Ce login existe déjà dans la base de données");
				is_input_ok = false;
			}else {		
				is_input_ok = true;
			}
		}
		return input_user;
	}
	
	/**
	 * Méthode pour demander à l'utilisateur de créer un compte
	 */
	public static User createAccount(SellingCourses service) {
		
		//Input spécifique ou j'ai rajouté le contrôle de l'existence du login 		
		String login = input_login(scanner, "Login", service);
		
		//TODO Rajouter des contrôles sur le mot de passe pour accepter des caractères spéciaux, des chiffres, longueur minimal du mot de passe etc
		String password = Functions.input_string(scanner, "Password");
				
		User user = service.createAccount(login, password);
		if (user == null) {
			System.out.println("ERREUR lors de la création du compte utilisateur");
		}
		return user;		
	}
	
	
	
}
