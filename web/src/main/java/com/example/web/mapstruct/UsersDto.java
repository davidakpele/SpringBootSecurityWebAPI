package com.example.web.mapstruct;

import com.example.web.model.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String photoUrl;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public static UsersDto fromEntity(User user) {
        UsersDto dto = new UsersDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        // Map other fields as needed
        return dto;
    }
}
