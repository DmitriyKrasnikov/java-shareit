package ru.practicum.shareit;

public class IdCounter {
    private Long id = 0L;

    public Long generateId() {
        return ++id;
    }
}
