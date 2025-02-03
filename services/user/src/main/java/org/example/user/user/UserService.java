package org.example.user.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.user.exception.UserNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @CacheEvict(value = "userCache", allEntries = true)
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Cacheable(value = "userCache")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "userCache", key = "#id")
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setEmail(userDto.email());
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toUserDto(userRepository.save(user));
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
