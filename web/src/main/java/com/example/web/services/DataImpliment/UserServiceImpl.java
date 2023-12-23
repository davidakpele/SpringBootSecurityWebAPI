package com.example.web.services.DataImpliment;


import com.example.web.mapstruct.UsersDto;
import com.example.web.model.User;
import com.example.web.repository.UserRepository;
import com.example.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UsersDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UsersDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, User user) {
        User existingUser = userRepository.findById(Math.toIntExact(id)).orElse(null);
        if (existingUser != null) {
            // Update the fields you want to change
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setAddress(user.getAddress());
            // Set other fields as needed

            userRepository.save(existingUser);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(Math.toIntExact(id));
    }
}
