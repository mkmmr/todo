package com.raisetech.todo.service;

import com.raisetech.todo.exception.ResourceNotFoundException;
import com.raisetech.todo.repository.ToDoRecord;
import com.raisetech.todo.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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
        List<ToDoEntity>  allTasksEntity = Arrays.asList(
                new ToDoEntity(1, false, "テスト用タスク１", LocalDate.of(2022, 8, 10)),
                new ToDoEntity(2, false, "テスト用タスク２", LocalDate.of(2022, 8, 20)),
                new ToDoEntity(3, false, "テスト用タスク３", LocalDate.of(2022, 8, 30))
        ) ;

        List<ToDoRecord>  allTasksRecord = Arrays.asList(
                new ToDoRecord(1, false, "テスト用タスク１", LocalDate.of(2022, 8, 10)),
                new ToDoRecord(2, false, "テスト用タスク２", LocalDate.of(2022, 8, 20)),
                new ToDoRecord(3, false, "テスト用タスク３", LocalDate.of(2022, 8, 30))
        ) ;
        doReturn(allTasksRecord).when(toDoRepository).findAllTask();

        List<ToDoEntity> actual = toDoService.findAllTask();
        assertThat(actual).hasSize(3).isEqualTo(allTasksEntity);

        verify(toDoRepository, times(1)).findAllTask();
    }

    @Test
    void 存在するタスクのIDを指定したときに正常にタスクが返されること() {
        doReturn(Optional.of(new ToDoRecord(1, false, "テスト用タスク１", LocalDate.of(2022,8,10))))
                .when(toDoRepository).findById(1);

        ToDoEntity actual = toDoService.findById(1);
        assertThat(actual).isEqualTo(new ToDoEntity(1, false, "テスト用タスク１", LocalDate.of(2022,8,10)));

        verify(toDoRepository, times(1)).findById(1);
    }

    @Test
    void 存在しないタスクのIDを指定したときに正常に例外が投げられていること() {
        doReturn(Optional.empty()).when(toDoRepository).findById(anyInt());
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()-> toDoService.findById(1));
        assertThat(e.getMessage()).isEqualTo("タスクが存在しません");

        verify(toDoRepository, times(1)).findById(anyInt());
    }

    @Test
    void 新規タスクを追加できること() {
        try (MockedStatic<ToDoRecord> toDoRecordMockedStatic = Mockito.mockStatic(ToDoRecord.class)) {
            toDoRecordMockedStatic
                    .when(() -> ToDoRecord.newInstance("テスト用タスク４", LocalDate.of(2022, 9, 1)))
                    .thenReturn(new ToDoRecord(4, false, "テスト用タスク４", LocalDate.of(2022, 9, 1)));
            doNothing().when(toDoRepository).insert(any());

            ToDoEntity actual = toDoService.create("テスト用タスク４", LocalDate.of(2022, 9, 1));
            assertThat(actual).isEqualTo(new ToDoEntity(4, false, "テスト用タスク４", LocalDate.of(2022, 9, 1)));

            verify(toDoRepository, times(1)).insert(any());
        }
    }

    @Test
    void タスクを変更できること() {
        ToDoRecord toDoRecordMock = new ToDoRecord(3, true, "テスト用タスク３変更", LocalDate.of(2022,9,30));

        doReturn(Optional.of(toDoRecordMock)).when(toDoRepository).findById(3);
        doNothing().when(toDoRepository).update(any());

        ToDoEntity actual = toDoService.update(3, true, "テスト用タスク３変更", LocalDate.of(2022, 9, 30));
        assertThat(actual).isEqualTo(new ToDoEntity(3, true, "テスト用タスク３変更", LocalDate.of(2022, 9, 30)));

        verify(toDoRepository, times(2)).findById(anyInt());
        verify(toDoRepository, times(1)).update(any());
    }

    @Test
    void done以外がnullの時にdoneだけ部分的に変更できること() {
        ToDoRecord toDoRecordMock = new ToDoRecord(3, true, "テスト用タスク３", LocalDate.of(2022,9,1));

        doReturn(Optional.of(toDoRecordMock)).when(toDoRepository).findById(3);
        doNothing().when(toDoRepository).update(any());

        ToDoEntity actual = toDoService.update(3, true, null, null);
        assertThat(actual).isEqualTo(new ToDoEntity(3, true, "テスト用タスク３", LocalDate.of(2022, 9, 1)));

        verify(toDoRepository, times(2)).findById(anyInt());
        verify(toDoRepository, times(1)).update(any());
    }

    @Test
    void task以外がnullの時にtaskだけ部分的に変更できること() {
        ToDoRecord toDoRecordMock = new ToDoRecord(3, false, "テスト用タスク３変更", LocalDate.of(2022,8,30));

        doReturn(Optional.of(toDoRecordMock)).when(toDoRepository).findById(3);
        doNothing().when(toDoRepository).update(any());

        ToDoEntity actual = toDoService.update(3, null, "テスト用タスク３変更", null);
        assertThat(actual).isEqualTo(new ToDoEntity(3, false, "テスト用タスク３変更", LocalDate.of(2022, 8, 30)));

        verify(toDoRepository, times(2)).findById(anyInt());
        verify(toDoRepository, times(1)).update(any());
    }

    @Test
    void limitDate以外がnullの時にlimitdateだけ部分的に変更できること() {
        ToDoRecord toDoRecordMock = new ToDoRecord(3, false, "テスト用タスク３", LocalDate.of(2022,9, 30));

        doReturn(Optional.of(toDoRecordMock)).when(toDoRepository).findById(3);
        doNothing().when(toDoRepository).update(any());

        ToDoEntity actual = toDoService.update(3, null, null, LocalDate.of(2022,9, 30));
        assertThat(actual).isEqualTo(new ToDoEntity(3, false, "テスト用タスク３", LocalDate.of(2022, 9, 30)));

        verify(toDoRepository, times(2)).findById(anyInt());
        verify(toDoRepository, times(1)).update(any());
    }

    @Test
    void 部分更新で全ての値がnullの時に何も変更しないこと() {
        ToDoRecord toDoRecordMock = new ToDoRecord(3, false, "テスト用タスク３", LocalDate.of(2022,8,30));

        doReturn(Optional.of(toDoRecordMock)).when(toDoRepository).findById(3);
        doNothing().when(toDoRepository).update(any());

        ToDoEntity actual = toDoService.update(3, null, null, null);
        assertThat(actual).isEqualTo(new ToDoEntity(3, false, "テスト用タスク３", LocalDate.of(2022, 8, 30)));

        verify(toDoRepository, times(2)).findById(anyInt());
        verify(toDoRepository, times(1)).update(any());
    }

    @Test
    void 存在しないタスクを変更しようとしたときに正常に例外が投げられていること() {
        doReturn(Optional.empty()).when(toDoRepository).findById(anyInt());
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()-> toDoService.update(99, true, "テスト用タスク９９", LocalDate.of(2022, 12, 1)) );
        assertThat(e.getMessage()).isEqualTo("タスク (id = 99) は存在しません");

        verify(toDoRepository, times(1)).findById(anyInt());
    }

    @Test
    void タスクを削除できること() {
        ToDoRecord toDoRecordMock = new ToDoRecord(3, true, "テスト用タスク３", LocalDate.of(2022,8,30));
        doReturn(Optional.of(toDoRecordMock)).when(toDoRepository).findById(3);
        doNothing().when(toDoRepository).deleteById(anyInt());

        toDoService.deleteById(3);

        verify(toDoRepository, times(1)).findById(anyInt());
        verify(toDoRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void 存在しないタスクを削除しようとしたときに正常に例外が投げられていること() {
        doReturn(Optional.empty()).when(toDoRepository).findById(anyInt());
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()-> toDoService.deleteById(99));
        assertThat(e.getMessage()).isEqualTo("タスク (id = 99) は存在しません");

        verify(toDoRepository, times(1)).findById(anyInt());
    }
}