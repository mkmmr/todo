package com.raisetech.todo.repository;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ToDoRepository {

    @Select("SELECT * FROM to_do_list")
    List<ToDoRecord> findAllTask();

    @Select("SELECT * FROM to_do_list WHERE id = #{id}")
    Optional<ToDoRecord> findById(int id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO to_do_list (is_done, task, limit_date) VALUES (#{done}, #{task}, #{limitDate})")
    void insert(ToDoRecord toDoRecord);

    @Update("UPDATE to_do_list " +
            "SET " +
                "is_done = CASE " +
                        "WHEN #{done} IS NOT NULL " +
                        "THEN #{done} " +
                        "ELSE is_done " +
                    "END, " +
                "task = CASE " +
                        "WHEN #{task} IS NOT NULL " +
                        "THEN #{task} " +
                        "ELSE task " +
                    "END, " +
                "limit_date = CASE " +
                        "WHEN #{limitDate} IS NOT NULL " +
                        "THEN #{limitDate} " +
                        "ELSE limit_date " +
                    "END " +
            "WHERE id = #{id}")
    void update(ToDoRecord toDoRecord);
}
