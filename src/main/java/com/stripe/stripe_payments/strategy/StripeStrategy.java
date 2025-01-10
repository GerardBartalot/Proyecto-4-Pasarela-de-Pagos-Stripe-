package com.stripe.stripe_payments.strategy;

import com.stripe.model.Event;
import com.stripe.stripe_payments.commons.entities.Payment;

public interface StripeStrategy {
    boolean isApplicable(Event event);
    Event process(Event event);
}
