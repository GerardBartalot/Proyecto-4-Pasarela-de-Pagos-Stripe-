package com.stripe.stripe_payments.services.impl;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.net.Webhook;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
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

    public StripeServiceImpl(@Value("${stripe.endpoint.secret}") String endpointSecret, List<StripeStrategy> stripeStrategies,
                             @Value("${stripe.secret.key}") String stripeKey) {
        Stripe.apiKey = stripeKey;
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

    @Override
    public Customer createCustomer(String email) {
        var customerCreateParams = CustomerCreateParams.builder()
                .setEmail(email)
                .build();

        try {
            return Customer.create(customerCreateParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product createProduct(String name) {
        var productCreateParams = ProductCreateParams.builder()
                .setName(name)
                .setType(ProductCreateParams.Type.SERVICE)
                .build();

        try {
            return Product.create(productCreateParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Price createPrice(String productId) {
        var createPrice = PriceCreateParams.builder()
                .setCurrency("eur")
                .setProduct(productId)
                .setUnitAmount(4000L)
                .build();

        try {
            return Price.create(createPrice);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

}
