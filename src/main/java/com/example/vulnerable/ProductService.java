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
        String query = "SELECT * FROM products WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, productName);
        return pstmt.executeQuery();
    }

    /**
     * Get product by ID - FIXED SQL INJECTION
     */
    public ResultSet getProductById(String productId) throws SQLException {
        String query = "SELECT * FROM products WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, productId);
        return pstmt.executeQuery();
    }

    /**
     * Delete product - FIXED SQL INJECTION
     */
    public int deleteProduct(String productName) throws SQLException {
        String query = "DELETE FROM products WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, productName);
        return pstmt.executeUpdate();
    }
}
