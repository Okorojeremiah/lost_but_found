//package com.winners.lostbutfound.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import lombok.NonNull;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//
//import javax.net.ssl.SSLContext;
//import java.security.NoSuchAlgorithmException;
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//public class MongoConfig extends AbstractMongoClientConfiguration {
//    @Override
//    @NonNull
//    protected String getDatabaseName() {
//        return "test";
//    }
//
//    @Override
//    @NonNull
//    public MongoClient mongoClient() {
//        ConnectionString connectionString = new ConnectionString(String.format("mongodb+srv://Slimjay91:Slimjay91@lostbutfound.cwpcg.mongodb.net/lostbutfound?retryWrites=true&w=majority&appName=LostButFound", "lostbutfound.cwpcg.mongodb.net", "27017", "test"));
//        MongoClientSettings clientSettings = MongoClientSettings.builder()
//                .retryWrites(true)
//                .applyConnectionString(connectionString)
//                .applyToSslSettings(builder -> {
//                    try {
//                        builder.enabled(true)
//                                .context(SSLContext.getInstance("TLSv1.2"));
//                    } catch (NoSuchAlgorithmException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//
//                .applyToConnectionPoolSettings(builder -> {
//                    builder.maxSize(100)
//                            .minSize(5)
//                            .maxConnectionLifeTime(30, TimeUnit.MINUTES)
//                            .maxConnectionIdleTime(300000, TimeUnit.MILLISECONDS);
//                })
//                .applyToSocketSettings(builder -> {
//                    builder.connectTimeout(2000, TimeUnit.MILLISECONDS)
//                            .readTimeout(3000, TimeUnit.MILLISECONDS); // Optional: Set read timeout
//                })
//                .applicationName("LostButFound")
//                .build();
//
//        return MongoClients.create(clientSettings);
//    }
//
//}
