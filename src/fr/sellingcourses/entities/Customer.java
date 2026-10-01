package fr.sellingcourses.entities;

public class Customer {
	private int idCustomer;
	private String name;
	private String firstName;
	private String mail;
	private String adresse;
	private String phone;
	
	/**
	 * Constructeur quand on ne connait pas encore l'id
	 * @param name
	 * @param firstName
	 * @param mail
	 * @param adresse
	 * @param phone
	 */
	public Customer(String name, String firstName, String mail, String adresse, String phone) {
		this.name = name;
		this.firstName = firstName;
		this.mail = mail;
		this.adresse = adresse;
		this.phone = phone;
		
		this.idCustomer = 0;
	}
	
	
	/**
	 * Constructeur complet
	 * @param idCustomer
	 * @param name
	 * @param firstName
	 * @param mail
	 * @param adresse
	 * @param phone
	 */
	public Customer(int idCustomer, String name, String firstName, String mail, String adresse, String phone) {
		this.idCustomer = idCustomer;
		this.name = name;
		this.firstName = firstName;
		this.mail = mail;
		this.adresse = adresse;
		this.phone = phone;
	}
	
	/*
	 * Getters et Setters
	 */
	public int getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		String str = idCustomer != 0 ? "Id : " + idCustomer + " " : "";
		str += name + " " + firstName + " - Email " + mail + " - Tél " + phone + "\nAdresse " + adresse + "\n";
		return str; 
	}
	
	
}
