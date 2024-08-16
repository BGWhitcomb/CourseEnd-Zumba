package com.courseend.zumba.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public interface DAO {

	// Method to obtain the singleton instance of the database
	static Database getInstance() {
		return Database.getInstance();
	}

	// Method to get the current database connection
	Connection getConnection();

	// Method to close the current database connection
	void closeConnection();

	// Method to execute an update (e.g., insert, update, delete)
	int executeUpdate(PreparedStatement preparedStatement);

	// Method to execute a query (e.g., select)
	ResultSet executeQuery(PreparedStatement preparedStatement);

}
