package com.raisetech.todo.service;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ToDoEntity {

    int id;
    Boolean done;
    String task;
    LocalDate limitDate;

}
