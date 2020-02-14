package com.filipve1994.personalexercisespringbootcrudapi.persistence.services;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.ItemInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.Item;

import java.util.List;

public interface ItemService {

    /**
     * Get all items from DB
     */
    List<Item> findAllItems();

    /**
     * Get a specific item by its id from the DB
     */
    Item findById(int itemId);

    Item addItem(ItemInput itemInput);

    Item updateItem(int id, ItemInput itemInput);

    void deleteItem(Item item);

    void deleteItemById(int id);


}
