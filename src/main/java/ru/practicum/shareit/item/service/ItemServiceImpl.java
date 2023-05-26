package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    public final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        log.info("Create item");
        Item item = itemRepository.createEntity(itemMapper.mapFrom(itemDto, userId));
        return itemMapper.mapTo(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        log.info("Update item with id {}", itemId);
        Item item = itemMapper.mapFrom(itemDto, itemRepository.getEntity(itemId), userId);
        itemRepository.updateEntity(item, itemId);
        return itemMapper.mapTo(itemRepository.getEntity(itemId));
    }

    @Override
    public ItemDto getItem(long itemId) {
        log.info("Get item with id {}", itemId);
        return itemMapper.mapTo(itemRepository.getEntity(itemId));
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        log.info("Get users items with userId {}", userId);
        return itemRepository.getUserItems(userId).stream().map(itemMapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        log.info("Get items by text {}", text);
        List<ItemDto> items = itemRepository.getItemsByText(text).stream().map(itemMapper::mapTo).collect(Collectors.toList());
        return items;
    }
}
