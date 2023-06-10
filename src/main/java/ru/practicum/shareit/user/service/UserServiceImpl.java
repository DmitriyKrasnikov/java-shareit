package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.model.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Creating user");
        User user = userMapper.mapFrom(userDto);
        return userMapper.mapTo(userRepository.save(user));
    }

    public UserDto getUser(long userId) {
        log.info("Getting user with id {}", userId);
        return userMapper.mapTo(userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с id " + userId + " не найден")));
    }

    public List<UserDto> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll().stream().map(userMapper::mapTo).collect(Collectors.toList());
    }

    public UserDto updateUser(UserDto userDto, long userId) {
        log.info("Update user with id {}", userId);
        userDto.setId(userId);
        User userB = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с id " + userId + " не найден"));
        User user = userMapper.mapFrom(userDto, userB);
        userRepository.save(user);
        return userMapper.mapTo(userRepository.findById(userId).get());
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("Deleted user with id {}", userId);
    }
}
