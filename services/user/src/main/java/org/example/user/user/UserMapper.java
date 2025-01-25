package org.example.user.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toUser(UserDto userDto) {
        return User.builder()
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .email(userDto.email())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
