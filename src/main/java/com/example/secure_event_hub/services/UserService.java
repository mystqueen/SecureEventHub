package com.example.secure_event_hub.services;


import com.example.secure_event_hub.dto.UserDto;
import com.example.secure_event_hub.model.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    UserDto registerUser(User user);
    Boolean verifyUser(String email, String otp);
    String resendOtp(String email);
}
