package com.raisetech.todo.service;

import com.raisetech.todo.exception.ResourceNotFoundException;
import com.raisetech.todo.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {

    @InjectMocks
    ToDoService toDoService;

    @Mock
    ToDoRepository toDoRepository;

    @Test
    void タスク全件を正常に返すこと() {
        List<ToDoEntity>  allTasks = Arrays.asList(
                new ToDoEntity(1, false, "Readの実装", LocalDate.of(2022, 8, 10)),
                new ToDoEntity(2, false, "Createの実装", LocalDate.of(2022, 8, 20)),
                new ToDoEntity(3, false, "Deleteの実装", LocalDate.of(2022, 8, 30))
        ) ;
        doReturn(allTasks).when(toDoRepository).findAllTask();

        List<ToDoEntity> actual = toDoService.findAllTask();
        assertThat(actual).hasSize(3).isEqualTo(allTasks);

        verify(toDoRepository, times(1)).findAllTask();
    }

    @Test
    void 存在するタスクのIDを指定したときに正常にタスクが返されること() {
        doReturn(Optional.of(new ToDoEntity(1, false, "Readの実装", LocalDate.of(2022,8,10))))
                .when(toDoRepository).findById(1);

        ToDoEntity actual = toDoService.findById(1);
        assertThat(actual).isEqualTo(new ToDoEntity(1, false, "Readの実装", LocalDate.of(2022,8,10)));

        verify(toDoRepository, times(1)).findById(1);
    }

    @Test
    void 存在しないタスクのIDを指定したときに正常に例外が投げられていること() {
        doReturn(Optional.empty()).when(toDoRepository).findById(anyInt());
        assertThrows(ResourceNotFoundException.class, ()-> toDoService.findById(1) );

        verify(toDoRepository, times(1)).findById(anyInt());
    }
}