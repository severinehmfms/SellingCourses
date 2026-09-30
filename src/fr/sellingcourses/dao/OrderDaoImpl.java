package fr.sellingcourses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import fr.sellingcourses.business.SellingCourses;
import fr.sellingcourses.business.SellingCoursesImpl;
import fr.sellingcourses.entities.LineOrder;
import fr.sellingcourses.entities.Order;
import fr.sellingcourses.entities.User;
import fr.sellingcourses.entities.Order.StatusValue;
import fr.sellingcourses.utils.Functions;

public class OrderDaoImpl implements OrderDao{

	/**
	 * Fonction qui permet de créer un objet Order à partir d'un ResultSet
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public Order getOrderFromDb(ResultSet resultSet) throws SQLException {
		SellingCourses service = new SellingCoursesImpl();	
		
		Order order = null;
		try {
			int rsId = resultSet.getInt("order_id"); 
			int rsStatus = resultSet.getInt("order_status"); 
			
			//On convertit en LocalDateTime la date récupérée au format sql
			LocalDateTime rsDate = null;
			if (resultSet.getTimestamp("order_date") != null) {
				resultSet.getTimestamp("order_date").toLocalDateTime();
			}
			
			//En fait on en a pas besoin j'ai décidé de le calculer dans getTotalAmount
			//int rsTotalAmount = resultSet.getInt("total_amount");
			
			//On va récupérer l'objet utilisateur et l'objet customer correspondant à l'id
			String rsLoginUser = resultSet.getString("login_app");
			int rsIdCustomer = resultSet.getInt("customer_id");
			
			//On récupère l'utilisateur associé à ce login
			User user = service.findUserByLogin(rsLoginUser);
						
			//On récupère le StatusValue associé au code statut en base
			StatusValue statusValue = StatusValue.IN_PROGRESS;
			if (rsStatus == 1) statusValue = StatusValue.ORDERED;
			
			//TODO On récupère l'objet client associé à customer_id si customer_id différent de 0 (A rajouter quand la partie optionnelle client sera prête)
			
			
			order =  new Order(rsId, statusValue, rsDate, user);		
			
			//On récupère la liste des lignes de commandes associées à cette commande
			ArrayList<LineOrder> lstLineOrder = (ArrayList<LineOrder>) service.findAllLineOrderByOrder(order);
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
		//On convertit le StatusValue en code
		int codeStatus = 0;
		if (order.getStatus() == StatusValue.ORDERED) codeStatus = 1;
		
		String str = "INSERT INTO orderapp (order_status, order_date, total_amount, login_app, customer_id) VALUES (?,?,?,?,?)";
		try (PreparedStatement ps = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS)){
			ps.setInt(1, codeStatus);
			
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
                    System.out.println("Nouvel ID généré : " + id);
                    
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
}
