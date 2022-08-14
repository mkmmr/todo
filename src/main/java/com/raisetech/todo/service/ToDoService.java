package com.raisetech.todo.service;

import com.raisetech.todo.exception.ResourceNotFoundException;
import com.raisetech.todo.repository.ToDoRecord;
import com.raisetech.todo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public ToDoEntity create(String task, LocalDate limitDate) {
        var toDoRecord = new ToDoRecord(null, false, task, limitDate);
        toDoRepository.insert(toDoRecord);

        return new ToDoEntity(toDoRecord.getId(), toDoRecord.isDone(), toDoRecord.getTask(), toDoRecord.getLimitDate());
    }
}
