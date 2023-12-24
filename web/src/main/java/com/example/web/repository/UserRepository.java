package com.example.web.repository;

import com.example.web.mapstruct.UsersDto;
import com.example.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT MAX(id) FROM users", nativeQuery = true)
    Long findMaxUserId();
}
