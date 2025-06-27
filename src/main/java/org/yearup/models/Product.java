package org.yearup.models;

import java.math.BigDecimal;

public class Product
{
    // fields
    private int id;
    private String name;
    private BigDecimal price;
    private int categoryId;
    private String description;
    private String color;
    private int stock;
    private boolean isFeatured;
    private String imageUrl;

    // empty constructor
    public Product() {}

    // full constructor
    public Product(int id, String name, BigDecimal price, int categoryId, String description,
                   String color, int stock, boolean isFeatured, String imageUrl)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.description = description;
        this.color = color;
        this.stock = stock;
        this.isFeatured = isFeatured;
        this.imageUrl = imageUrl;
    }

    // GETTERS & SETTERS

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }

    public boolean isFeatured()
    {
        return isFeatured;
    }

    public void setFeatured(boolean featured)
    {
        isFeatured = featured;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    // Optional: Override toString() for easier debugging
    @Override
    public String toString()
    {
        return String.format("Product{id=%d, name='%s', price=%s}", id, name, price);
    }
}
