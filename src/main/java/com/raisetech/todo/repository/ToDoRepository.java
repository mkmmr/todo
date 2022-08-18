package com.raisetech.todo.repository;

import com.raisetech.todo.service.ToDoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ToDoRepository {

    @Select("SELECT * From to_do_list")
    List<ToDoEntity> findAllTask();

    @Select("SELECT * FROM to_do_list WHERE id = #{id}")
    Optional<ToDoEntity> findById(int id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO to_do_list (is_done, task, limit_date) values (#{done}, #{task}, #{limitDate})")
    void insert(ToDoRecord toDoRecord);

}
