package org.yearup.models;

import java.math.BigDecimal;

public class ShoppingCartItem
{
    private Product product;
    private int quantity;

    // ===== Constructors =====
    public ShoppingCartItem() {}

    public ShoppingCartItem(Product product, int quantity)
    {
        this.product = product;
        this.quantity = quantity;
    }

    // ===== Getters/Setters =====
    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public int getProductId()
    {
        return product != null ? product.getId() : -1; // safely get ID or return -1 if null
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    // ===== Helper Methods =====
    public BigDecimal getLineTotal()
    {
        if (product == null || product.getPrice() == null)
        {
            return BigDecimal.ZERO;
        }

        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
