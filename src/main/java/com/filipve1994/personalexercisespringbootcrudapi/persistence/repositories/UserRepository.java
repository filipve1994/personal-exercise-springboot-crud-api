package com.filipve1994.personalexercisespringbootcrudapi.persistence.repositories;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


}
