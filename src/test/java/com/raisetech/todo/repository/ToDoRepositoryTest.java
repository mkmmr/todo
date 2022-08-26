package com.raisetech.todo.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
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
        List<ToDoRecord> actual = toDoRepository.findAllTask();
        assertThat(actual)
                .hasSize(3)
                .contains(
                        new ToDoRecord(1, false, "Readの実装", LocalDate.of(2022, 8, 10)),
                        new ToDoRecord(2, false, "Createの実装", LocalDate.of(2022, 8, 20)),
                        new ToDoRecord(3, false, "Deleteの実装", LocalDate.of(2022, 8, 30))
                );
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @Transactional
    void IDを指定して特定のタスク情報が取得できること() {
        Optional<ToDoRecord> actual = toDoRepository.findById(1);
        assertThat(actual).isEqualTo(Optional.of(new ToDoRecord(1, false, "Readの実装", LocalDate.of(2022, 8, 10))));
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @ExpectedDataSet(value = "datasets/expected_insert_to_do_list.yml", ignoreCols = "id")
    void タスクを新規登録できること() {
                assertThat(toDoRepository.findAllTask().size()).isEqualTo(3);
                toDoRepository.insert(new ToDoRecord(4, false, "Updateの実装", LocalDate.of(2022, 8, 25)));
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @ExpectedDataSet(value = "datasets/expected_update_to_do_list.yml", ignoreCols = "id")
    void タスクを変更できること() {
        assertThat(toDoRepository.findAllTask().size()).isEqualTo(3);
        toDoRepository.update(new ToDoRecord(3, false, "Updateの実装", LocalDate.of(2022, 8, 25)));
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @ExpectedDataSet(value = "datasets/to_do_list.yml", ignoreCols = "id")
    void nullが入力された時にタスクを更新しないこと() {
        assertThat(toDoRepository.findAllTask().size()).isEqualTo(3);
        toDoRepository.update(new ToDoRecord(3, false, null, null));
    }
}