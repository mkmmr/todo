package com.raisetech.todo.controller;

import com.raisetech.todo.exception.ResourceNotFoundException;
import com.raisetech.todo.service.ToDoEntity;
import com.raisetech.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ToDoController {
    private final ToDoService toDoService;

    @GetMapping("/todolists")
    public List<ToDoEntity> getAllTask(){
        return toDoService.findAllTask();
    }

    @GetMapping("/todolists/{id}")
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
}
