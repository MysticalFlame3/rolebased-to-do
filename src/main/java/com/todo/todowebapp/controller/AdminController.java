package com.todo.todowebapp.controller;

import com.todo.todowebapp.model.User;
import com.todo.todowebapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/new")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user_form";  // Thymeleaf template for user creation form
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user) {
        // Set role as singleton set with "USER"
        user.setRoles(Collections.singleton("USER"));
        userService.saveUser(user);
        return "redirect:/admin/users/new?success";
    }
}
