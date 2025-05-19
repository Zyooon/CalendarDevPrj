package com.calendardev.calendardevelop.repository;

import com.calendardev.calendardevelop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
