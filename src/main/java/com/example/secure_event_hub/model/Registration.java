package com.example.secure_event_hub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Registration {
    @Id
    @Column(name = "registration_id", nullable = false)
    private String registration_id;
    private User user_id;
    private Event event_id;
    private String status;
    private LocalDateTime registration_time;
}
