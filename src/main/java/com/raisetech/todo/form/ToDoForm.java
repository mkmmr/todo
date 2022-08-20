package com.raisetech.todo.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class ToDoForm {

    @NotBlank
    @Size(min = 1, max = 256)
    private String task;

    @NotNull
    private String limitDate;

    public LocalDate getLimitDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(limitDate, dtf);
    }
}
