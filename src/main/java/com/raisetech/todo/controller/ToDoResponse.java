package com.raisetech.todo.controller;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ToDoResponse {
    int id;
    boolean done;
    String task;
    LocalDate limitDate;
}
