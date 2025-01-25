package com.stripe.stripe_payments.commons.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto {
    private String customerId;
    private String productId;
}
