package com.stripe.stripe_payments.services.impl;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.stripe_payments.services.StripeService;
import com.stripe.stripe_payments.strategy.StripeStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StripeServiceImpl implements StripeService {
    private final String endpointSecret;
    private final List<StripeStrategy> stripeStrategies;

    public StripeServiceImpl(@Value("${stripe.endpoint.secret}") String endpointSecret, List<StripeStrategy> stripeStrategies) {
        this.endpointSecret = endpointSecret;
        this.stripeStrategies = Collections.unmodifiableList(stripeStrategies);
    }

    @Override
    public void manageWebhook(Event event) {
        System.out.println("Evento recibido en manageWebhook: " + event.getType());

        Optional.of(event)
                .map(this::processStrategy)
                .ifPresentOrElse(
                        e -> System.out.println("Evento procesado correctamente: " + e.getType()),
                        () -> System.err.println("No se pudo procesar el evento: " + event.getType())
                );
    }

    private Event processStrategy(Event event) {
        System.out.println("Buscando estrategia para evento: " + event.getType());

        return stripeStrategies.stream()
                .filter(stripeStrategy -> stripeStrategy.isApplicable(event))
                .peek(strategy -> System.out.println("Estrategia encontrada: " + strategy.getClass().getName()))
                .findFirst()
                .map(stripeStrategy -> {
                    System.out.println("Procesando evento con estrategia: " + stripeStrategy.getClass().getName());
                    return stripeStrategy.process(event);
                })
                .orElseGet(() -> {
                    System.err.println("No se encontró una estrategia aplicable para el evento: " + event.getType());
                    return new Event();
                });
    }

    @Override
    public Event constructEvent(String payload, String stripeHeader) {
        try {
            Event event = Webhook.constructEvent(payload, stripeHeader, endpointSecret);
            System.out.println("Evento construido correctamente: " + event.getType());
            return event;
        } catch (SignatureVerificationException e) {
            System.err.println("Error verificando la firma del webhook: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
