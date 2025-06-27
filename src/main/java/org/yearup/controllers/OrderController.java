package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.OrderItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@PreAuthorize("isAuthenticated()")
public class OrderController
{
    private final OrderDao orderDao;
    private final ShoppingCartDao cartDao;
    private final UserDao userDao;

    @Autowired
    public OrderController(OrderDao orderDao, ShoppingCartDao cartDao, UserDao userDao)
    {
        this.orderDao = orderDao;
        this.cartDao = cartDao;
        this.userDao = userDao;
    }

    // POST /orders
    // Create a new order from the shopping cart
    @PostMapping
    public void createOrder(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();



            ShoppingCart cart = cartDao.getCart(userId);
            System.out.println("DEBUG: Retrieved cart = " + cart);
            System.out.println("DEBUG: Cart items = " + cart.getItems());
            System.out.println("DEBUG: Cart items isEmpty = " + (cart.getItems() == null ? "null" : cart.getItems().isEmpty()));

            if (cart.getItems().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shopping cart is empty.");
            }

            List<OrderItem> orderItems = new ArrayList<>();
            for (ShoppingCartItem cartItem : cart.getItems().values())
            {
                if (cartItem == null)
                {
                    System.out.println("cartItem is null!");
                    continue;
                }

                Product product = cartItem.getProduct();
                if (product == null)
                {
                    System.out.println("cartItem.getProduct() is null!");
                    continue;
                }

                System.out.println("DEBUG: Product = " + product);
                System.out.println("DEBUG: ProductId = " + product.getId());
                System.out.println("DEBUG: Quantity = " + cartItem.getQuantity());
                System.out.println("DEBUG: Price = " + product.getPrice());

                OrderItem item = new OrderItem();
                item.setProductId(product.getId());
                item.setQuantity(cartItem.getQuantity());
                item.setUnitPrice(product.getPrice());
                orderItems.add(item);
            }

            Order order = new Order();
            order.setUserId(userId);
            order.setOrderDate(LocalDateTime.now());
            order.setTotal(cart.getTotal());
            order.setItems(orderItems);

            orderDao.create(order);
            cartDao.clearCart(userId);
        }
        catch (ResponseStatusException e)
        {
            throw e; // Re-throw client-side errors like 400 so they’re not masked
        }
        catch (Exception e)
        {
            e.printStackTrace(); // Still log the unexpected error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create order.");
        }
    }

    // GET /orders
    @GetMapping
    public List<Order> getOrders(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            return orderDao.getOrdersByUserId(user.getId());
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch orders.");
        }
    }

    // GET /orders/{orderId}
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable int orderId, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Order order = orderDao.getById(orderId);

            if (order == null || order.getUserId() != userId)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
            }

            return order;
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch order.");
        }
    }
}
