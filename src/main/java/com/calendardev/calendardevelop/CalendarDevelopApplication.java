package com.calendardev.calendardevelop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CalendarDevelopApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarDevelopApplication.class, args);
    }

}
