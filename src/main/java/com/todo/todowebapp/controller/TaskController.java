package com.todo.todowebapp.controller;

import com.todo.todowebapp.model.Task;
import com.todo.todowebapp.service.TaskService;
import com.todo.todowebapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // View all tasks
    @GetMapping("/tasks")
    public String viewTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "admin/tasks";  // Make sure this Thymeleaf template exists
    }

    // Show form to create a new task
    @GetMapping("/tasks/new")
    public String showTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userService.getAllUsers()); // Provide user list for assignment
        return "admin/task_form";  // Make sure this Thymeleaf template exists
    }

    // Save a new or edited task
    @PostMapping("/tasks/save")
    public String saveTask(@ModelAttribute Task task) {
        System.out.println("Saving task with ID: " + task.getId());
        taskService.saveTask(task);
        return "redirect:/admin/tasks";
    }

    // Delete a task by id
    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/admin/tasks";
    }

    // Show form to edit existing task
    @GetMapping("/tasks/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
        model.addAttribute("task", task);
        model.addAttribute("users", userService.getAllUsers());
        return "admin/task_form";
    }
}
