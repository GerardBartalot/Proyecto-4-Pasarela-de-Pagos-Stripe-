package com.stripe.stripe_payments.strategy.impl;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.stripe_payments.commons.entities.Payment;
import com.stripe.stripe_payments.commons.enums.StripeEventEnum;
import com.stripe.stripe_payments.repositories.PaymentRepository;
import com.stripe.stripe_payments.strategy.StripeStrategy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StripeStrategyPaymentIntentSucceed implements StripeStrategy {
    private final PaymentRepository paymentRepository;

    public StripeStrategyPaymentIntentSucceed(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public boolean isApplicable(Event event) {
        return StripeEventEnum.PAYMENT_INTENT_SUCCEED.value.equals(event.getType());
    }

    @Override
    public Event process(Event event) {
        return Optional.of(event)
                .map(this::deserialize)
                .map(this::mapToEntity)
                .map(paymentRepository::save)
                .map(given -> event)
                .orElseThrow(() -> new RuntimeException("Error processing"));
    }

    private Payment mapToEntity(PaymentIntent paymentIntent) {
        return Payment.builder()
                .paymentIntentId(paymentIntent.getId())
                .customerId(paymentIntent.getCustomer())
                .amount(paymentIntent.getAmount())
                .currency(paymentIntent.getCurrency())
                .type(StripeEventEnum.PAYMENT_INTENT_SUCCEED)
                .build();
    }

    private PaymentIntent deserialize(Event event) {
        return (PaymentIntent) event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> new RuntimeException("Error deserializing"));
    }
}
