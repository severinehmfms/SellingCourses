package fr.sellingcourses.entities;

public class LineOrder {

	/**
	 * LineOrder = Ligne de commande d'un Order (panier, commande). 
	 * Contient une formation et une quantité
	 */
	private Training training;
	private int quantity;
	private double amountLineOrder;
	
	/**
	 * Constructeur 
	 * @param training
	 * @param quantity
	 */
	public LineOrder(Training training, int quantity) {
		super();
		this.training = training;
		this.quantity = quantity;
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
	
}
