package com.raisetech.todo.repository;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ToDoRecord {

    Integer id;
    int done;
    String task;
    LocalDate limitDate;

    public static ToDoRecord newInstance(String task, LocalDate limitDate) {
        return new ToDoRecord(null, 0, task, limitDate);
    }

    public boolean newDone() {
        return false;
    }
}