package com.raisetech.todo.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@Value
public class ToDoUpdateForm {

    Boolean done;

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
