package com.calendardev.calendardevelop.common;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordManager {

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public boolean isPasswordMatch(String inputPassword, String encodedPassword){
        return passwordEncoder.matches(inputPassword, encodedPassword);
    }

}
