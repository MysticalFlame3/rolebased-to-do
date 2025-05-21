package com.todo.todowebapp.controller;

import com.todo.todowebapp.model.User;
import com.todo.todowebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users"; // templates/admin/users.html
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Arrays.asList("USER", "ADMIN"));
        return "admin/addUser"; // templates/admin/addUser.html
    }

    @PostMapping("/add")
    public String addUserSubmit(@ModelAttribute User user, @RequestParam("role") String role, Model model) {
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("errorMessage", "Username '" + user.getUsername() + "' already exists!");
            model.addAttribute("user", user);
            model.addAttribute("roles", Arrays.asList("USER", "ADMIN"));
            return "admin/addUser";
        }

        Set<String> roles = new HashSet<>();
        roles.add(role);
        userService.saveUser(user.getUsername(), user.getPassword(), roles);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}