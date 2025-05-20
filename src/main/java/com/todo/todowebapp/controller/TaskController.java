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

    @GetMapping("/tasks")
    public String viewTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "admin/tasks";
    }

    @GetMapping("/tasks/new")
    public String showTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userService.getAllUsers()); // for dropdown in form
        return "admin/task_form";
    }

    @PostMapping("/tasks/save")
    public String saveTask(@ModelAttribute Task task) {
        taskService.saveTask(task);
        return "redirect:/admin/tasks";
    }

    @GetMapping("/admin/tasks")
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "admin/tasks";  // points to templates/admin/tasks.html
    }
}
