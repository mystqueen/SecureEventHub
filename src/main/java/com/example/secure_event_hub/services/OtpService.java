package com.example.secure_event_hub.services;

import jakarta.persistence.EntityExistsException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int OTP_EXP_MIN = 10;

    public OtpService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateOtp(String email) {
        String existingOtp = redisTemplate.opsForValue().get(email);
        if (existingOtp != null) {
            throw new EntityExistsException("An OTP already exists for this email: " + email);
        }
        String newOtp = generateRandomOtp();
        redisTemplate.opsForValue().set(email, newOtp, OTP_EXP_MIN, TimeUnit.MINUTES);
        return newOtp;
    }

    public void verifyOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(email);
        }
    }

    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}
