package com.winners.lostbutfound.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;



@Configuration
public class DatabaseConfig {

    @Bean
    public CommandLineRunner dataInitializer(MongoTemplate mongoTemplate) {
        return args -> {
            mongoTemplate.dropCollection("user");
            mongoTemplate.createCollection("user");
            mongoTemplate.dropCollection("item");
            mongoTemplate.createCollection("item");
            mongoTemplate.dropCollection("refreshToken");
            mongoTemplate.createCollection("refreshToken");
        };
    }

}
