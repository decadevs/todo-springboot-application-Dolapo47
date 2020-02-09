package com.example.demo.controllers;

import com.example.demo.models.Todo;
import com.example.demo.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoController {

    @Autowired
    private TodoService todoService;


    @RequestMapping("/")
    public String name(){
        return "index";
    }

    @RequestMapping(path = "/todo/create", method = RequestMethod.GET)
    public String createTodo(Model model){
        model.addAttribute("todo", new Todo());
        return "edit";
    }

    @RequestMapping(path = "/todo", method = RequestMethod.POST)
    public String saveTodo(Todo todo){
        todo.setStatus("pending");
        todoService.createTodo(todo);
        return "redirect:/todo";
    }

    @RequestMapping(path = "/todo", method = RequestMethod.GET)
    public String getAllTodos(Model model){
        model.addAttribute("todo", todoService.getTodo());
        return "todo";
    }

    @RequestMapping(path = "/todo/edit/{id}", method = RequestMethod.GET)
    public String editProduct(Model model, @PathVariable(value = "id") Integer id){
        model.addAttribute("todo", todoService.getATodo(id));
        return "edit";
    }

    @RequestMapping(path = "/todo/delete/{id}", method = RequestMethod.GET)
    public String deleteTodo(@PathVariable(name = "id") Integer id, Todo todo){
        todoService.delete(todo, id);
        return "redirect:/todo";
    }
}
