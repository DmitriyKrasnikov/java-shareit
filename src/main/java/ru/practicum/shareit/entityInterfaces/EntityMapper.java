package ru.practicum.shareit.entityInterfaces;

public interface EntityMapper<T, E> {
    T mapTo(E entity);

    E mapFrom(T entity);
}
