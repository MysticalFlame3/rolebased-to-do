package com.todo.todowebapp.service;

import com.todo.todowebapp.model.Todo;
import com.todo.todowebapp.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getTodosByUsername(String username) {
        return todoRepository.findByUsername(username);
    }

    public void addTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public void updateTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public void deleteTodoById(Long id) {
        todoRepository.deleteById(id);
    }

    public Todo getTodoById(Long id) {
        return todoRepository.findById(id).orElse(null);
    }
}
