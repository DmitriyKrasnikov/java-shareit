package ru.practicum.shareit.item.model.mapper;

import ru.practicum.shareit.entityInterfaces.EntityMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;

public interface ItemMapper extends EntityMapper<ItemDto, Item> {
    Item mapFrom(ItemDto entity, long userId);

    Item mapFrom(ItemDto itemDto, Item item, long userId);
}
