package com.example.secure_event_hub.model;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @Column(nullable = false)
    private String user_id;
    private String userName;
    private String email;
    private String password;
    private Role role;
}
