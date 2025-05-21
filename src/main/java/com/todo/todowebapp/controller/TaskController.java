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
        model.addAttribute("users", userService.getAllUsers());
        return "admin/task_form";
    }


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


    @GetMapping("/tasks/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
        model.addAttribute("task", task);
        model.addAttribute("users", userService.getAllUsers());
        return "admin/task_form";
    }
}
