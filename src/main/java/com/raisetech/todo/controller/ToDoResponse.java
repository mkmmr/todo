package com.raisetech.todo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ToDoResponse {
    int id;
    boolean done;
    String task;
    LocalDate limitDate;
}
