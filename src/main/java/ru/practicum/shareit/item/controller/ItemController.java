package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.dto.CommentDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validateInterfaces.Create;
import ru.practicum.shareit.validateInterfaces.Update;

import java.util.List;

import static ru.practicum.shareit.user.controller.UserController.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto createComment(@RequestBody @Validated(Create.class) CommentDto commentDto,
                                    @RequestHeader(X_SHARER_USER_ID) long userId,
                                    @PathVariable long itemId) {
        return itemService.createComment(commentDto, userId, itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody @Validated(Create.class) ItemDto itemDto,
                              @RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestBody @Validated(Update.class) ItemDto itemDto,
                              @PathVariable long itemId,
                              @RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable long itemId,
                           @RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItems(@RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemByText(@RequestParam String text,
                                       @RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemService.getItemsByText(text, userId);
    }
}
