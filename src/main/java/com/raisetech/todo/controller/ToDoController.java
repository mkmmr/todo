package com.raisetech.todo.controller;

import com.raisetech.todo.form.ToDoForm;
import com.raisetech.todo.form.ToDoUpdateForm;
import com.raisetech.todo.service.ToDoEntity;
import com.raisetech.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequiredArgsConstructor
public class ToDoController {
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
    public ResponseEntity<ToDoResponse> create(@Validated @RequestBody ToDoForm toDoForm){
        ToDoEntity toDoEntity = toDoService.create(toDoForm.getTask(), toDoForm.getLimitDate());
        ToDoResponse toDoResponse = new ToDoResponse(toDoEntity.getId(), toDoEntity.isDone(), toDoEntity.getTask(), toDoEntity.getLimitDate());
        return ResponseEntity.created(URI.create("/todos/" + toDoEntity.getId())).body(toDoResponse);
    }

    @PatchMapping("/todos/{id}")
    public ResponseEntity<ToDoResponse> edit(@PathVariable("id") int id, @Validated @RequestBody ToDoUpdateForm updateForm){
        ToDoEntity toDoEntity = toDoService.update(id, updateForm.getTask(), updateForm.getLimitDate());
        ToDoResponse toDoResponse = new ToDoResponse(toDoEntity.getId(), toDoEntity.isDone(), toDoEntity.getTask(), toDoEntity.getLimitDate());
        return ResponseEntity.ok(toDoResponse);
    }
}
