package com.example.web.controllers;

import com.example.web.exceptions.ErrorResponse;
import com.example.web.exceptions.SuccessResponse;
import com.example.web.mapstruct.DataWrapper;
import com.example.web.mapstruct.UsersDto;
import com.example.web.model.User;
import com.example.web.repository.UserRepository;
import com.example.web.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public List<UsersDto> listUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object>  getUser(@PathVariable Long id){
        try{
            // Check if the user with the given ID exists in the database
            User existingUser = userService.findUserById(id);
            if (existingUser == null) {
                // If user does not exist, return an error response
                ErrorResponse errorResponse = new ErrorResponse("User with ID " + id + " does not exist.", "404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }else {
                var Data= (User) userService.findUserById(id);
                return ResponseEntity.ok(Data);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error updating user data: " + e.getMessage(), "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            // Check if the user with the given ID exists in the database
            User existingUser = userService.findUserById(id);
            if (existingUser == null) {
                // If user does not exist, return an error response
                ErrorResponse errorResponse = new ErrorResponse("User with ID " + id + " does not exist.", "404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            // Update the user data if the user exists
            userService.updateUser(id, user);
            SuccessResponse successResponse = new SuccessResponse("Data has successfully been updated.", 200);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error updating user data: " + e.getMessage(), "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            logger.info(String.valueOf(id));
            // Check if the user with the given ID exists in the database
            User existingUser = userService.findUserById(id);
            if (existingUser == null) {
                // If user does not exist, return an error response
                ErrorResponse errorResponse = new ErrorResponse("User with ID " + id + " does not exist.", "404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }else {
                userService.deleteUser(id);
                SuccessResponse successResponse = new SuccessResponse("Data has successfully been deleted.", 200);
                return ResponseEntity.ok(successResponse);
            }
        } catch (Exception e) {

            ErrorResponse errorResponse = new ErrorResponse("Error updating user data: " + e.getMessage(), "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/deleteMultiple")
    public ResponseEntity<Object> checkUserIds(@RequestBody DataWrapper userIdsWrapper) {
        List<Long> userIds = userIdsWrapper.getData();
        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().body("User IDs array is empty");
        }
        List<User> users = userRepository.findAllById(userIds);

        List<Long> existingUserIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<Long> nonExistingUserIds = userIds.stream()
                .filter(id -> !existingUserIds.contains(id))
                .collect(Collectors.toList());

        for (Long userId : userIds) {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                userRepository.deleteById(userId);
            } else {
                // Handle the case where a user with the given ID is not found
                return ResponseEntity.badRequest().body("User with ID " + userId + " not found");
            }
        }
        return ResponseEntity.ok("Users with IDs " + userIds + " deleted successfully");
    }
}
