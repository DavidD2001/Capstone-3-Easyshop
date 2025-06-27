package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderItem;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Component
public class MySqlOrderDao implements OrderDao
{
    private final DataSource dataSource;

    @Autowired
    public MySqlOrderDao(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Order order)
    {
        String orderSql = "INSERT INTO orders (user_id, order_date, total) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS))
        {
            conn.setAutoCommit(false); // Start transaction

            orderStmt.setInt(1, order.getUserId());
            orderStmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            orderStmt.setBigDecimal(3, order.getTotal());
            orderStmt.executeUpdate();

            ResultSet keys = orderStmt.getGeneratedKeys();
            if (keys.next())
            {
                int orderId = keys.getInt(1);

                for (OrderItem item : order.getItems())
                {
                    try (PreparedStatement itemStmt = conn.prepareStatement(itemSql))
                    {
                        itemStmt.setInt(1, orderId);
                        itemStmt.setInt(2, item.getProductId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.setBigDecimal(4, item.getUnitPrice());
                        itemStmt.executeUpdate();
                    }
                }
            }

            conn.commit(); // Commit transaction
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error creating order: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(int userId)
    {
        List<Order> orders = new ArrayList<>();

        String orderSql = "SELECT * FROM orders WHERE user_id = ?";
        String itemsSql = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderSql))
        {
            orderStmt.setInt(1, userId);
            ResultSet orderResults = orderStmt.executeQuery();

            while (orderResults.next())
            {
                int orderId = orderResults.getInt("order_id");
                Timestamp orderDate = orderResults.getTimestamp("order_date");
                BigDecimal total = orderResults.getBigDecimal("total");

                Order order = new Order();
                order.setOrderId(orderId);
                order.setUserId(userId);
                order.setOrderDate(orderDate.toLocalDateTime());
                order.setTotal(total);

                // Fetch items for this order
                try (PreparedStatement itemStmt = conn.prepareStatement(itemsSql))
                {
                    itemStmt.setInt(1, orderId);
                    ResultSet itemResults = itemStmt.executeQuery();
                    List<OrderItem> items = new ArrayList<>();

                    while (itemResults.next())
                    {
                        OrderItem item = new OrderItem();
                        item.setOrderId(orderId);
                        item.setProductId(itemResults.getInt("product_id"));
                        item.setQuantity(itemResults.getInt("quantity"));
                        item.setUnitPrice(itemResults.getBigDecimal("unit_price"));
                        items.add(item);
                    }

                    order.setItems(items);
                }

                orders.add(order);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error fetching orders: " + e.getMessage(), e);
        }

        return orders;
    }

    @Override
    public Order getById(int orderId)
    {
        String orderSql = "SELECT * FROM orders WHERE order_id = ?";
        String itemsSql = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderSql))
        {
            orderStmt.setInt(1, orderId);
            ResultSet orderResults = orderStmt.executeQuery();

            if (orderResults.next())
            {
                Order order = new Order();
                order.setOrderId(orderResults.getInt("order_id"));
                order.setUserId(orderResults.getInt("user_id"));
                order.setOrderDate(orderResults.getTimestamp("order_date").toLocalDateTime());
                order.setTotal(orderResults.getBigDecimal("total"));

                // Load items
                try (PreparedStatement itemStmt = conn.prepareStatement(itemsSql))
                {
                    itemStmt.setInt(1, orderId);
                    ResultSet itemResults = itemStmt.executeQuery();
                    List<OrderItem> items = new ArrayList<>();

                    while (itemResults.next())
                    {
                        OrderItem item = new OrderItem();
                        item.setOrderId(orderId);
                        item.setProductId(itemResults.getInt("product_id"));
                        item.setQuantity(itemResults.getInt("quantity"));
                        item.setUnitPrice(itemResults.getBigDecimal("unit_price"));
                        items.add(item);
                    }

                    order.setItems(items);
                }

                return order;
            }
            else
            {
                return null; // No order found
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error fetching order by ID: " + e.getMessage(), e);
        }
    }
}
