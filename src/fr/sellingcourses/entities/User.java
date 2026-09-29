package fr.sellingcourses.entities;

public class User {
	
	/**
	 * User
	 */

	private String login;
	private String password;
	
	/**
	 * Constructeur
	 * @param login
	 * @param password
	 */
	public User(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}
	
	/*
	 * Getters et Setters
	 */
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setLogin(String login) {
		this.login = login;
	}
			
}
