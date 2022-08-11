package com.raisetech.todo.service;

import com.raisetech.todo.exception.ResourceNotFoundException;
import com.raisetech.todo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public List<ToDoEntity> findAllTask() {
        return toDoRepository.findAllTask();
    }

    public ToDoEntity findById(int id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("タスクが存在しません"));
    }
}
