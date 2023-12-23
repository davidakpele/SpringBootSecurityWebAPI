package com.example.web.controllers;

import com.example.web.auth.AuthenticationService;
import com.example.web.exceptions.ErrorResponse;
import com.example.web.exceptions.SuccessResponse;
import com.example.web.mapstruct.UsersDto;
import com.example.web.model.User;
import com.example.web.repository.UserRepository;
import com.example.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class ApiController {

    private final UserService userService;

    @Autowired
    public ApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public List<UsersDto> listUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object>  getUser(@PathVariable Long id, @RequestBody User user){
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
    public ResponseEntity<Object> deleteUser(@PathVariable Long id, @RequestBody User user) {
        try {
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

}
