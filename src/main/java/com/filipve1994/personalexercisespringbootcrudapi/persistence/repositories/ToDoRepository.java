package com.filipve1994.personalexercisespringbootcrudapi.persistence.repositories;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<TodoModel, Long> {



}
