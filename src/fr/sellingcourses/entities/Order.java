package fr.sellingcourses.entities;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui représente le panier, puis quand le statut est validé, la commande
 */

public class Order {
	
	//Valeurs que peuvent prendre le statut
	public static enum StatusValue {
		IN_PROGRESS, ORDERED
	}
	
	private int idOrder;					//Id (en base)
	private StatusValue status;				//Statut (EN_COURS = Panier, VALIDEE = Commande)
	private LocalDateTime date; 			//Date et heure du passage réel de la commande LocalDateTime.now()
	private double totalAmount; 			//Montant total du panier/de la commande
	
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
		//TODO Montant de la commande ( A calculer si on crée un objet panier avec déjà des lignes de commandes)
		this.totalAmount = 0;		
		//Par défaut id à 0
		this.idOrder = 0;
		//Le client sera renseigné lors de la validation de la commande
		this.customer = null;
		//Liste des lignes de commandes pour l'instant vide
		this.lstLineOrder = new ArrayList();
	}
	
	/**
	 * Constructeur sans le client
	 * @param idOrder
	 * @param status
	 * @param date
	 * @param totalAmount
	 * @param user
	 * @param customer
	 * @param lstLineOrder
	 */
	public Order(int idOrder, StatusValue status, LocalDateTime date, double totalAmount, User user) {
		this.idOrder = idOrder;
		this.status = status;
		this.date = date;
		this.totalAmount = totalAmount;
		this.user = user;
		this.customer = null;
		this.lstLineOrder = new ArrayList();
	}
	
	/**
	 * Constructeur complet
	 * @param idOrder
	 * @param status
	 * @param date
	 * @param totalAmount
	 * @param user
	 * @param customer
	 * @param lstLineOrder
	 */
	public Order(int idOrder, StatusValue status, LocalDateTime date, double totalAmount, User user, Customer customer) {
		this.idOrder = idOrder;
		this.status = status;
		this.date = date;
		this.totalAmount = totalAmount;
		this.user = user;
		this.customer = customer;
		this.lstLineOrder = new ArrayList();
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
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
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

	@Override
	public String toString() {
		return "Order [idOrder=" + idOrder + ", status=" + status + ", date=" + date + ", totalAmount=" + totalAmount
				+ ", user=" + user + ", customer=" + customer + ", lstLineOrder=" + lstLineOrder + "]";
	}
	
	
	
}
