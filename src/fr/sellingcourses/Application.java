package fr.sellingcourses;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import fr.sellingcourses.business.SellingCourses;
import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.entities.Customer;
import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.entities.User;
import fr.sellingcourses.entities.Order.StatusValue;
import fr.sellingcourses.exceptions.LoginAlreadyUsedException;
import fr.sellingcourses.utils.Functions;


/**
 * Application Vente de formations - partie IHM
 */

public class Application {
	
	//On initialise le scanner
	private static Scanner scanner = new Scanner(System.in);
	
	//Constantes
	public static final int LENGTH_MIN_PASSWORD = 5;
	public static final int LENGTH_MAX_PASSWORD = 8;
	
	public static void main(String[] args) throws SQLException, LoginAlreadyUsedException {
		//Appel de la classe de services (Partie métier de l'application)
		SellingCourses service = new SellingCoursesImpl();		
		
		User user = null;
		Order order = null;
				
		int choice_user = -1;
		while (choice_user != 0) {
			if (user != null) System.out.println("\nUtilisateur : " + user.getLogin() + "\n");
			
			String strMenuConnect = user != null ? "Me déconnecter" : "Me connecter (obligatoire pour acheter des formations) ";
			String[] menu = {
					"Affichage de toutes les formations",
					"Recherche des formations par critères",
					strMenuConnect,
				    "Créer un compte si vous n'en avez pas",
				    "Voir mon panier",
				    "Afficher les commandes déjà passées"
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
					if (user != null) {
						System.out.println("Déconnection");
						user = null;
					//Me déconnecter
					}else {
						user = authentification(service);
					}
					break;				
				case 4:				
					//Créer un compte
					System.out.println("Créer un compte");
					user = createAccount(service);
					break;
				case 5:				
					//Voir/Gérer mon panier
					if (user == null) System.out.println("ERREUR il faut être connecté pour accéder à cette fonctionnalité. Si vous n'avez pas encore de compter, créez un compte");
					else{
						//On récupère le panier associé à cet utilisateur, s'il existe (statut in_progress uniquement)
						order = service.findOrderInProgressByUser(user.getLogin());
						//Si pas de panier au statut en cours associé à cet utilisateur, on le crée
						if (order == null) {
							order = new Order(user);
							order = service.createOrder(order);
						}
						//On va gérer ce panier et on récupère les modifications
						order = gestionOrder(service, order);
					}
					break;
				case 6:				
					//Voir les commandes déjà passées
					if (user == null) System.out.println("ERREUR il faut être connecté pour accéder à cette fonctionnalité. Si vous n'avez pas encore de compter, créez un compte");
					else{
						ArrayList<Order> lstOrdersPassees = (ArrayList<Order>) service.findLstOrderByUser(user.getLogin(), StatusValue.ORDERED);
						
						System.out.println("Liste des commandes déjà passées :\n");
						for (Order or : lstOrdersPassees) {
							System.out.println(or); 
						}
					}
					break;
				case 0:
					System.out.println("Au-revoir et à bientôt !");
					break;
			}
		}
		
		//On referme le scanner
		scanner.close();
		
		//On referme la connexion mysql (mais ça me plait pas de le faire ici, finalement j'aurais du mettre dans chaque dao une nouvelle connexion et la fermer)
		service.closeConnection();
	}
	
	
	/**
	 * Méthode qui permet d'afficher les formations
	 * @param service
	 */
	public static void showTraining(SellingCourses service) {
		System.out.println("Affichage de toutes les formations :\n");
		
		ArrayList<Training> lstTrainings = (ArrayList) service.findAllTraining();
		for (Training t : lstTrainings) {
			System.out.println(t+"\n"); 
		}
	}
	
	/**
	 * Méthode qui permet de rechercher des formations par critère
	 * @param service
	 */
	public static void showTrainingByCriterion(SellingCourses service) {
		System.out.println("Affichage des formations par sélection :\n");
		
		String wordToSearch = Functions.input_string(scanner, "Entrez le mot clé à rechercher", true);
		int choiceRemote = Functions.input_int(scanner, "Recherche de tout type de formation, tapez 0, Présentiel tapez 1, Distanciel tapez 2", 0, 2);
		
		ArrayList<Training> lstTrainings = (ArrayList) service.findTrainingBySearch(wordToSearch,choiceRemote);
		for (Training t : lstTrainings) {
			System.out.println(t+"\n"); 
		}
	}
	
	/**
	 * Méthode pour demander à l'utilisateur de s'authentifier
	 */
	public static User authentification(SellingCourses service) {
		System.out.println("Authentification");
		
		String login = Functions.input_string(scanner, "Login");
		String password = Functions.input_string(scanner, "Password");
		
		User user = service.authentification(login, password);
		if (user == null) {
			System.out.println("ERREUR Aucun utilisateur n'existe avec ce login et ce mot de passe");
		}
		return user;		
	}
	
	/**
	 * Fonction qui permet de demander une saisie d'un login à l'utilisateur, et vérifie si ce login n'existe pas déjà en base
	 * @param scanner
	 * @param prompt
	 * @param service
	 * @return
	 * @throws LoginAlreadyUsedException
	 */
	public static String input_login(Scanner scanner, String prompt, SellingCourses service) throws LoginAlreadyUsedException {
		boolean is_input_ok = false;
		String input_user = "";
		
		while (!is_input_ok) {
			System.out.println(prompt);
			input_user = scanner.nextLine();
			
			try {
				if (input_user.trim().isEmpty()) {
					System.out.println("ERREUR - La saisie ne peut pas être à vide");
				}else if (service.verifExistsLogin(input_user)) {
					Functions.printLogs(Functions.LOG_FILE, "ERREUR - Ce login existe déjà dans la base de données");
					throw new LoginAlreadyUsedException("ERREUR - Ce login existe déjà dans la base de données\n");
				}else {		
					is_input_ok = true;
				}
			}catch(LoginAlreadyUsedException e) {
				e.printStackTrace();
			}
		}		
		return input_user;
	}
	
	/**
	 * Méthode pour demander à l'utilisateur de créer un compte
	 * @throws LoginAlreadyUsedException 
	 */
	public static User createAccount(SellingCourses service) throws LoginAlreadyUsedException {
		//Input spécifique ou j'ai rajouté le contrôle de l'existence du login 		
		String login = input_login(scanner, "Login", service);
		
		//Input spécifique pour le mot de passe, avec nombre de caractère minimal et maximal
		String password = Functions.input_password(scanner, "Password", LENGTH_MIN_PASSWORD, LENGTH_MAX_PASSWORD);
				
		User user = service.createAccount(login, password);
		if (user == null) {
			System.out.println("ERREUR lors de la création du compte utilisateur");
		}
		return user;		
	}
	
	/**
	 * Méthode qui permet à l'utilisateur de gérer son panier/sa commande
	 * @param service
	 * @throws SQLException 
	 */
	public static Order gestionOrder(SellingCourses service, Order order) throws SQLException {
		System.out.println("Voir mon panier");
		String[] sousMenu = {
				"Ajouter une formation à mon panier ou modifier une quantité d'une formation déjà ajoutée",
				"Retirer une formation de mon panier",
				"Valider le panier et passer la commande"
		};
		
		int choice_user = -1;
		while (choice_user != 0) {
			//On affiche le panier 
			System.out.println(order);
			
			//On demande à l'utilisateur son choix par rapport au sous-menu proposé
			choice_user = Functions.ask_user_choice(scanner, sousMenu);
			
			switch(choice_user) {
				case 1:				
					//Ajouter une formation à mon panier
					System.out.println("Ajouter une formation à mon panier");
					order = addTrainingOrder(service, order);
					break;
				case 2:	
					//Si le panier est vide message d'erreur
					if (order.getLstLineOrder().size() == 0) {
						System.out.println("Impossible de retirer une formation du panier : il est vide");
					}else {
						//Retirer une formation de mon panier
						System.out.println("Retirer une formation de mon panier");
						order = delTrainingOrder(service, order);
					}
					break;
				case 3:				
					//Valider le panier et passer la commande
					System.out.println("Valider le panier et passer la commande");
					Customer customer = new Customer(1, "DUPONT", "André", "andre.dupont@mail.com", "56 rue des colibris 50410 Gernau", "0102030405");
					order.validate(customer) ;
					//On enregistre la mise à jour de l'order
					if (service.updateOrder(order) == true) {
						System.out.println("Commande bien effectuée.");
						choice_user = 0;
					}else {
						System.out.println("ERREUR lors de la validation de la commande.");
					}
					break;
				case 0:
					System.out.println("Retour au menu précédent.");
					break;
			}
		}
		return order;
	}
	
	/**
	 * Méthode qui demande à l'utilisateur de saisir le numéro de formation à ajouter
	 * @param service
	 * @param scanner
	 * @param prompt
	 * @return
	 */
	public static int askUserIdTrainingToAdd(SellingCourses service, Scanner scanner, String prompt) {
		int input_int_user = 0;
		boolean is_valid_input = false;
	    while (!is_valid_input) {
	    	System.out.println(prompt);
	    	String input_user = scanner.nextLine();
	    	
	    	if (! input_user.matches("\\d+")) {
	        	System.out.println("ERREUR - Vous devez saisir un entier.");
	        } else {
	        	input_int_user = Integer.parseInt(input_user);
	        	
	        	Training training = service.findTrainingById(input_int_user);
	        	
	        	if (training == null) {
	        		System.out.println("ERREUR - Cet identifiant ne correspond pas à une id de formation valide.");
	        	}else {
	        		is_valid_input = true;
	        	}
	        }
	    }
		return input_int_user;
	}
	
	/**
	 * Méthode qui demande à l'utilisateur de saisir le numéro de formation à supprimer
	 * @param service
	 * @param scanner
	 * @param prompt
	 * @return
	 */
	public static int askUserIdTrainingToDel(SellingCourses service, Scanner scanner, String prompt, Order order) {
		int input_int_user = 0;
		boolean is_valid_input = false;
	    while (!is_valid_input) {
	    	System.out.println(prompt);
	    	String input_user = scanner.nextLine();
	    	
	    	if (! input_user.matches("\\d+")) {
	        	System.out.println("ERREUR - Vous devez saisir un entier.");
	        } else {
	        	input_int_user = Integer.parseInt(input_user);
	        	
	        	//On vérifie que le numéro de formation choisi correspond bien à une formation déjà dans le panier
	        	LineOrder lineOrder = service.findLineOrder(order.getIdOrder(), input_int_user);
	        	
	        	if (lineOrder == null) {
	        		System.out.println("ERREUR - Cette formation n'existe pas dans le panier");
	        	}else {
	        		is_valid_input = true;
	        	}
	        }
	    }
		return input_int_user;
	}
	
	
	/**
	 * Méthode pour ajouter une formation au panier
	 * @param service
	 * @param idTraining
	 * @throws SQLException 
	 */
	public static Order addTrainingOrder(SellingCourses service, Order order) throws SQLException {
		//On demande à l'utilisateur le numéro de la formation à ajouter
		int idTraining = askUserIdTrainingToAdd(service, scanner, "Entrez le numéro de la formation que vous souhaitez ajouter");
		
		//On regarde si cette formation a déjà été ajoutée au panier
		boolean isAlreadyExist = false;
		LineOrder lineOrder = service.findLineOrder(order.getIdOrder(), idTraining);
		if (lineOrder != null){
			isAlreadyExist = true;
		}
		
		String promptQty = isAlreadyExist? "Cette formation est déjà dans le panier, entrez la nouvelle quantité :" : "Entrez la quantité souhaitée";
		int qty = Functions.input_int(scanner, promptQty, 1, 1000);
		
		//Si cette ligne de commande existe déjà, on modifie la quantité et on l'update en base
		if (isAlreadyExist) {
			
			lineOrder.setQuantity(qty);
			if (service.updateLineOrder(lineOrder)) {
				System.out.println("Modification de cette formation du panier correctement effectué");
				
				//Important on met à jour la liste des lignes de commande dans order.
				order.majQuantityLineOrder(lineOrder,qty);
			}else {
				System.out.println("ERREUR lors de la modification de cette formation");
			}
			
		//Si cette ligne de commande n'existe pas, on la crée et on l'insère en base
		}else {
			Training training = service.findTrainingById(idTraining);
			lineOrder = new LineOrder(training, qty, order.getIdOrder());
						
			if (service.createLineOrder(lineOrder)) {
				System.out.println("Ajout de cette formation au panier correctement effectuée");
				//Important on met à jour la liste des lignes de commande dans order.
				order.addLineOrderToLst(lineOrder);
			}
			else {
				System.out.println("ERREUR lors de l'ajout de cette formation");
			}
		}
		return order;
	}
	
	/**
	 * Méthode pour supprimer une formation du panier
	 * @param service
	 * @param idTraining
	 */
	public static Order delTrainingOrder(SellingCourses service, Order order) {
		//On demande à l'utilisateur quelle formation il souhaite supprimer
		int idTraining = askUserIdTrainingToDel(service, scanner, "Entrez le numéro de la formation que vous souhaitez retirer", order);
		
		LineOrder lineOrder = service.findLineOrder(order.getIdOrder(), idTraining);
		if (service.deleteLineOrder(lineOrder)) {
			System.out.println("Suppression de cette formation du panier correctement effectuée");
			//Important on met à jour la liste des lignes de commande dans order.
			order.delLineOrderLst(lineOrder);
		}else {
			System.out.println("ERREUR lors de la suppression du panier de cette formation");
		}
				
		return order;
	}	
}
