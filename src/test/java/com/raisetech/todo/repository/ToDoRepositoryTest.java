package com.raisetech.todo.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.raisetech.todo.service.ToDoEntity;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ToDoRepositoryTest {

    @Autowired
    ToDoRepository toDoRepository;

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @Transactional
    void すべてのタスク情報が取得できること() {
        List<ToDoEntity> actual = toDoRepository.findAllTask();
        assertThat(actual)
                .hasSize(3)
                .contains(
                        new ToDoEntity(1, false, "Readの実装", LocalDate.of(2022, 8, 10)),
                        new ToDoEntity(2, false, "Createの実装", LocalDate.of(2022, 8, 20)),
                        new ToDoEntity(3, false, "Deleteの実装", LocalDate.of(2022, 8, 30))
                );
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @Transactional
    void IDを指定して特定のタスク情報が取得できること() {
        Optional<ToDoEntity> actual = toDoRepository.findById(1);
        assertThat(actual).isEqualTo(Optional.of(new ToDoEntity(1, false, "Readの実装", LocalDate.of(2022, 8, 10))));
    }
}