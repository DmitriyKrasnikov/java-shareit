package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.entityInterfaces.EntityRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends EntityRepository<Item> {
    List<Item> getUserItems(long userId);

    List<Item> getItemsByText(String text);
}
