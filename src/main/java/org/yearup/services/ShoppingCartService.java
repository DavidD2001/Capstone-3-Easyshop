package org.yearup.services;

import org.springframework.stereotype.Service;
import org.yearup.models.ShoppingCart;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShoppingCartService
{
    private Map<String, ShoppingCart> userCarts = new HashMap<>();

    public ShoppingCart getCart(String username)
    {
        return userCarts.computeIfAbsent(username, u -> new ShoppingCart());
    }

    public void clearCart(String username)
    {
        userCarts.remove(username);
    }
}
