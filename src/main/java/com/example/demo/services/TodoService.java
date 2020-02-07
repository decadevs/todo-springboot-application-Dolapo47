package com.example.demo.services;


import com.example.demo.models.Todo;
import com.example.demo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;


    public Todo createTodo(Todo todo){
        Todo t = null;
        try{
            todo.setStatus("pending");
            t = todoRepository.save(todo);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
        return t;
    }

    public Todo getATodo(Integer id){
        Optional<Todo> t = todoRepository.findById(id);
        if(t.isPresent()){
            return t.get();
        }
        else{
            return null;
        }
    }

    public List<Todo> getTodo(){
        List<Todo> todos = todoRepository.findAll();
        return todos;
    }

    public Todo update(Todo todo, Integer id){
        Todo t = todoRepository.findById(id).orElse(null);
        if (t != null){
            t.setUpdatedAt(t.getUpdatedAt());
            if(todo.getStatus().equalsIgnoreCase("in progress") ||
                    todo.getStatus().equalsIgnoreCase("completed")) {
                t.setTitle(todo.getTitle());
                t.setDescription(todo.getDescription());
                t.setStatus(todo.getStatus());
            }else {
                t.setTitle(todo.getTitle());
                t.setDescription(todo.getDescription());
            }
            return todoRepository.save(t);
        }
        return null;
    }

    public List<Todo> delete(Todo todo, Integer id){
        todoRepository.deleteById(id);
        todoRepository.findById(id);
        List<Todo> todos = todoRepository.findAll();
        return todos;
    }
}
