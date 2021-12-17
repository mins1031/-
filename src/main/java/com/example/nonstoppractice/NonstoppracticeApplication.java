package com.example.nonstoppractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NonstoppracticeApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/Users/macbookair/app/config/real-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(NonstoppracticeApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
