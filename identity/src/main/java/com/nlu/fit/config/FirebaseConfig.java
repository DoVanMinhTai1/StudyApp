package com.nlu.fit.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public DatabaseReference databaseReference() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference;
    }

    @Value("${firebase.service.account.path}")
    private String accountPath;

    @Value("${firebase.service.database.url}")
    private String databaseUrl;

    @PostConstruct
    public void init() throws IOException {

        if (FirebaseApp.getApps().isEmpty()) {
            InputStream inputStream = FirebaseConfig.class.getClassLoader().getResourceAsStream(accountPath);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
