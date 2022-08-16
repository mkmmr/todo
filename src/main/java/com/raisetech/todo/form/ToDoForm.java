package com.raisetech.todo.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class ToDoForm {

    private int id;
    private boolean done;

    @NotNull
    @Size(min = 1, max = 256)
    private String task;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate limitDate;

}
