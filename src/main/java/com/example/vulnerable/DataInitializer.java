package com.example.vulnerable;

import com.example.vulnerable.model.User;
import com.example.vulnerable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize with test data
        userRepository.save(new User("admin", "admin123456", "admin@example.com", "ADMIN"));
        userRepository.save(new User("john", "password123", "john@example.com", "USER"));
        userRepository.save(new User("jane", "qwerty", "jane@example.com", "USER"));
        userRepository.save(new User("bob", "letmein", "bob@example.com", "MANAGER"));
    }
}
