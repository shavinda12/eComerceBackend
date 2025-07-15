package com.ecommercebackend.store.exceptions;

public class CartEmptyException extends RuntimeException{
    public CartEmptyException() {
        super("cart is empty");
    }
}
