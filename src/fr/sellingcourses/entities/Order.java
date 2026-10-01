package fr.sellingcourses.entities;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Classe qui représente le panier, puis quand le statut est validé, la commande
 */

public class Order {
	
	//Valeurs que peuvent prendre le statut
	public static enum StatusValue {		//0 : IN_PROGRESS
		IN_PROGRESS, ORDERED				//1 : ORDERED
	}
	
	private int idOrder;					//Id (en base)
	private StatusValue status;				//Statut (EN_COURS = Panier, VALIDEE = Commande)
	private LocalDateTime date; 			//Date et heure du passage réel de la commande LocalDateTime.now()
	
	private User user;						//Utilisateur qui a créé le panier
	private Customer customer;				//Client concerné par la commande
	
	private List<LineOrder> lstLineOrder;	//Liste des lignes de commandes de ce panier/cette commande
	
	/**
	 * Constructeur de base quand on a pas encore toutes les informations
	 * @param user
	 */
	public Order(User user) {
		this.user = user;
		
		//Quand on initialise un panier, le statut est à En Cours
		this.status = StatusValue.IN_PROGRESS;
		//Date de la commande à null (sera renseigné
		this.date = null;	
		//Par défaut id à 0
		this.idOrder = 0;
		//Le client sera renseigné lors de la validation de la commande
		this.customer = null;
		//Liste des lignes de commandes pour l'instant vide
		this.lstLineOrder = new ArrayList<LineOrder>();
	}
	
	/**
	 * Constructeur sans le client
	 * @param idOrder
	 * @param status
	 * @param date
	 * @param user
	 */
	public Order(int idOrder, StatusValue status, LocalDateTime date, User user) {
		this.idOrder = idOrder;
		this.status = status;
		this.date = date;
		this.user = user;
		this.customer = null;
		this.lstLineOrder = new ArrayList<LineOrder>();
	}
	
	/**
	 * Constructeur avec le client
	 * @param idOrder
	 * @param status
	 * @param date
	 * @param user
	 * @param customer
	 */
	public Order(int idOrder, StatusValue status, LocalDateTime date, User user, Customer customer) {
		this.idOrder = idOrder;
		this.status = status;
		this.date = date;
		this.user = user;
		this.customer = customer;
		this.lstLineOrder = new ArrayList<LineOrder>();
	}

	/*
	 * Getters et Setters
	 */
	public int getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}	
	public StatusValue getStatus() {
		return status;
	}
	public void setStatus(StatusValue status) {
		this.status = status;
	}

	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
		public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public List<LineOrder> getLstLineOrder() {
		return lstLineOrder;
	}
	public void setLstLineOrder(List<LineOrder> lstLineOrder) {
		this.lstLineOrder = lstLineOrder;
	}
	
	/**
	 * Fonction qui calcule le montant total de la commande
	 * @return
	 */
	public double getTotalAmount() {
		double totalAmount = 0;
		//On calcule le montant total de la commande
		for (LineOrder lo : lstLineOrder) {
			totalAmount = totalAmount + lo.getAmountLineOrder();
		}

		return totalAmount;
	}
	
	/**
	 * Fonction qui ajoute une ligne de commande au panier
	 */
	public void addLineOrderToLst(LineOrder lo) {
		lstLineOrder.add(lo);
	}
	
	/**
	 * Fonction qui met à jour la quantité de la ligne de commande de la liste
	 * @param lineOrder
	 * @param qty
	 */
	public void majQuantityLineOrder(LineOrder lineOrder, int qty) {
		//On calcule le montant total de la commande
		for (LineOrder lo : lstLineOrder) {
			if (lo.getOrder_id() == lineOrder.getOrder_id() && lo.getTraining().getIdTraining() == lineOrder.getTraining().getIdTraining()) {
				lo.setQuantity(qty);
			}
		}
	}
	
	/**
	 * Fonction qui retire une ligne de commande du panier
	 */
	public void delLineOrderLst(LineOrder lineOrderToDel) {
		//Je m'étais notée sur un fichier d'aide java cette méthode moderne removeIf pour effacer d'un tableau sans que ça plante (car sinon NullPointerException quand on supprime en parcourant)
		//Je l'ai adaptée 
		lstLineOrder.removeIf(lineOrder -> 
			lineOrder.getOrder_id() == lineOrderToDel.getOrder_id() &&
			lineOrder.getTraining().getIdTraining() == lineOrderToDel.getTraining().getIdTraining());
	}
	
	/**
	 * Fonction qui valide le panier et passe la commande
	 */
	public void validate(Customer customerOrder) {
		date = LocalDateTime.now();
		status = StatusValue.ORDERED;
		customer = customerOrder;
	}
	
	/**
	 * Fonction qui renvoie le code (pour la base de données) correspondant au statut
	 * @return
	 */
	public int getCodeStatusValue() {
		//On convertit le StatusValue en code
		int codeStatus = 0;
		if (getStatus() == StatusValue.ORDERED) codeStatus = 1;
		return codeStatus;
	}	
	
	/**
	 * Méthode toString pour afficher l'objet Order
	 */
	@Override
	public String toString() {
		//Formatter pour afficher la date au format français
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm:ss", Locale.FRENCH);

        // Formatage
        String dateFormatee = "";
        if (date != null) {
        	dateFormatee = date.format(formatter);
        }
        
		String strOrder = "********************************************************************************\n";
		strOrder += this.getStatus() == StatusValue.IN_PROGRESS ? "Panier" : "Commande";
		strOrder += " Id : " + idOrder + "\n";
		if (this.getStatus() == StatusValue.ORDERED) {
			strOrder += "Date de la commande : " + dateFormatee + "\n";
			if (customer != null)	strOrder += "Client concerné par la commande :\n" + customer;
		}
		strOrder += "*******Formations commandées :\n";
		for (LineOrder lo : lstLineOrder) {
			strOrder += lo+"\n";
		}
		strOrder += "Montant total : " + getTotalAmount() + "\n";
		strOrder += "********************************************************************************\n";
		return strOrder;
	}
	
}
