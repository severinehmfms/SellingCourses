package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.sellingcourses.business.SellingCourses;
import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.entities.Customer;
import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.User;
import fr.sellingcourses.entities.Order.StatusValue;
import fr.sellingcourses.utils.Functions;

public class OrderDaoImpl implements OrderDao{

	private Connection connection; 
	
	/**
	 * Fonction qui permet de créer un objet Order à partir d'un ResultSet
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public Order getOrderFromDb(ResultSet resultSet) throws SQLException {
		Order order = null;
		//Obligée de récupérer la connection pour pouvoir utiliser les méthodes des DAO
		this.connection = DatabaseConnection.getConnection();
		//Appels des DAO nécessaires
		UserDao userDao = new UserDaoImpl();
		LineOrderDao lineOrderDao = new LineOrderDaoImpl();
		CustomerDao customerDao = new CustomerDaoImpl();
		
		try {
			int rsId = resultSet.getInt("order_id"); 
			int rsStatus = resultSet.getInt("order_status"); 
			
			//On convertit en LocalDateTime la date récupérée au format sql
			LocalDateTime rsDate = null;
			if (resultSet.getTimestamp("order_date") != null) {
				rsDate = resultSet.getTimestamp("order_date").toLocalDateTime();
			}
			
			//En fait on en a pas besoin j'ai décidé de le calculer dans getTotalAmount
			//int rsTotalAmount = resultSet.getInt("total_amount");
			
			//On va récupérer l'objet utilisateur et l'objet customer correspondant à l'id
			String rsLoginUser = resultSet.getString("login_app");
			int rsIdCustomer = resultSet.getInt("customer_id");
			
			//On récupère l'utilisateur associé à ce login
			User user = userDao.findUserByLogin(connection, rsLoginUser);
						
			//On récupère le StatusValue associé au code statut en base
			StatusValue statusValue = StatusValue.IN_PROGRESS;
			if (rsStatus == 1) statusValue = StatusValue.ORDERED;
			
			//On récupère l'objet client associé à customer_id si customer_id différent de 0
			Customer customer = customerDao.findById(connection, rsIdCustomer);
			
			order =  new Order(rsId, statusValue, rsDate, user, customer);
			
			//On récupère la liste des lignes de commandes associées à cette commande
			ArrayList<LineOrder> lstLineOrder = (ArrayList<LineOrder>) lineOrderDao.findAllByOrder(connection, order);
			
			order.setLstLineOrder(lstLineOrder);			
	   
		}catch(SQLException e) {
			Functions.printLogs(Functions.LOG_FILE, "ERREUR lors de la création d'un objet Training via le ResultSet.");
			e.printStackTrace();
		}
		return order;
	}

	/**
	 * Fonction qui renvoie l'order pour un utilisateur donné et un statut donné
	 */
	@Override
	public Order findOrderInProgressByUser(Connection connection, String login) {
		//On recherche uniquement le statut en cours (pour avoir un seul résultat)
		int codeStatus = 0;
		
		String sql = "SELECT order_id, order_status, order_date, total_amount, login_app, customer_id  FROM orderapp WHERE login_app = ? AND order_status = ? ";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, login);
			ps.setInt(2, codeStatus);

	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {

	        		Functions.printLogs(Functions.LOG_FILE, "Récupération d'un panier par son id bien effectuée");
	        		//On récupère l'objet Order à partir du resultSet et on le renvoie
	        		return getOrderFromDb(rs);
	             }
	        }
	    } catch (SQLException e) {
	    	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération d'un panier par son id.");
	    	e.printStackTrace();
	    }
		Functions.printLogs(Functions.LOG_FILE, "Aucun panier trouvé pour cet utilisateur et ce statut.");
	    return null; // Aucun order trouvé
	}	

	/**
	 * Création d'une ligne de commande en base de données
	 */
	@Override
	public Order create(Connection connection, Order order) {			
		String str = "INSERT INTO orderapp (order_status, order_date, total_amount, login_app, customer_id) VALUES (?,?,?,?,?)";
		try (PreparedStatement ps = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS)){
			//On récupère le code du statut
			ps.setInt(1, order.getCodeStatusValue());
			
			//On convertit la LocalDateTime en timestamp (date time sql)
			Timestamp sqlDateTime = null;
			if (order.getDate()!=null){
				sqlDateTime = Timestamp.valueOf(order.getDate());
			}
			ps.setTimestamp(2, sqlDateTime);
			
			ps.setDouble(3, order.getTotalAmount());
			//On récupère le login de l'utilisateur s'il est renseigné
			String login = "";
			if(order.getUser()!=null) {
				login = order.getUser().getLogin();
			}
			ps.setString(4, login);
			//On récupère l'id du client s'il est renseigné
			if(order.getCustomer()!=null) {
				ps.setInt(5, order.getCustomer().getIdCustomer());
			}else {
				//On met à null la colonne customer_id (sinon plantage sql du aux contraintes de clés étrangères)
				ps.setNull(5, java.sql.Types.VARCHAR);
			}			
			
			if( ps.executeUpdate() == 0)
				throw new SQLException("Échec de l'insertion, aucune ligne affectée.");
			
            // Récupération de l'ID généré
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    //System.out.println("Nouvel ID généré : " + id);
                    
                    order.setIdOrder(id);
                } else {
                	Functions.printLogs(Functions.LOG_FILE, "ERREUR Échec de la récupération de l'ID généré lors de la création d'un Order.");
        	    	
                    throw new SQLException("Échec de la récupération de l'ID généré.");
                }
            }
			return order;
			
		}catch (SQLException e) {
			e.printStackTrace();
		}	
		return order;
	}

	@Override //order_status, order_date, total_amount, login_app, customer_id
	public boolean update(Connection connection, Order order) {
		String str = "UPDATE orderapp SET order_status=?, order_date=?, total_amount=?, login_app=?, customer_id=? WHERE order_id=? ";
		try (PreparedStatement ps = connection.prepareStatement(str)){
			//On récupère le code qui correspond au statut 
			ps.setInt(1, order.getCodeStatusValue());
			
			//On convertit la LocalDateTime en timestamp (date time sql)
			Timestamp sqlDateTime = null;
			if (order.getDate()!=null){
				sqlDateTime = Timestamp.valueOf(order.getDate());
			}
			ps.setTimestamp(2, sqlDateTime);
			
			ps.setDouble(3, order.getTotalAmount());
			
			ps.setString(4, order.getUser().getLogin());
			
			ps.setInt(5, order.getCustomer().getIdCustomer());
			
			ps.setInt(6, order.getIdOrder());
			
			// On récupère le nombre de lignes affectées par la requête
			int nbLignes = ps.executeUpdate(); 
			
			if (nbLignes == 0) { 
				Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la mise à jour d'un objet order en base.");
				
				throw new SQLException("Échec de la mise à jour, aucune ligne affectée."); 
			}
			
			return true;
            
		}catch (SQLException e) {
			Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors du PreparedStatement de la mise à jour d'un order en base.");
			
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Order> findLstOrderByUser(Connection connection, String login, StatusValue statusValue) {
		//On récupère le StatusValue associé au code statut en base
		int codeStatus = 0;		
		if (statusValue == StatusValue.ORDERED) codeStatus = 1;
		
		String sql = "SELECT order_id, order_status, order_date, total_amount, login_app, customer_id  FROM orderapp WHERE login_app = ? AND order_status = ?";
        List<Order> lstOrder = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)){
    		ps.setString(1, login);  
    		ps.setInt(2, codeStatus);  
    		try(ResultSet rs = ps.executeQuery()) {
            	while (rs.next()) {
            		//On récupère l'objet correspondant à ce resultSet
            		Order order = getOrderFromDb(rs);
            		if (order != null) lstOrder.add(order);
            	}
    		}
        } catch (SQLException e) {
        	Functions.printLogs(Functions.LOG_FILE, "ERREUR SQL lors de la récupération de la liste des commandes pour un login et un statut.");
            e.printStackTrace();
        }
        Functions.printLogs(Functions.LOG_FILE, "Récupération de la liste des commandes pour un login et un statut.");
        return lstOrder;
	}
}
