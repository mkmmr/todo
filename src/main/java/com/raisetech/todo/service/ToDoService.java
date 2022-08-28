package com.raisetech.todo.service;

import com.raisetech.todo.exception.ResourceNotFoundException;
import com.raisetech.todo.repository.ToDoRecord;
import com.raisetech.todo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    public ToDoEntity update(int id, Boolean done, String task, LocalDate limitDate) {
        ToDoRecord nowToDoRecord = toDoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("タスク (id = " + id + ") は存在しません"));

        if (done == null) {
            done = nowToDoRecord.isDone();
        }

        var toDoRecord = new ToDoRecord(id, done, task, limitDate);
        toDoRepository.update(toDoRecord);
        return toDoRepository.findById(id)
                .map(updatedRecord -> new ToDoEntity(id, updatedRecord.isDone(), updatedRecord.getTask(), updatedRecord.getLimitDate()))
                .orElseThrow(() -> new ResourceNotFoundException("タスク (id = " + id + ") は存在しません"));
    }

    public void deleteById(int id) {
        toDoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("タスク (id = " + id + ") は存在しません"));
        toDoRepository.deleteById(id);
    }
}
