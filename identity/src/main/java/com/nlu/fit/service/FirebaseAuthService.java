package com.nlu.fit.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.nlu.fit.viewmodel.LoginReponse;
import com.nlu.fit.viewmodel.UserProfileReponse;
import com.nlu.fit.viewmodel.VerifyTokenReponse;
import org.springframework.stereotype.Service;
@Service
public class FirebaseAuthService {

    public LoginReponse loginWithEmailPassword(String email, String password) {
        return null;
    }

    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        System.out.println(decodedToken.getUid());
        return decodedToken;
    }

    public UserRecord getUserProfile(String id) throws FirebaseAuthException {
        UserRecord userRecord  = FirebaseAuth.getInstance().getUser(id);
        System.out.println(userRecord.getUid());
        return userRecord;
    }

}
