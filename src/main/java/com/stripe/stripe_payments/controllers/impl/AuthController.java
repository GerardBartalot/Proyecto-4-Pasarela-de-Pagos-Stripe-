package com.stripe.stripe_payments.controllers.impl;

import com.stripe.stripe_payments.commons.dto.AuthResponseDto;
import com.stripe.stripe_payments.commons.dto.UserRequest;
import com.stripe.stripe_payments.controllers.AuthApi;
import com.stripe.stripe_payments.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<AuthResponseDto> createUser(UserRequest userRequest) {
        return ResponseEntity.ok(authService.createUser(userRequest));
    }
}
