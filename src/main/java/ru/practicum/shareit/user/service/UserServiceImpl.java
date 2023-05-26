package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.model.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
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
        @Valid User user = userMapper.mapFrom(userDto);
        return userMapper.mapTo(userRepository.createEntity(user));
    }

    public UserDto getUser(long userId) {
        log.info("Getting user with id {}", userId);
        return userMapper.mapTo(userRepository.getEntity(userId));
    }

    public List<UserDto> getUsers() {
        log.info("Getting all users");
        List<UserDto> list = userRepository.getEntities().stream().map(userMapper::mapTo).collect(Collectors.toList());
        return list;
    }

    public UserDto updateUser(UserDto userDto, long userId) {
        log.info("Update user with id {}", userId);
        userDto.setId(userId);
        User user = userMapper.mapFrom(userDto, userRepository.getEntity(userId));
        userRepository.updateEntity(user, userId);
        return userMapper.mapTo(userRepository.getEntity(userId));
    }

    public void deleteUser(long userId) {
        userRepository.deleteEntity(userId);
        log.info("Deleted user with id {}", userId);
    }
}
