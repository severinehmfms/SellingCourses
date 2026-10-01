package fr.sellingcourses;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.sellingcourses.business.SellingCourses;
import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.Training;
import fr.sellingcourses.entities.User;
import fr.sellingcourses.entities.Order.StatusValue;
import fr.sellingcourses.utils.Functions;

public class TestApplication {
	
	public static void main(String[] args) throws SQLException {
		
		SellingCourses service = new SellingCoursesImpl();		
		
		//TEST Training
		//On récupère toutes les formations
		System.out.println("Affichage de toutes les formations :\n");		
		ArrayList<Training> lstTrainings = (ArrayList) service.findAllTraining();
		for (Training t : lstTrainings) {
			System.out.println(t+"\n"); 
		}
		
		//On récupère formation par mot clé
		System.out.println("Affichage des formations par sélection :\n");
		
		String wordToSearch = "JAVA";
		int choiceRemote = 1;
		
		ArrayList<Training> lstTrainings2 = (ArrayList) service.findTrainingBySearch(wordToSearch,choiceRemote);
		for (Training t : lstTrainings2) {
			System.out.println(t+"\n"); 
		}
				
		//On récupère une formation par son id
		Training training = service.findTrainingById(1);
		System.out.println(training);
		
		//TEST User
		//On récupère l'utilisateur
		String login = "seve";
		User user = service.findUserByLogin(login);
		
		//S'il n'existe pas on va le créer TEST Création d'un utilisateur
		if (user == null) {
			user = service.createAccount(login, "test");
		}
				
		//TEST Customer
		//TODO On récupère la liste des customers
		
		//TODO On récupère un customer par son id
		
		
		//TEST Order
		//On récupère le panier en cours si il existe pour notre utilisateur
		Order order = service.findOrderInProgressByUser(user.getLogin());
		if (order != null)		System.out.println(order);
		else					System.out.println("Pas de panier associé à cet utilisateur, au statut en cours");
		//Si elle n'existe pas, on la crée
		if (order == null) {
			//On va créer un panier associé à ce user
			order = new Order(user);
			service.createOrder(order);
		}		
		
		//On récupère la liste des commandes déjà passées pour notre utilisateur 
		ArrayList<Order> lstOrdersPassees = (ArrayList<Order>) service.findLstOrderByUser(user.getLogin(), StatusValue.ORDERED);
		System.out.println("Liste des commandes déjà passées pour l'utilisateur " + user.getLogin() + "\n");
		for (Order or : lstOrdersPassees) {
			System.out.println(or); 
		}
		
		//Test LineOrder
		//On recherche d'abord si cette formation est déjà dans ce panier
		if (service.isLineOrderExists(order.getIdOrder(), training.getIdTraining())){
			System.out.println("La formation est déjà dans le panier");
			
			System.out.println("On récupère la ligne de commande pour idorder : " + order.getIdOrder() + " et id training : " + training.getIdTraining() + ": \n");
			LineOrder lineRecup = service.findLineOrder(order.getIdOrder(), training.getIdTraining() );
			System.out.println(lineRecup);
			
		}else {
			System.out.println("La formation n'est pas encore dans le panier");
			
			//On rajoute une formation et une quantité (ligne de commande lineorder) à ce panier
			LineOrder line = new LineOrder(training, 5, order.getIdOrder());
			if (service.createLineOrder(line)) {
				System.out.println("Création de la ligne de commande bien effectuée : " + line);
			}else {
				System.out.println("ERREUR lors de la création de la ligne de commande");
			}
			
			//On va modifier la quantité de cette formation
			line.setQuantity(10);
			if (service.updateLineOrder(line)) {
				System.out.println("Modification de la ligne de commande bien effectuée : " + line);
			}else {
				System.out.println("ERREUR lors de la modification de la ligne de commande");
			}
			
			//On va supprimer complétement cette formation de la commande
			if (service.deleteLineOrder(line)){
				System.out.println("Suppression de la ligne de commande bien effectuée : ");
			}else {
				System.out.println("ERREUR lors de la Suppression de la ligne de commande");
			}
		}
		
		service.closeConnection();
		
	}
}
