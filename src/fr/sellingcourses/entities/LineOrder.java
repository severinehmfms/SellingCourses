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
	private double amountLineOrder;
	
	/**
	 * Constructeur 
	 * @param training
	 * @param quantity
	 */
	public LineOrder(Training training, int quantity, int order_id) {
		this.training = training;
		this.quantity = quantity;
		this.order_id = order_id;
		//Calcul du sous total de cette ligne de commande
		this.amountLineOrder = training.getPrice() * quantity;
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
	public double getAmountLineOrder() {
		return amountLineOrder;
	}
	public void setAmountLineOrder(double amountLineOrder) {
		this.amountLineOrder = amountLineOrder;
	}
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	@Override
	public String toString() {
		return "LineOrder [training=" + training.getName() + ", quantity=" + quantity + ", order_id=" + order_id
				+ ", amountLineOrder=" + amountLineOrder + "]";
	}	
}
