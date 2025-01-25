package com.stripe.stripe_payments.controllers.impl;

import com.stripe.stripe_payments.commons.dto.CheckoutRequest;
import com.stripe.stripe_payments.commons.dto.CheckoutResponse;
import com.stripe.stripe_payments.controllers.StripeApi;
import com.stripe.stripe_payments.services.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeController implements StripeApi {
    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @Override
    public ResponseEntity<Void> stripeWebhook(String payload, @RequestHeader(value = "Stripe-Signature", required = false) String stripeHeader) {
        System.out.println("Payload recibido: " + payload);
        System.out.println("Stripe-Signature: " + stripeHeader);

        try {
            var event = stripeService.constructEvent(payload, stripeHeader);
            System.out.println("Evento construido: " + event.getType());

            stripeService.manageWebhook(event);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error procesando webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CheckoutResponse> createCheckout(CheckoutRequest checkoutRequest) {
        return ResponseEntity.ok(stripeService.createCheckout(checkoutRequest));
    }

}
