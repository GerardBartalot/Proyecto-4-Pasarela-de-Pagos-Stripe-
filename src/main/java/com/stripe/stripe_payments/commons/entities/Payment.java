package com.stripe.stripe_payments.commons.entities;

import com.stripe.stripe_payments.commons.enums.StripeEventEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String paymentIntentId;
    private String customerId;
    private String productId;
    private Long amount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private StripeEventEnum type;
}
