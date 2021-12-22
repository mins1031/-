package com.example.nonstoppractice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@EnableJpaAuditing
@SpringBootApplication
public class NonstoppracticeApplication {

//    @Autowired
//    private Environment environment;
//
//    private static String localYmlPath = "/Users/macbookair";
//    private static String liveYmlPath = "/home/ubuntu";

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "app/config/real-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(NonstoppracticeApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
//        SpringApplication.run(NonstoppracticeApplication.class);
    }

//    public String assortYmlFilePath() {
//        StringBuffer frontPath = new StringBuffer("spring.config.location=classpath:application.yml,");
//        String backPath = "/app/config/real-application.yml";
//        if (Arrays.stream(environment.getActiveProfiles()).findFirst().orElse("").equals("local")) {
//            return String.valueOf(frontPath.append(localYmlPath).append(backPath));
//        }
//
//        return String.valueOf(frontPath.append(liveYmlPath).append(backPath));
//    }

}
