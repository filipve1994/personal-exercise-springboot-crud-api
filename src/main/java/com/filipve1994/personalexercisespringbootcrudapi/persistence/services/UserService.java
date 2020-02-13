package com.filipve1994.personalexercisespringbootcrudapi.persistence.services;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.UserInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User save(UserInput user);

    Page<User> findAll(PageRequest of);
}
