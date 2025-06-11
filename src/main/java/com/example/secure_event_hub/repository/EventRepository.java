package com.example.secure_event_hub.repository;

import com.example.secure_event_hub.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
