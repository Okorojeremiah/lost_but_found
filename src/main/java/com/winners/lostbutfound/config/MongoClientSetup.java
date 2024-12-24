//package com.winners.lostbutfound.config;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import jakarta.annotation.PreDestroy;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MongoClientSetup {
//
//    private MongoClient mongoClient;
//
//    @Value("${DB_URL}")
//    private String dbUrl;
//
//    @Bean
//    public MongoClient mongoClient() {
//        this.mongoClient = MongoClients.create("mongodb+srv://Slimjay91:Slimjay91@lostbutfound.cwpcg.mongodb.net/lostbutfound?retryWrites=true&w=majority&appName=LostButFound");
//        return this.mongoClient;
//    }
//
//    @PreDestroy
//    public void closeMongoClient() {
//        if (mongoClient != null) {
//            mongoClient.close();
//        }
//    }
//
//}
