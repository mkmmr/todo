package com.raisetech.todo.controller;

import com.raisetech.todo.exception.ResourceNotFoundException;
import com.raisetech.todo.form.ToDoForm;
import com.raisetech.todo.service.ToDoEntity;
import com.raisetech.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

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


    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFound(
            ResourceNotFoundException e, HttpServletRequest request) {

        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "message", e.getMessage(),
                "path", request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/todos")
//    @ResponseStatus(HttpStatus.CREATED)
    public ToDoEntity create(@Validated @RequestBody ToDoForm toDoForm){
        return toDoService.create(toDoForm.getTask(), toDoForm.getLimitDate());
    }
}
