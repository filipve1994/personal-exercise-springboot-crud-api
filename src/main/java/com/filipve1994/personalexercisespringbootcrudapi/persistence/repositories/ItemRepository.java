package com.filipve1994.personalexercisespringbootcrudapi.persistence.repositories;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
