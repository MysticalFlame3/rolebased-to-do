package com.todo.todowebapp.controller;

import com.todo.todowebapp.model.Todo;
import com.todo.todowebapp.service.TodoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public String listTodos(Authentication authentication, Model model) {
        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            model.addAttribute("todos", todoService.getAllTodos()); // need to create this method
        } else {
            model.addAttribute("todos", todoService.getTodosByUsername(username));
        }

        model.addAttribute("newTodo", new Todo());
        return "todos";
    }

    @PostMapping("/add")
    public String addTodo(@ModelAttribute("newTodo") Todo todo, Authentication authentication) {
        todo.setUsername(authentication.getName());
        todo.setCompleted(false);
        todoService.addTodo(todo);
        return "redirect:/todos";
    }

    @PostMapping("/complete/{id}")
    public String completeTodo(@PathVariable Long id, Authentication authentication) {
        Todo todo = todoService.getTodoById(id);
        if (todo != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin || todo.getUsername().equals(authentication.getName())) {
                todo.setCompleted(true);
                todoService.updateTodo(todo);
            }
        }
        return "redirect:/todos";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id, Authentication authentication) {
        Todo todo = todoService.getTodoById(id);
        if (todo != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin || todo.getUsername().equals(authentication.getName())) {
                todoService.deleteTodoById(id);
            }
        }
        return "redirect:/todos";
    }
}

