//package com.todo.todowebapp.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class DashboardController {
//
//    @GetMapping("/dashboard")
//    public String dashboard(Authentication authentication, Model model) {
//        // Check if logged-in user has ROLE_ADMIN
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//
//        if (isAdmin) {
//            model.addAttribute("role", "ADMIN");
//        } else {
//            model.addAttribute("role", "USER");
//        }
//
//        model.addAttribute("username", authentication.getName());
//
//        // Return the dashboard.html Thymeleaf template
//        return "dashboard";
//    }
//}
