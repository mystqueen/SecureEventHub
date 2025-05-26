package com.example.secure_event_hub.repository;

import com.example.secure_event_hub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

