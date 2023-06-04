package ru.practicum.shareit.item.model.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;

@Component
public class ItemMapperImpl implements ItemMapper {
    @Override
    public ItemDto mapTo(Item entity) {
        return new ItemDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getAvailable());
    }

    @Override
    public Item mapFrom(ItemDto entity) {
        return null;
    }

    @Override
    public Item mapFrom(ItemDto entity, long userId) {
        return new Item(entity.getId(), userId, entity.getName(), entity.getDescription(), entity.getAvailable());
    }

    @Override
    public Item mapFrom(ItemDto itemDto, Item item, long userId) {
        Item item1 = new Item(item.getId(), userId, item.getName(), item.getDescription(), item.getAvailable());
        if (itemDto.getName() != null) {
            item1.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item1.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item1.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() == null && itemDto.getDescription() == null && itemDto.getAvailable() == null) {
            throw new ConflictException("Item is empty");
        }
        return item1;
    }
}
