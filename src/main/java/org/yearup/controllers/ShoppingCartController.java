package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // GET current user's cart
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.getCart(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // POST add product to cart
    @PostMapping("/products/{productId}")
    public void addToCart(@PathVariable int productId, @RequestBody Map<String, Integer> body, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Product product = productDao.getById(productId);
            if (product == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
            }

            ShoppingCart cart = shoppingCartDao.getCart(userId);
            ShoppingCartItem item = cart.getItem(productId);

            if (item != null)
            {
                int quantity = item.getQuantity() + 1;
                shoppingCartDao.updateProduct(userId, productId, quantity);
            }
            else
            {
                ShoppingCartItem newItem = new ShoppingCartItem(product, 1);
                shoppingCartDao.addProduct(userId, newItem);
            }
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add product to cart.");
        }
    }

    // PUT update product quantity
    @PutMapping("/products/{productId}")
    public void updateProduct(@PathVariable int productId,
                              @RequestBody ShoppingCartItem item,
                              Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.updateProduct(userId, productId, item.getQuantity());
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update product quantity.");
        }
    }

    // DELETE remove specific product from cart
    @DeleteMapping("/products/{productId}")
    public void removeProduct(@PathVariable int productId, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.removeProduct(userId, productId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to remove product from cart.");
        }
    }

    // DELETE clear entire cart
    @DeleteMapping
    public void clearCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.clearCart(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to clear cart.");
        }
    }
}
