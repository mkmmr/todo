package com.raisetech.todo.controller;

import com.raisetech.todo.form.ToDoForm;
import com.raisetech.todo.service.ToDoEntity;
import com.raisetech.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ToDoController{
    private final ToDoService toDoService;

    @GetMapping("/todos")
    public List<ToDoEntity> getAllTask(){
        return toDoService.findAllTask();
    }

    @GetMapping("/todos/{id}")
    public ToDoEntity getTaskById(@PathVariable int id){
        return toDoService.findById(id);
    }

    @PostMapping("/todos")
    public ToDoEntity create(@Validated @RequestBody ToDoForm toDoForm){
        return toDoService.create(toDoForm.getTask(), toDoForm.getLimitDate());
    }
}
