package com.example.demo4.controller;

import com.example.demo4.dto.UserDTO;
import com.example.demo4.model.User;
import com.example.demo4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // GET all users
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getName(), user.getSalary()))
                .collect(Collectors.toList());
    }

    // POST create a new user
    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO.getName(), userDTO.getSalary());
        user = userRepository.save(user);
        return new UserDTO(user.getName(), user.getSalary());
    }

    // PUT update an existing user
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        user.setName(userDTO.getName());
        user.setSalary(userDTO.getSalary());

        user = userRepository.save(user);
        return new UserDTO(user.getName(), user.getSalary());
    }

    // DELETE user by ID
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }
        userRepository.deleteById(id);
        return "User deleted successfully with id " + id;
    }
}
