package com.stripe.stripe_payments.services;

import com.stripe.model.Event;

public interface StripeService {
    void manageWebhook(Event event);
    Event constructEvent(String payload, String stripeHeader);
}
