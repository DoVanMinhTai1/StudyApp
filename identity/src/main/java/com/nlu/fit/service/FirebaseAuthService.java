package com.nlu.fit.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.nlu.fit.viewmodel.user.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;

    public LoginResponse loginWithEmailPassword(String email, String password) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUserByEmail(email);

        String token = firebaseAuth.createCustomToken(userRecord.getUid());

        return LoginResponse.builder().token(token).build();
    }

    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        return decodedToken;
    }

    public UserRecord getUserProfile(String id) throws FirebaseAuthException {
        UserRecord userRecord  = FirebaseAuth.getInstance().getUser(id);
        return userRecord;
    }

}
