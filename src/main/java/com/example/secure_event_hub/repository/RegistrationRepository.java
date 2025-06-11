package com.example.secure_event_hub.repository;

import com.example.secure_event_hub.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
