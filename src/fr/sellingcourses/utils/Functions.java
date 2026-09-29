package fr.sellingcourses.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Fonctions plus génériques pas spécifiques aux avions
 */
public class Functions {
	
	public static final String LOG_FILE = "log.txt";

	/**
	 * Fonction qui écrit une ligne de log dans le fichier 
	 * @param file
	 * @param e
	 */
	public static void printLogs(String file, Exception exceptionRecup){
		//On récupère l'erreur de l'exception pour ensuite la mettre dans le fichier de log (ce code de conversion exception en chaine trouvé sur google)
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exceptionRecup.printStackTrace(pw);
		String sStackTrace = sw.toString();
		
		//Deuxième argument de FileWriter à true pour indiquer qu'on veut écrire à la fin du fichier
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
			//System.out.println(new File(file).getAbsolutePath());
		    bw.write(sStackTrace);
		    bw.newLine();

		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Fonction qui écrit une ligne de log dans le fichier 
	 * @param file
	 * @param log
	 */
	public static void printLogs(String file, String log){
		//Deuxième argument de FileWriter à true pour indiquer qu'on veut écrire à la fin du fichier
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
			//System.out.println(new File(file).getAbsolutePath());
		    bw.write(log);
		    bw.newLine();

		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Fonction qui va afficher le menu proposé à l'utilisateur (en paramètre)
	 * @param menu (String[])
	 * @return  la saisie de l'utilisateur (int)
	 */
	public static int ask_user_choice(Scanner scanner, String[] menu) {
		//On récupère le tableau correspondant au menu en paramètre et on construit la chaine qui va bien
		String menuStr = "\nMENU :\n";
		menuStr += 0 + ": Quitter \n";
		for (int i = 0; i < menu.length; i++) {
			menuStr += i+1 + ": " + menu[i] + "\n";
		}
		
		//On récupère la saisie de l'utilisateur entre 0 et la taille du menu
		int choiceInt = Functions.input_int(scanner, menuStr, 0, menu.length);
		return choiceInt;
	}
	
	
	/**
	 * Surcharge de la méthode input_int pour ne pas rentrer de valeur minimum et maximum
	 * @param prompt
	 * @return
	 */
	public static int input_int(Scanner scanner, String prompt) {
	    return input_int(scanner, prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 *  Surcharge de la méthode pour ne pas rentrer de valeur maximum
	 * @param prompt
	 * @param min_val
	 * @return
	 */
	public static int input_int(Scanner scanner, String prompt, int min_val) {
	    return input_int(scanner, prompt, min_val, Integer.MAX_VALUE);
	}
	
	/**
	 * Fonction générique pour faire saisir à l'utilisateur un int entre min_val et max_val
	 * @param prompt
	 * @param min_val
	 * @param max_val
	 * @return
	 */
	public static int input_int(Scanner scanner, String prompt, int min_val, int max_val) {
		int input_int_user = 0;
		boolean is_valid_input = false;
	    while (!is_valid_input) {
	    	System.out.println(prompt);
	    	String input_user = scanner.nextLine();

	    	if (! input_user.matches("\\d+")) {
	        	System.out.println("ERREUR - Vous devez saisir un entier.");
	        } else {
	        	input_int_user = Integer.parseInt(input_user);

	            if (input_int_user < min_val || input_int_user > max_val) {
	            	System.out.println("ERREUR - La saisie doit être comprise entre " + min_val + " et " + max_val);
	            } else {
	            	is_valid_input = true;
	            }
	        }
	    }
		return input_int_user;
	}
	
	/**
	 * Surcharge de la méthode input_double pour ne pas rentrer de valeur minimum et maximum
	 * Double.MIN_VALUE représente la plus petite valeur positive strictement supérieure à zéro qu’un double peut stocker
	 * @param prompt
	 * @return
	 */
	public static double input_double(Scanner scanner, String prompt) {
	    return input_double(scanner, prompt, Double.MIN_VALUE, Double.MAX_VALUE);
	}
	
	/**
	 * Fonction générique pour faire saisir à l'utilisateur un int entre min_val et max_val
	 * @param prompt
	 * @param min_val
	 * @param max_val
	 * @return
	 */
	public static double input_double(Scanner scanner, String prompt, double min_val, double max_val) {
		double input_double_user = 0;
		boolean is_valid_input = false;
	    while (!is_valid_input) {
	    	System.out.println(prompt);
	    	String input_user = scanner.nextLine();

	    	//On remplace les virgules par des points
	    	input_user = input_user.trim().replace(',', '.');

	    	//On vérifie si le format de saisie correspond à un double
	    	if (! input_user.matches("\\d+(\\.\\d+)?")) {
	    		System.out.println("ERREUR - Vous devez saisir un nombre décimal.");
	    	} else {	    	
	        	input_double_user = Double.parseDouble(input_user);

	            if (input_double_user < min_val || input_double_user > max_val) {
	            	System.out.println("ERREUR - La saisie doit être comprise entre " + min_val + " et " + max_val);
	            } else {
	            	is_valid_input = true;
	            }
	        }
	    }
		return input_double_user;
	}
	
	public static LocalDate input_date_fr(Scanner scanner, String prompt) {
		LocalDate date = null;
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
	    boolean is_valid_input = false;
	    while (!is_valid_input) {
	        System.out.println(prompt);
	        String input_user = scanner.nextLine();

	        input_user = input_user.trim();

	        // Vérification du format dd/MM/yyyy
	        if (!input_user.matches("\\d{2}/\\d{2}/\\d{4}")) {
	            System.out.println("ERREUR - La saisie doit être une date au format dd/MM/yyyy.");
	        }else {
	        	try {
	        		date = LocalDate.parse(input_user, formatter);
		        	is_valid_input = true;
	            } catch (DateTimeParseException e) {
	                System.out.println("ERREUR - Date inexistante. Veuillez saisir une date existante.");
	            }
	        }
	    }
        return date;
	}
	
	//Fonction input_date modifiée pour l'exercice 2.2 Java avancée
	public static Date input_date_fr_exceptions(Scanner scanner, String prompt) throws Exception{

	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    
	    //setLenient à false dit à SimpleDateFormat de ne pas être tolérant
	    formatter.setLenient(false);

	    Date date = null;
	    boolean isValid = false;

	    while (!isValid) {

	        try {

	            System.out.println(prompt);

	            String input_user = scanner.nextLine().trim();

	            if (!input_user.matches("\\d{2}/\\d{2}/\\d{4}")) {
	                throw new ParseException("La date doit être au format dd/MM/yyyy", 0);
	            }

	            date = formatter.parse(input_user);
	            isValid = true;

	        } catch (ParseException e) {
	        	e.printStackTrace();
	            System.out.println(
	                "ERREUR - La saisie doit être une date valide au format dd/MM/yyyy."
	            );
	        }
	    }

	    return date;
	}	

	/** 
	 * Fonction qui permet de demander une saisie à l'utilisateur
	 * prompt = Prompt qui demande à l'utilisateur de saisir 
	 */
	public static String input_string(Scanner scanner, String prompt) {
		boolean is_input_ok = false;
		String input_user = "";
		while (!is_input_ok) {
			System.out.println(prompt);
			input_user = scanner.nextLine();
			
			if (input_user.trim().isEmpty()) {
				System.out.println("ERREUR - La saisie ne peut pas être à vide");
				is_input_ok = false;
			}else {		
				is_input_ok = true;
			}
		}
		return input_user;
	}

	/** 
	 * Fonction qui permet de demander une saisie à l'utilisateur
	 * prompt = Prompt qui demande à l'utilisateur de saisir 
	 */
	public static String input_string(Scanner scanner, String prompt, boolean isEmptyAutorise) {
		boolean is_input_ok = false;
		String input_user = "";
		while (!is_input_ok) {
			System.out.println(prompt);
			input_user = scanner.nextLine();
			
			if (!isEmptyAutorise && input_user.trim().isEmpty()) {
				System.out.println("ERREUR - La saisie ne peut pas être à vide");
				is_input_ok = false;
			}else {		
				is_input_ok = true;
			}
		}
		return input_user;
	}
	
	/** 
	 * Fonction qui permet de demander une saisie à l'utilisateur : Les attendus pour oui ou non sont stockés dans les ensembles yes_answers et no_answers
	 * prompt = Prompt qui demande à l'utilisateur de saisir 
	 */
	public static boolean input_yes_no(Scanner scanner, String prompt) {
		boolean is_input_ok = false;
		
		Set<String> yes_answers = new HashSet<>(Arrays.asList(
			    "oui",
			    "o",
			    "yes",
			    "y"
			));
		Set<String> no_answers = new HashSet<>(Arrays.asList(
			    "non",
			    "n",
			    "no"
			));
		String input_user = "";
		//Tant que la saisie n'est pas conforme à ce qui est attendu, on redemande à l'utilisateur
		while (!is_input_ok) {
			System.out.println(prompt);
			input_user = scanner.nextLine();
			//On enlève les espaces
			input_user = input_user.trim();
			//On met en minuscules
			input_user = input_user.toLowerCase();
			if (input_user.trim().isEmpty()) {
				System.out.println("ERREUR - La saisie ne peut pas être à vide");
				is_input_ok = false;
			}else if ( (!yes_answers.contains(input_user.toLowerCase())) && (!no_answers.contains(input_user.toLowerCase())) ){
				System.out.println("ERREUR - Saisie incorrecte ! ");
				is_input_ok = false;
			}else {		
				is_input_ok = true;
			}
		}
		//Si l'utilisateur a répondu oui, on renvoie vrai, sinon false
		if (yes_answers.contains(input_user.toLowerCase())) {
			return true;
		}else {
			return false;
		}
	}
	
}