package com.example.demo.controllers;

import com.example.demo.models.Todo;
import com.example.demo.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TodoController {

    @Value("${server.por:40}")
    private String port;
    @Value("${server.servlet.context-path}")
    private String path;

    @GetMapping
    public String HealthCheck() {
        return "Our app is running on port:" + port + " with path: " + path;
    }
        @Autowired
        private TodoService todoService;

    @GetMapping
    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public MyResponse<List<Todo>> getTodo(){
        List<Todo> allTodo = todoService.getTodo();
        if(allTodo == null){
            return new MyResponse<>( HttpStatus.NOT_FOUND,"Successful", null);
        }else{
            return new MyResponse<>( HttpStatus.OK,"Successful", allTodo);
        }
    }

    @PostMapping
    @RequestMapping(value = "/todo", method = RequestMethod.POST)
    public MyResponse<Todo> createTodo(@RequestBody Todo todo, HttpServletResponse response){
        try{
            System.out.println("logs: " + todo.getTitle());
            Todo t = todoService.createTodo(todo);
            HttpStatus statusCode = HttpStatus.CREATED;
            String message = "Todo created successfully";
            if(t == null){
                statusCode = HttpStatus.BAD_REQUEST;
                message = "Bad request!!!";
            }
            response.setStatus(statusCode.value());
            return new MyResponse<>(statusCode, message, t);
        }catch (Exception e){
            HttpStatus statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
            return new MyResponse<>(statusCode, "Missing input field", null);
        }
    }

    @GetMapping
    @RequestMapping("/todo/{id}")
    public MyResponse<Todo> getOneTodo(@PathVariable Integer id){
        System.out.printf("id: %d\n", id);
        Todo t = todoService.getATodo(id);
        if(t == null){
            return new MyResponse<>(HttpStatus.UNPROCESSABLE_ENTITY, "No task with this id: " + id, null);
        }
        return new MyResponse<>(HttpStatus.OK, "Retrieved task successfully", t);
    }

    @PatchMapping
    @RequestMapping(value = "/todo/{id}", method = RequestMethod.PATCH)
    public MyResponse<Todo> updateTodo(@RequestBody Todo todo, @PathVariable Integer id){
        System.out.printf("id: %d\n", id);
        Todo t = todoService.update(todo, id);
        if(t == null){
            return new MyResponse<>(HttpStatus.UNPROCESSABLE_ENTITY, "No task with this id: " + id, null);
        }
        return new MyResponse<>(HttpStatus.OK, "Retrieved task successfully", t);
    }

    @DeleteMapping
    @RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE)
    public MyResponse<List<Todo>> deleteTodo(@RequestBody Todo todo, @PathVariable Integer id){
        try{
            ArrayList<Todo> t = (ArrayList<Todo>) todoService.delete(todo, id);
            if(t == null){
                return new MyResponse<>( HttpStatus.NOT_FOUND,"Successful", null);
            }
            return new MyResponse<>( HttpStatus.OK,"Successful", t);
        }catch(Exception e){
            HttpStatus statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
            return new MyResponse<>(statusCode, "No task with this id: " + id, null);
        }

    }
}
