package com.stripe.stripe_payments.services;

import com.stripe.stripe_payments.commons.dto.AuthResponseDto;
import com.stripe.stripe_payments.commons.dto.UserRequest;

public interface AuthService {
    AuthResponseDto createUser(UserRequest userRequest);
}
