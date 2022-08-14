package com.raisetech.todo.form;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
public class ToDoForm {

    int id;
    boolean done;
    String task;
    LocalDate limitDate;

}
