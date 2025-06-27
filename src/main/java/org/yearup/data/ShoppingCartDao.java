package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;



public interface ShoppingCartDao
{
    ShoppingCart getCart(int userId); // renamed for clarity (was getByUserId)

    void addProduct(int userId, ShoppingCartItem item);

    void updateProduct(int userId, int productId, int quantity);

    void clearCart(int userId);

    void removeProduct(int userId, int productId);

}
