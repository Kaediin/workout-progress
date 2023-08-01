package com.daiken.workoutprogress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WorkoutProgressApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkoutProgressApplication.class, args);
    }

}
