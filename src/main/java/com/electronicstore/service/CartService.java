package com.electronicstore.service;


import com.electronicstore.dto.AddItemToCartRequest;
import com.electronicstore.dto.CartDto;

public interface CartService {
    //add items to cart:
    //case1: cart for user is not available: we will create the cart and then add the item
    //case2: cart available add the items to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart:
    Object removeItemFromCart(String userId,int cartItem);

    //remove all items from cart
    Object clearCart(String userId);

    CartDto getCartByUser(String userId);
}
