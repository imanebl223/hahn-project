package com.example.taskmanagement.service;

import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Optional<User> updateUser(String email, User userDetails) {
        if (email == null || userDetails == null) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email)
                .map(existingUser -> {
                    if (userDetails.getName() != null) {
                        existingUser.setName(userDetails.getName());
                    }
                    if (userDetails.getEmail() != null) {
                        existingUser.setEmail(userDetails.getEmail());
                    }
                    if (userDetails.getPassword() != null) {
                        existingUser.setPassword(userDetails.getPassword());
                    }
                    return userRepository.save(existingUser);
                });
    }

    @Transactional
    public void deleteUser(String email) {
        if (email != null) {
            userRepository.findByEmail(email)
                    .ifPresent(user -> userRepository.deleteById(user.getId()));
        }
    }
}
