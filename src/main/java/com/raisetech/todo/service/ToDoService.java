package com.raisetech.todo.service;

import com.raisetech.todo.exception.ResourceNotFoundException;
import com.raisetech.todo.repository.ToDoRecord;
import com.raisetech.todo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public List<ToDoEntity> findAllTask() {
        return toDoRepository.findAllTask()
                .stream()
                .map(toDoRecord -> new ToDoEntity(toDoRecord.getId(), toDoRecord.isDone(), toDoRecord.getTask(), toDoRecord.getLimitDate()))
                .collect(Collectors.toList());
    }

    public ToDoEntity findById(int id) {
        return toDoRepository.findById(id)
                .map(toDoRecord -> new ToDoEntity(toDoRecord.getId(), toDoRecord.isDone(), toDoRecord.getTask(), toDoRecord.getLimitDate()))
                .orElseThrow(() -> new ResourceNotFoundException("タスクが存在しません"));
    }

    public ToDoEntity create(String task, LocalDate limitDate) {
        var toDoRecord = ToDoRecord.newInstance(task, limitDate);
        toDoRepository.insert(toDoRecord);

        return new ToDoEntity(toDoRecord.getId(), toDoRecord.isDone(), toDoRecord.getTask(), toDoRecord.getLimitDate());
    }

    public ToDoEntity update(int id, String task, LocalDate limitDate) {
        toDoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("タスク (id = " + id + ") は存在しません"));
        toDoRepository.update(ToDoRecord.valueOf(id, task, limitDate));
        return findById(id);
    }
}
