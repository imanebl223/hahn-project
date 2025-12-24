package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.UserDTO;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .map(UserDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(
            @Valid @RequestBody User user,
            Authentication authentication) {
        return userService.updateUser(authentication.getName(), user)
                .map(UserDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser(Authentication authentication) {
        userService.deleteUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
