package com.raisetech.todo.form;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.time.format.DateTimeParseException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ToDoUpdateFormTest {

    @InjectMocks
    ToDoUpdateForm toDoUpdateForm;

    @Test
    void 正しい値を入力した時にバリデーションエラーとならないこと() {
        ToDoUpdateForm toDoUpdateForm = new ToDoUpdateForm(true, "テスト用タスク１", "2022-08-10");
        Set<ConstraintViolation<ToDoUpdateForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(toDoUpdateForm);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void nullを入力した時にバリデーションエラーとならないこと() {
        ToDoUpdateForm toDoUpdateForm = new ToDoUpdateForm(null, null, null);
        Set<ConstraintViolation<ToDoUpdateForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(toDoUpdateForm);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void 空文字を入力した時にバリデーションエラーとならないこと() {
        ToDoUpdateForm toDoUpdateForm = new ToDoUpdateForm(null, "", "");
        Set<ConstraintViolation<ToDoUpdateForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(toDoUpdateForm);
        assertThat(violations.size()).isEqualTo(1);

        assertThat(violations)
                .extracting(
                        propertyPath -> propertyPath.getPropertyPath().toString(),
                        message -> message.getMessage())
                .containsOnly(
                        tuple("task", "1 から 256 の間のサイズにしてください")
                );
    }

    @Test
    void limitDateに有効な型以外を入力した時にバリデーションエラーとなること() {
        ToDoUpdateForm toDoUpdateForm = new ToDoUpdateForm(null, null, "aaaa");
        DateTimeParseException e = assertThrows(DateTimeParseException.class, ()-> toDoUpdateForm.getLimitDate());
        assertThat(e.getMessage()).isEqualTo("Text 'aaaa' could not be parsed at index 0");
    }
}