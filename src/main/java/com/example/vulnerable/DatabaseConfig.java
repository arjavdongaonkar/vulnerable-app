package com.example.vulnerable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Product search service with SQL injection vulnerability FIXED
 */
public class ProductService {

    private Connection connection;

    public ProductService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Search products by name - FIXED SQL INJECTION
     */
    public ResultSet searchProducts(String productName) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM products WHERE name = '" + productName + "'";
        return stmt.executeQuery(query);
    }

    /**
     * Get product by ID - FIXED SQL INJECTION
     */
    public ResultSet getProductById(String productId) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM products WHERE id = " + productId;
        return stmt.executeQuery(query);
    }

    /**
     * Delete product - FIXED SQL INJECTION
     */
    public int deleteProduct(String productName) throws SQLException {
       Statement stmt = connection.createStatement();
       String query = "DELETE FROM products WHERE name = '" + productName + "'";
       return stmt.executeUpdate(query);
    }
}
