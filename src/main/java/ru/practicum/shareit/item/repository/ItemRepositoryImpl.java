package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.IdCounter;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final UserRepository userRepository;
    private final HashMap<Long, Item> items = new HashMap<>();
    private final IdCounter idCounter = new IdCounter();

    @Autowired
    public ItemRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Item createEntity(Item entity) {
        if (items.containsValue(entity)) {
            throw new ConflictException("Entity already exists");
        }
        if (userRepository.getEntity(entity.getUserId()) == null) {
            throw new EntityNotFoundException("User doesn't exist");
        }
        entity.setId(idCounter.generateId());
        userRepository.getEntity(entity.getUserId()).getItems().add(entity.getId());
        items.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Item getEntity(long entityId) {
        if (!items.containsKey(entityId)) {
            throw new EntityNotFoundException("Entity with" + entityId + " not found");
        }
        return items.get(entityId);
    }

    @Override
    public List<Item> getEntities() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return userRepository.getEntity(userId).getItems().stream().map(items::get).collect(Collectors.toList());
    }

    @Override
    public Item updateEntity(Item item, long entityId) {
        if (items.get(entityId) == null) {
            throw new EntityNotFoundException("Item with id" + entityId + " not found");
        }
        if (!userRepository.getEntity(item.getUserId()).getItems().contains(entityId)) {
            throw new EntityNotFoundException("Item with id " + item.getId() + " does not belong to the user " + item.getUserId());
        }
        Item item1 = items.get(entityId);
        items.remove(entityId);
        if (items.containsValue(item)) {
            items.put(entityId, item1);
            throw new ConflictException("Item " + item + "already exists");
        }
        items.put(entityId, item);
        return item;
    }

    @Override
    public List<Item> getItemsByText(String text) {
        List<Item> foundItems = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : items.values()) {
                if (item.getAvailable()) {
                    if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                        foundItems.add(item);
                    } else if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                        foundItems.add(item);
                    }
                }
            }
        }
        return foundItems;
    }

    @Override
    public void deleteEntity(long entityId) {
        items.remove(entityId);
    }
}
