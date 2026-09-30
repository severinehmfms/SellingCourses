package fr.sellingcourses.entities;

public class LineOrder {

	/**
	 * LineOrder = Ligne de commande d'un Order (panier, commande). 
	 * Contient une formation et une quantité
	 */
	private Training training;
	private int quantity;
	//On stocke uniquement l'id pour éviter que ça tourne en boucle ;-)
	private int order_id;
	
	/**
	 * Constructeur 
	 * @param training
	 * @param quantity
	 * @param order_id
	 */
	public LineOrder(Training training, int quantity, int order_id) {
		this.training = training;
		this.quantity = quantity;
		this.order_id = order_id;
	}
	
	/*
	 * Getters et Setters
	 */
	public Training getTraining() {
		return training;
	}
	public void setTraining(Training training) {
		this.training = training;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	
	//Calcul du sous total de cette ligne de commande
	public double getAmountLineOrder() {
		return training.getPrice() * quantity;
	}

	@Override
	public String toString() {
		String strLineOrder = "";
		strLineOrder += "Formation : " + training.getName() + " - ";
		strLineOrder += "Quantité : " + quantity + " - ";
		strLineOrder += "Montant total : " + this.getAmountLineOrder() + "\n";
		return strLineOrder;
	}	
}
