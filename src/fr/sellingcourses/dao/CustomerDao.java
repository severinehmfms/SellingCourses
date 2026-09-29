package fr.sellingcourses.dao;

import java.sql.Connection;
import java.util.List;

import fr.sellingcourses.entities.Customer;

public interface CustomerDao {
	
	Customer findById(Connection connection, int id);
    List<Customer> findAll(Connection connection);

}
