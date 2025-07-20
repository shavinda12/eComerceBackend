package com.ecommercebackend.store.payments;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class  CheckOutSession {
    private String checkoutUrl;
}
