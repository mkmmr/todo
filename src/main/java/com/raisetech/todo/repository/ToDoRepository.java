package com.raisetech.todo.repository;

import com.raisetech.todo.service.ToDoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ToDoRepository {

    @Select("SELECT * From to_do_list")
    List<ToDoEntity> findAllTask();

    @Select("SELECT * FROM to_do_list WHERE id = #{id}")
    Optional<ToDoEntity> findById(int id);

}
