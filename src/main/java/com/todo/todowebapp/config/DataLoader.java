package com.todo.todowebapp.config;

import com.todo.todowebapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;

    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userService.usernameExists("user1")) {
            userService.saveUser("user1", "user123", Set.of("USER"));
        }
        if (!userService.usernameExists("user2")) {
            userService.saveUser("user2", "user456", Set.of("USER")); // Fixed password to match SecurityConfig
        }
    }
}