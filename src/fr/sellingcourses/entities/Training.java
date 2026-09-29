package fr.sellingcourses.entities;

public class Training {
	
	/**
	 * Formation
	 */
	
	private int idTraining;
	private String name;
	private String description;
	private int length;
	private boolean remoteTraining;
	
	/**
	 * Constructeur quand on ne connait pas encore l'id
	 * @param name
	 * @param description
	 * @param length
	 * @param remoteTraining
	 */
	public Training(String name, String description, int length, boolean remoteTraining) {
		this.name = name;
		this.description = description;
		this.length = length;
		this.remoteTraining = remoteTraining;
		this.idTraining = 0;
	}
	
	/**
	 * Constructeur complet
	 * @param idTraining
	 * @param name
	 * @param description
	 * @param length
	 * @param remoteTraining
	 */
	public Training(int idTraining, String name, String description, int length, boolean remoteTraining) {
		this.idTraining = idTraining;
		this.name = name;
		this.description = description;
		this.length = length;
		this.remoteTraining = remoteTraining;		
	}
	
	/**
	 * Getters et Setters générés automatiquement
	 * @return
	 */
	public int getIdTraining() {
		return idTraining;
	}
	public void setIdTraining(int idTraining) {
		this.idTraining = idTraining;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isRemoteTraining() {
		return remoteTraining;
	}
	public void setRemoteTraining(boolean remoteTraining) {
		this.remoteTraining = remoteTraining;
	}
	
	/**
	 * Méthode toString pour l'affichage
	 */
	public String toString() {
		String str = "Formation " + this.name + " \nDescription : " + this.description + " - Durée : " + this.length + " jours";
		if (this.remoteTraining == true) str += " - Formation en distanciel ";
		else str += " - Formation en présentiel ";
		return str;
	}

}
