package com.example.secure_event_hub.dto;

import com.example.secure_event_hub.model.Status;
import lombok.*;

import java.util.UUID;

@Data
public class UserDto {
    private UUID userId;
    private String userName;
    private String email;
    private Status status;
}
