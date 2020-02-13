package com.filipve1994.personalexercisespringbootcrudapi.persistence.services;

import com.filipve1994.personalexercisespringbootcrudapi.errorhandling.exceptions.UserNotFoundException;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.UserInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.User;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));
    }

    @Transactional
    @Override
    public User save(UserInput user) {

        User newUser = new User();
        newUser.setLastName(user.getLastName());
        newUser.setMiddleName(user.getMiddleName());
        newUser.setFirstName(user.getFirstName());
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setSiblings(user.getSiblings());

        return userRepository.saveAndFlush(newUser);
    }

    @Override
    public Page<User> findAll(PageRequest of) {
        return userRepository.findAll(of);
    }
}
