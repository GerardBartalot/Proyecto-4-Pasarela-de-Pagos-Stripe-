package com.stripe.stripe_payments.strategy.impl;

import com.stripe.model.Event;
import com.stripe.stripe_payments.commons.enums.StripeEventEnum;
import com.stripe.stripe_payments.strategy.StripeStrategy;
import org.springframework.stereotype.Component;

@Component
public class StripeStrategyCheckoutSessionCompleted implements StripeStrategy {
    @Override
    public boolean isApplicable(Event event) {
        return StripeEventEnum.CHECKOUT_SESSION_COMPLETED.value.equals(event.getType());

    }

    @Override
    public Event process(Event event) {
        return null;
    }
}
