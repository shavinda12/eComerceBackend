package com.ecommercebackend.store.payments;
import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.exceptions.CartEmptyException;
import com.ecommercebackend.store.exceptions.CartNotFoundException;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.OrderRepository;
import com.ecommercebackend.store.service.AuthService;
import com.ecommercebackend.store.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Value("${webSiteUrl}")
    private String webSiteUrl;

    @Transactional
    public CheckoutResponseDto checkout(CheckoutRequestDto request) {
        //this will create an order object
        var cart=cartRepository.findById(request.getCartId()).orElse(null);
        if(cart==null){
            System.out.println("error is in checkout service");
            throw new CartNotFoundException();
        }
        if(cart.getItems().isEmpty()){
            throw new CartEmptyException();
        }
        var order= Order.fromCart(cart,authService.getCurrentUser());
        orderRepository.save(order);
        //create a checkout session
        try{
            var session=paymentGateway.createSession(order);
            cartService.clearCart(request.getCartId());
            return new CheckoutResponseDto(order.getId(),session.getCheckoutUrl());
        }catch(PaymentNotFoundException e){
            orderRepository.delete(order);
            throw e;
        }


    }

    public void  handleWebHookEvent(WebhookRequest request){
        paymentGateway.parseWebHookEvent(request)
                .ifPresent(paymentResult->{
                    var order=orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus() );
                    orderRepository.save(order);
                });
    }

}
