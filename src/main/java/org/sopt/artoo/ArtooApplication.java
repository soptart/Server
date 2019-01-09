package org.sopt.artoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArtooApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArtooApplication.class, args);
    }
}

