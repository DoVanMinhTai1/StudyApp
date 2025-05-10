package com.nlu.fit.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.nlu.fit.service.FirebaseAuthService;
import com.nlu.fit.viewmodel.user.LoginReponse;
import com.nlu.fit.viewmodel.user.LoginRequest;
import com.nlu.fit.viewmodel.user.TokenReponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.nlu.fit.model.User;
import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthController {
    private final FirebaseAuthService firebaseAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginReponse response = firebaseAuthService.loginWithEmailPassword(loginRequest.email(),loginRequest.password());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error","Invalid Email Or Password"));
        }
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<FirebaseToken> verifyToken(@RequestBody TokenReponse tokenReponse ) throws FirebaseAuthException {
        String token = tokenReponse.token();
        System.out.println("token: " + token);
        return ResponseEntity.ok(firebaseAuthService.verifyToken(token));
    }

    @GetMapping("/getUserProfile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal User user) throws FirebaseAuthException {
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok("User UUID" + user.getEmail());
    }

}
