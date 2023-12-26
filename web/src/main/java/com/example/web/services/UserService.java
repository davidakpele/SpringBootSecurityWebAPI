package com.example.web.services;

import com.example.web.mapstruct.UsersDto;
import com.example.web.model.User;

import java.util.List;

public interface UserService {

    List<UsersDto> findAllUsers();

    User findUserById(Long id);

    void saveUser(User user);

    void updateUser(Long id, User user);

    void deleteUser(Long id);

    User registerUser(User user);

    List<Long> checkUserIds(List<Long> userIds);

}
