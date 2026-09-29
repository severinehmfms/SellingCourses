package fr.sellingcourses.exceptions;

public class LoginAlreadyUsedException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public LoginAlreadyUsedException(String msg) {
		super(msg);
	}
	public LoginAlreadyUsedException() {
		super("Ce login existe déjà dans la base de données");
	}
	
}
