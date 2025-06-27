package org.yearup.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart
{
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();

    // ===== Getters/Setters =====
    public Map<Integer, ShoppingCartItem> getItems()
    {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items)
    {
        this.items = items;
    }

    // ===== Core Methods =====

    public boolean contains(int productId)
    {
        return items.containsKey(productId);
    }

    public void add(ShoppingCartItem item)
    {
        items.put(item.getProduct().getId(), item);
    }

    public ShoppingCartItem get(int productId)
    {
        return items.get(productId);
    }

    public void updateQuantity(int productId, int quantity)
    {
        if (items.containsKey(productId))
        {
            ShoppingCartItem item = items.get(productId);
            item.setQuantity(quantity);
        }
    }
    public ShoppingCartItem getItem(int productId)
    {
        return items.get(productId);
    }


    public void removeProduct(int productId)
    {
        items.remove(productId);
    }

    public void clear()
    {
        items.clear();
    }

    public BigDecimal getTotal()
    {
        return items.values().stream()
                .map(ShoppingCartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void remove(int productId)
    {
        items.remove(productId);
    }
}
