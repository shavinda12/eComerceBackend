package com.ecommercebackend.store.payments;

import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.entities.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


//This will implement the payment gateway interface
//this will implement the stripe payment
@Service
public class StripeGateWay implements PaymentGateway {

    @Value("${webSiteUrl}")
    private String webSiteUrl;

    @Value("${stripe.webHookSecretKey}")
    private String webHookSecretKey;

    @Override
    public CheckOutSession createSession(Order order) {

        //so first this will create session params including product name
        //qunatity,unit price.then this params are wrapped around a session
        //then this session is created and send the strip url to the user
        //then user can go to that url and purchase the item.
        //once user purchase the item then it will give user a success url or failed url

        try{
            var builder= SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webSiteUrl+"/checkout-success?orderId="+order.getId() )
                    .setCancelUrl(webSiteUrl+"/checkout-cancel")
                    .putMetadata("order_id",order.getId().toString());

            order.getItems().forEach(item->{
                var lineItem=SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmountDecimal(item.getUnit_price().multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build( )
                                         ).build()
                        ).build();
                builder.addLineItem(lineItem);
            });
            var session= Session.create(builder.build());
            return new CheckOutSession(session.getUrl());
        }catch(StripeException e){
            throw new PaymentNotFoundException();
        }

    }

    @Override
    public Optional<PaymentResult> parseWebHookEvent(WebhookRequest request) {
        var payload=request.getPayload();
        var signature=request.getHeaders().get("stripe-signature");
        try {
            var event= Webhook.constructEvent(payload ,signature,webHookSecretKey);
            switch(event.getType()){
                case "payment_intent.succeeded"->{
                    return Optional.of(new PaymentResult(extractOrderId(event),OrderStatus.PAID));
                }
                case "payment_intent.payment_ failed"->{
                    return Optional.of(new PaymentResult(extractOrderId(event),OrderStatus.FAILED ));
                }
                default->{
                    return Optional.empty();
                }
            }
        } catch (SignatureVerificationException e) {
            throw  new PaymentException("Invalid Signature");
        }

    }

    private Long extractOrderId(Event event){
        var stripeObject= event.getDataObjectDeserializer().getObject().orElseThrow(()-> new PaymentException("Could not deserialize stripe event.Check the sdk and api version "));
        var paymentIntent=(PaymentIntent) stripeObject;
         return Long.valueOf(paymentIntent.getMetadata().get("order_id")   );


    }
}
