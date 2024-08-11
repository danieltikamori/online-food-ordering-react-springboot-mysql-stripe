package me.amlu.service;

public class CartItemNotFoundException extends Exception {
    public CartItemNotFoundException(String message) {

        super(message);
    }
}
