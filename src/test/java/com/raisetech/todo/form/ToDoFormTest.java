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

class ToDoFormTest {

    @InjectMocks
    ToDoForm toDoForm;

    @Test
    void 正しい値を入力した時にバリデーションエラーとならないこと() {
        ToDoForm toDoForm = new ToDoForm();
        toDoForm.setTask("テスト用タスク１");
        toDoForm.setLimitDate("2022-08-10");

        Set<ConstraintViolation<ToDoForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(toDoForm);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void nullを入力した時にバリデーションエラーとなること() {
        ToDoForm toDoForm = new ToDoForm();
        toDoForm.setTask(null);
        toDoForm.setLimitDate(null);

        Set<ConstraintViolation<ToDoForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(toDoForm);
        assertThat(violations.size()).isEqualTo(2);

        assertThat(violations)
                .extracting(
                        propertyPath -> propertyPath.getPropertyPath().toString(),
                        message -> message.getMessage())
                .containsOnly(
                        tuple("limitDate", "空白は許可されていません"),
                        tuple("task", "空白は許可されていません")
                );
    }

    @Test
    void 空文字を入力した時にバリデーションエラーとなること() {
        ToDoForm toDoForm = new ToDoForm();
        toDoForm.setTask("");
        toDoForm.setLimitDate("2022-08-10");

        Set<ConstraintViolation<ToDoForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(toDoForm);
        assertThat(violations.size()).isEqualTo(2);

        assertThat(violations)
                .extracting(
                        propertyPath -> propertyPath.getPropertyPath().toString(),
                        message -> message.getMessage())
                .containsOnly(
                        tuple("task", "1 から 256 の間のサイズにしてください"),
                        tuple("task", "空白は許可されていません")
                );
    }

    @Test
    void limitDateに有効な型以外を入力した時にバリデーションエラーとなること() {
        ToDoForm toDoForm = new ToDoForm();
        toDoForm.setTask("テスト用タスク１");
        toDoForm.setLimitDate("aaaa");

        DateTimeParseException e = assertThrows(DateTimeParseException.class, ()-> toDoForm.getLimitDate());
        assertThat(e.getMessage()).isEqualTo("Text 'aaaa' could not be parsed at index 0");
    }
}