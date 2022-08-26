package com.raisetech.todo.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@Data
public class ToDoUpdateForm {

    boolean done;

    @Size(min = 1, max = 256)
    private String task;

    private String limitDate;

    public LocalDate getLimitDate() {
        if(limitDate != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(limitDate, dtf);
        }
        return null;
    }
}
