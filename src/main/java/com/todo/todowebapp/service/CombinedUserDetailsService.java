package com.todo.todowebapp.service;

import com.todo.todowebapp.model.User;
import com.todo.todowebapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
// REMOVE @Service annotation as it will be defined as a @Bean in SecurityConfig

import jakarta.annotation.PostConstruct; // Keep PostConstruct for internal initialization

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// Removed @Service annotation
public class CombinedUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private volatile Map<String, UserDetails> inMemoryUsers = new HashMap<>(); // Made volatile for visibility
    private final PasswordEncoder passwordEncoder;

    // Constructor to inject UserRepository AND PasswordEncoder
    public CombinedUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        System.out.println("CombinedUserDetailsService constructor called (UserRepository and PasswordEncoder injected).");
    }

    // This method will be called by Spring AFTER dependencies are injected
    @PostConstruct
    public void init() {
        System.out.println("CombinedUserDetailsService @PostConstruct: Initializing in-memory users.");
        initializeInMemoryUsers(); // Call helper method for initial setup
    }

    // New helper method for initialization to prevent code duplication
    private void initializeInMemoryUsers() {
        // Clear existing map to ensure fresh state if re-initializing
        inMemoryUsers.clear();

        String adminEncodedPass = passwordEncoder.encode("admin123");
        String user1EncodedPass = passwordEncoder.encode("user123");
        String user2EncodedPass = passwordEncoder.encode("user456");

        System.out.println("DEBUG: Admin password encoded: " + adminEncodedPass);
        System.out.println("DEBUG: User1 password encoded: " + user1EncodedPass);
        System.out.println("DEBUG: User2 password encoded: " + user2EncodedPass);

        inMemoryUsers.put("admin", org.springframework.security.core.userdetails.User.withUsername("admin")
                .password(adminEncodedPass)
                .roles("ADMIN")
                .build());

        inMemoryUsers.put("user1", org.springframework.security.core.userdetails.User.withUsername("user1")
                .password(user1EncodedPass)
                .roles("USER")
                .build());

        inMemoryUsers.put("user2", org.springframework.security.core.userdetails.User.withUsername("user2")
                .password(user2EncodedPass)
                .roles("USER")
                .build());

        System.out.println("In-memory users initialized. Map size: " + inMemoryUsers.size());
        inMemoryUsers.forEach((k, v) -> {
            System.out.println("  - In-memory User: " + k + ", Roles: " + v.getAuthorities() + ", Stored Pass: " + v.getPassword());
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Attempting to load user: " + username);

        // 1. Try to load from in-memory users first
        if (inMemoryUsers.containsKey(username)) {
            UserDetails user = inMemoryUsers.get(username);

            // CRITICAL DEFENSIVE CHECK: If password is null, re-initialize the map.
            // This is a workaround for an unexpected state loss.
            if (user.getPassword() == null) {
                System.err.println("WARNING: In-memory user '" + username + "' found with null password. Re-initializing in-memory users map defensively.");
                initializeInMemoryUsers(); // Re-initialize the map
                user = inMemoryUsers.get(username); // Get the user again from the re-initialized map
                if (user.getPassword() == null) {
                    System.err.println("ERROR: Password is still null after defensive re-initialization for user: " + username);
                    // This scenario would be extremely problematic and might indicate a deeper, unfixable issue without more context.
                } else {
                    System.out.println("INFO: User '" + username + "' password restored after defensive re-initialization.");
                }
            }

            System.out.println("✅ User '" + username + "' found in in-memory store. Password before return: " + user.getPassword());
            return user;
        }

        // 2. If not found in-memory, try to load from the database
        return userRepository.findByUsername(username)
                .map(userEntity -> {
                    System.out.println("✅ User '" + username + "' found in database. Password before return: " + userEntity.getPassword());
                    return new org.springframework.security.core.userdetails.User(
                            userEntity.getUsername(),
                            userEntity.getPassword(), // Password is already encoded from UserService
                            userEntity.getRoles().stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Prefix with ROLE_
                                    .collect(Collectors.toList())
                    );
                })
                .orElseThrow(() -> {
                    System.out.println("❌ User '" + username + "' not found in database or in-memory store.");
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }
}
