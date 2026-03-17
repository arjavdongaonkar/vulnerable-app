package com.example.vulnerable.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Product search service with SQL injection vulnerability
 */
public class ProductService {

    private Connection connection;

    public ProductService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Search products by name - VULNERABLE TO SQL INJECTION
     */
    public ResultSet searchProducts(String productName) throws SQLException {
        Statement stmt = connection.createStatement();

        // SQL Injection vulnerability - user input directly concatenated
        String query = "SELECT * FROM products WHERE name = '" + productName + "'";

        return stmt.executeQuery(query);
    }

    /**
     * Get product by ID - VULNERABLE TO SQL INJECTION
     */
    public ResultSet getProductById(String productId) throws SQLException {
        Statement stmt = connection.createStatement();

        // Another SQL injection - integer parameter but still vulnerable
        String query = "SELECT * FROM products WHERE id = " + productId;

        return stmt.executeQuery(query);
    }

    /**
     * Delete product - VULNERABLE TO SQL INJECTION
     */
    public int deleteProduct(String productName) throws SQLException {
        Statement stmt = connection.createStatement();

        // SQL injection in DELETE statement
        String query = "DELETE FROM products WHERE name = '" + productName + "'";

        return stmt.executeUpdate(query);
    }

    /**
     * Update product price - VULNERABLE TO SQL INJECTION
     */
    public int updatePrice(String productId, String newPrice) throws SQLException {
        Statement stmt = connection.createStatement();

        // SQL injection in UPDATE statement
        String query = "UPDATE products SET price = " + newPrice + " WHERE id = " + productId;

        return stmt.executeUpdate(query);
    }
}
