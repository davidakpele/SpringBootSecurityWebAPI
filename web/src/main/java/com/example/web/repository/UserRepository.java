package com.example.web.repository;

import com.example.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id IN :userIds")
    List<User> findAllByIdIn(@Param("userIds") List<Integer> userIds);


    @Modifying
    @Query("DELETE FROM User u WHERE u.id IN :userIds")
    void deleteById(@Param("userIds") List<Long> userIds);

}
