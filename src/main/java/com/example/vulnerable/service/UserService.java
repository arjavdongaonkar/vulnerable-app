package com.example.vulnerable.service;

import com.example.vulnerable.model.User;
import com.example.vulnerable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    // SAST Vulnerability #1: Hardcoded credentials
    private static final String ADMIN_PASSWORD = "admin123456";
    private static final String DB_PASSWORD = "P@ssw0rd2024";
    private static final String API_KEY = "sk_live_51234567890abcdefghijklmnop";

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // SAST Vulnerability #2: SQL Injection via string concatenation
    public List<User> searchUsersByName(String username) {
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        Query query = entityManager.createNativeQuery(sql, User.class);
        return query.getResultList();
    }

    // SAST Vulnerability #3: SQL Injection in dynamic query
    public List<User> searchUsersByRole(String role) {
        String sql = "SELECT u FROM User u WHERE u.role = '" + role + "'";
        Query query = entityManager.createQuery(sql);
        return query.getResultList();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // Hardcoded admin bypass
            if (username.equals("admin") && password.equals(ADMIN_PASSWORD)) {
                return true;
            }
            return user.getPassword().equals(password);
        }
        return false;
    }

    public String getApiKey() {
        return API_KEY;
    }
}
