package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.IdCounter;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepositoryImpl {

    private final HashMap<Long, User> users = new HashMap<>();
    private final IdCounter idCounter = new IdCounter();

    public User createEntity(User entity) {
        if (users.containsValue(entity)) {
            throw new ConflictException("Entity already exists");
        }
        entity.setId(idCounter.generateId());
        users.put(entity.getId(), entity);
        return entity;
    }

    public User getEntity(long entityId) {
        if (!users.containsKey(entityId)) {
            throw new EntityNotFoundException("User with id" + entityId + " not found");
        }
        return users.get(entityId);
    }

    public List<User> getEntities() {
        return new ArrayList<>(users.values());
    }

    public User updateEntity(User entity, long entityId) {
        if (users.get(entityId) == null) {
            throw new EntityNotFoundException("User with id not found");
        }
        User user = users.get(entityId);
        users.remove(entityId);
        if (users.containsValue(entity)) {
            users.put(entityId, user);
            throw new ConflictException("User with email" + entity.getEmail() + "already exists");
        }
        users.put(entityId, entity);
        return entity;
    }

    public void deleteEntity(long entityId) {
        users.remove(entityId);
    }
}
