package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.HashMap;
import java.util.Map;

@Component
public class MySqlShoppingCartDao implements ShoppingCartDao
{
    // simulate database with in-memory map
    private final Map<Integer, ShoppingCart> cartStorage = new HashMap<>();

    @Override
    public ShoppingCart getCart(int userId)
    {
        // return cart if it exists, else create new one
        return cartStorage.computeIfAbsent(userId, id -> new ShoppingCart());
    }

    @Override
    public void addProduct(int userId, ShoppingCartItem item)
    {
        ShoppingCart cart = getCart(userId);
        if (cart.contains(item.getProductId()))
        {
            ShoppingCartItem existing = cart.get(item.getProductId());
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        }
        else
        {
            cart.add(item);
        }
    }

    @Override
    public void updateProduct(int userId, int productId, int quantity)
    {
        ShoppingCart cart = getCart(userId);
        if (cart.contains(productId))
        {
            cart.updateQuantity(productId, quantity);
        }
    }

    @Override
    public void clearCart(int userId)
    {
        ShoppingCart cart = getCart(userId);
        cart.clear();
    }

    @Override
    public void removeProduct(int userId, int productId)
    {
        ShoppingCart cart = getCart(userId);
        if (cart.contains(productId))
        {
            cart.remove(productId);
        }
    }
}
