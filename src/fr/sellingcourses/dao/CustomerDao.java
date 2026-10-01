package fr.sellingcourses.dao;

import java.sql.Connection;

import fr.sellingcourses.entities.Customer;

public interface CustomerDao {
	
	Customer findById(Connection connection, int id);
    Customer create(Connection connection, Customer customer);

}
