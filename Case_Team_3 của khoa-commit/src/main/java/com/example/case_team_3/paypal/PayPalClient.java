package com.example.case_team_3.paypal;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PayPalClient {

    private final PayPalHttpClient client;

    public PayPalClient(@Value("${paypal.client-id}") String clientId,
                        @Value("${paypal.secret}") String secret,
                        @Value("${paypal.mode}") String mode) {
        PayPalEnvironment environment;
        if ("sandbox".equalsIgnoreCase(mode)) {
            environment = new PayPalEnvironment.Sandbox(clientId, secret);
        } else {
            environment = new PayPalEnvironment.Live(clientId, secret);
        }
        this.client = new PayPalHttpClient(environment);
    }

    public PayPalHttpClient client() {
        return this.client;
    }
}
