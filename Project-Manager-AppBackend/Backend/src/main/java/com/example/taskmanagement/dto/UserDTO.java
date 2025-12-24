package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.User;

public record UserDTO(
    Long id,
    String email,
    String name
) {
    public static UserDTO from(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
            user.getId(), 
            user.getEmail(),
            user.getName()
        );
    }
}
