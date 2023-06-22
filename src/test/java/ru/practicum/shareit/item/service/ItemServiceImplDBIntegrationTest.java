package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemServiceImplDBIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    private Long userId;
    private Long itemId;

    @BeforeAll
    public void setUp() {
        User user = new User(1L, "test_user", "password");
        userId = user.getId();
        user = userRepository.save(user);

        Item item = new Item(1L, user, "Test item", "Test item description",
                true, new ArrayList<>());
        item = itemRepository.save(item);
        itemId = item.getId();
    }

    @AfterAll
    public void tearDown() {
        itemRepository.deleteById(itemId);
        userRepository.deleteById(userId);
    }

    @Test
    public void testGetItems() {
        List<ItemDto> items = itemService.getItems(userId);
        assertNotNull(items);
        assertTrue(items.size() > 0);
        ItemDto itemDto = items.get(0);
        assertNotNull(itemDto.getName());
        assertNotNull(itemDto.getDescription());
        assertNotNull(itemDto.getAvailable());
        assertNull(itemDto.getLastBooking());
        assertNull(itemDto.getNextBooking());
        assertEquals(itemDto.getComments().size(), 0);
        assertNull(itemDto.getRequestId());
    }
}