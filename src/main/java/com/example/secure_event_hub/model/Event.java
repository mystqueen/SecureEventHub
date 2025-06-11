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
@NoArgsConstructor
@RequiredArgsConstructor
public class Event {
    @Id
    @Column(name = "event_id", nullable = false)
    private String event_id;
    private String event_type;
    private String description;
    private LocalDateTime event_date;
    private User event_organiser;
}
