package com.stripe.stripe_payments.services;

import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.stripe_payments.commons.dto.CheckoutRequest;
import com.stripe.stripe_payments.commons.dto.CheckoutResponse;

public interface StripeService {
    void manageWebhook(Event event);
    Event constructEvent(String payload, String stripeHeader);
    Customer createCustomer(String email);
    Product createProduct(String name);
    Price createPrice(String productId);

    CheckoutResponse createCheckout(CheckoutRequest checkoutRequest);
}
