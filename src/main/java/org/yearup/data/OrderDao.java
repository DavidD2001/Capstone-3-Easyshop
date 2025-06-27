package org.yearup.data;

import org.yearup.models.Order;

import java.util.List;

public interface OrderDao
{
    // Create a new order
    void create(Order order);

    // Get all orders by a specific user
    List<Order> getOrdersByUserId(int userId);

    // Get a specific order by its ID
    Order getById(int orderId);
}
