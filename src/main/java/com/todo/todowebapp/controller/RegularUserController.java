package com.todo.todowebapp.controller;

import com.todo.todowebapp.model.Task;
import com.todo.todowebapp.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
public class RegularUserController {  // ✅ Renamed to avoid conflict

    private final TaskService taskService;

    public RegularUserController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String viewUserTasks(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Task> tasks = taskService.getTasksForUser(username);
        model.addAttribute("tasks", tasks);
        return "user/tasks";  // templates/user/tasks.html
    }

    @PostMapping("/tasks/complete/{id}")
    public String completeTask(@PathVariable Long id, Authentication authentication) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));

        if (!task.getAssignedTo().equals(authentication.getName())) {
            throw new IllegalArgumentException("You are not authorized to complete this task");
        }

        task.setCompleted(true);
        task.setCompletedDate(LocalDate.now());
        taskService.saveTask(task);

        return "redirect:/user/tasks";
    }
}

