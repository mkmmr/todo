package com.raisetech.todo.repository;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ToDoRecord {

    Integer id;
    boolean done;
    String task;
    LocalDate limitDate;

}