package com.electronicstore.service.serviceimpl;

import com.electronicstore.dto.AddItemToCartRequest;
import com.electronicstore.dto.CartDto;
import com.electronicstore.exceptions.IdNotFoundException;
import com.electronicstore.exceptions.Response;
import com.electronicstore.model.Cart;
import com.electronicstore.model.CartItem;
import com.electronicstore.model.Product;
import com.electronicstore.model.User;
import com.electronicstore.repository.CartItemRepository;
import com.electronicstore.repository.CartRepository;
import com.electronicstore.repository.ProductRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new IdNotFoundException("Requested quantity is not valid !!");
        }

        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new IdNotFoundException("Product not found in database !!"));
        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new IdNotFoundException("user not found in database!!"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //perform cart operations
        //if cart items already present; then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItems);

        //create items
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }


        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public Object removeItemFromCart(String userId, int cartItem) {
        try{
            Optional<CartItem> findById = cartItemRepository.findById(cartItem);
            if(findById.isEmpty()){
                throw  new IdNotFoundException("CartItem doesn't exist");
            }
            CartItem cartItem1 = findById.get();

            cartItemRepository.delete(cartItem1);
            return new Response<>("CartItem deleted successfully", "1", cartItem1);

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in removeItemFromCart of CartServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public Object clearCart(String userId) {
        try{
            Optional<User> findById = userRepository.findById(userId);
            if(findById.isEmpty()){
                throw new IdNotFoundException("user not found in database!!");
            }
            User user = findById.get();
            Optional<Cart> findByUser = cartRepository.findByUser(user);
            if(findByUser.isEmpty()){
                throw new IdNotFoundException("Cart of given user not found !!");
            }

            Cart cart = findByUser.get();
            cart.getItems().clear();
            cartRepository.save(cart);
            return new Response<>("Cart deleted successfully", "1",cart).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in clearCart of CartServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public CartDto getCartByUser(String userId) {
        try{
            Optional<User> findById = userRepository.findById(userId);
            if(findById.isEmpty()){
                throw new IdNotFoundException("user not found in database!!");
            }
            User user = findById.get();
            Optional<Cart> findByUser = cartRepository.findByUser(user);
            if(findByUser.isEmpty()){
                throw new IdNotFoundException("Cart of given user not found !!");
            }

            Cart cart = findByUser.get();
            CartDto cartDto = mapper.map(cart, CartDto.class);
            return new Response<>("Cart fetched successfully", "1",cartDto).getData();

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getCartByUser of CartServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }
}
