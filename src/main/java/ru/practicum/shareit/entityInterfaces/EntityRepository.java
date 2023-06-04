package ru.practicum.shareit.entityInterfaces;

import java.util.List;

public interface EntityRepository<T> {

    T createEntity(T entity);

    T getEntity(long entityId);

    List<T> getEntities();

    T updateEntity(T entity, long entityId);

    void deleteEntity(long entityId);
}
