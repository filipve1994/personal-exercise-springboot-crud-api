package com.filipve1994.personalexercisespringbootcrudapi.persistence.services;

import com.filipve1994.personalexercisespringbootcrudapi.errorhandling.exceptions.ItemNotFoundException;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.ItemInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.Item;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item findById(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found with id : " + itemId));
    }

    @Override
    public Item addItem(ItemInput itemInput) {
        Item itemToSave = new Item();
        itemToSave.setName(itemInput.getName());
        itemToSave.setCategory(itemInput.getCategory());

        return itemRepository.save(itemToSave);
    }

    @Override
    public Item updateItem(int id, ItemInput itemInput) {

        Item updateDb = findById(id);

        updateDb.setName(itemInput.getName());
        updateDb.setCategory(itemInput.getCategory());

        return itemRepository.save(updateDb);
    }

    @Override
    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }

    @Override
    public void deleteItemById(int id) {
        itemRepository.deleteById(id);


    }
}
