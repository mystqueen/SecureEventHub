package com.example.secure_event_hub.services;

import com.example.secure_event_hub.dto.UserDto;
import com.example.secure_event_hub.exception.UserAlreadyExistException;
import com.example.secure_event_hub.exception.UserNotFoundException;
import com.example.secure_event_hub.model.Role;
import com.example.secure_event_hub.model.Status;
import com.example.secure_event_hub.model.User;
import com.example.secure_event_hub.repository.UserRepository;
import com.example.secure_event_hub.utils.InputValidations;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final OtpService otpService;
    private final RedisTemplate<String, String> redisTemplate;
    Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    String intro = "User with email ";

    public UserServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, ObjectMapper objectMapper, OtpService otpService, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.otpService = otpService;
        this.redisTemplate = redisTemplate;
    }

    private Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto registerUser(User user) {
        InputValidations.validateEmail(user.getEmail());
        InputValidations.validatePassword(user.getPassword());

        Optional<User> userExists = userRepository.findByUserName(user.getUserName());
        if (getUser(user.getEmail()).isPresent() || userExists.isPresent()) {
            logger.info("User already exists");
            throw new UserAlreadyExistException(intro + user.getEmail() + "or user with username " + user.getUserName() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        user.setStatus(Status.FRESH);
        user.setRole(Role.USER);
        String userOtp = otpService.generateOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), userOtp);
        return objectMapper.convertValue(userRepository.save(user), UserDto.class);
    }

    @Override
    public Boolean verifyUser(String email, String otp) {
        InputValidations.validateEmail(email);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(intro + email + "does not exist"));

        if(user.getStatus() == Status.VERIFIED){
            throw new UserAlreadyExistException("User is already verified!");
        }

        String storedOtp = redisTemplate.opsForValue().get(email);
        if(storedOtp == null || !storedOtp.equals(otp)){
            throw new EntityNotFoundException("Invalid OTP");
        }
        otpService.verifyOtp(email, otp);
        user.setStatus(Status.VERIFIED);
        userRepository.save(user);
        redisTemplate.delete(email);
        return true;
    }

    public String resendOtp(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(intro + email + "does not exist"));
        if (user.getStatus() == Status.VERIFIED) {
            throw new UserAlreadyExistException("User is already verified");
        }
        String storedOtp = redisTemplate.opsForValue().get(email);
        if (storedOtp != null) {
            return "Current OTP is still valid";
        }
        otpService.generateOtp(email);
        return "New OTP sent";
    }

}
