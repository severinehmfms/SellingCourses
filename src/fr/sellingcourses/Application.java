package fr.sellingcourses;

import java.util.Scanner;

import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.dao.TrainingDaoImpl;
import fr.sellingcourses.utils.Functions;

public class Application {
	
	//On initialise le scanner
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		//TODO Pour test : pour l'instant l'application ne gère pas l'authentification
		//Quand l'authentification sera possible, on mettra ce booléen à true quand le visiteur aura été authentifié
		boolean isConnect = false;
		
		
		
		
		String[] menu = {
				"Affichage des formations",
				"Me connecter",
			    "Créer un compte"
			};
		
		int choice_user = -1;
		while (choice_user != 0) {
			//On demande à l'utilisateur son choix par rapport au menu proposé
			choice_user = Functions.ask_user_choice(scanner, menu);
			switch(choice_user) {
				case 1:				
					//Affichage des formations
					showTraining();
					break;
				case 2:				
					//Me connecter
					System.out.println("Me connecter");
					System.out.println("Fonctionnalité non implémentée pour l'instant");
					break;				
				case 3:				
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
	
	
	public static void showTraining() {
		System.out.println("Affichage des formations");
		System.out.println("Fonctionnalité non implémentée pour l'instant");
	}
	
	

}
