package org.mrshim.transactionalservice.stripe;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.config.StripeConfig;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StripeClient {

    private final StripeConfig stripeConfig;

    public StripeClient(StripeConfig stripeConfig){
        this.stripeConfig = stripeConfig;
        Stripe.apiKey  = stripeConfig.getSecretKey();
    }



    public Charge chargeNewCard(String token, double amount) throws Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        return charge;

    }





}
