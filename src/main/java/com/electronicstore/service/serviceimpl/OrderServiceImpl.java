package com.electronicstore.service.serviceimpl;

import com.electronicstore.dto.CreateOrderRequest;
import com.electronicstore.dto.OrderDto;
import com.electronicstore.dto.PageableResponse;
import com.electronicstore.exceptions.BadApiRequest;
import com.electronicstore.exceptions.IdNotFoundException;
import com.electronicstore.exceptions.Response;
import com.electronicstore.helper.Helper;
import com.electronicstore.model.*;
import com.electronicstore.repository.CartRepository;
import com.electronicstore.repository.OrderRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        try{
            logger.info("Inside createOrder of OrderServiceImpl");
            String userId = orderDto.getUserId();
            String cartId = orderDto.getCartId();
            Optional<User> findByUserId = userRepository.findById(userId);
            Optional<Cart> findByCartId = cartRepository.findById(cartId);
            if(findByUserId.isEmpty()){
                throw new IdNotFoundException("User id doesn't exist !!");
            }
            if(findByCartId.isEmpty()){
                throw new IdNotFoundException("Cart id doesn't exist !!");
            }
            User user = findByUserId.get();
            Cart cart = findByCartId.get();
            List<CartItem> cartItems = cart.getItems();

            if(cartItems.size() <= 0){
                throw new BadApiRequest("Invalid number of items in cart !!");
            }

            Order order = Order.builder()
                    .billingName(orderDto.getBillingName())
                    .billingPhone(orderDto.getBillingPhone())
                    .billingAddress(orderDto.getBillingAddress())
                    .orderedDate(new Date())
                    .deliveredDate(null)
                    .paymentStatus(orderDto.getPaymentStatus())
                    .orderStatus(orderDto.getOrderStatus())
                    .orderId(UUID.randomUUID().toString())
                    .user(user)
                    .build();


            AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
            List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
                OrderItem orderItem = OrderItem.builder()
                        .quantity(cartItem.getQuantity())
                        .product(cartItem.getProduct())
                        .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                        .order(order)
                        .build();

                orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
                return orderItem;
            }).collect(Collectors.toList());

            order.setOrderItems(orderItems);
            order.setOrderAmount(orderAmount.get());

            cart.getItems().clear();
            cartRepository.save(cart);
            Order savedOrder = orderRepository.save(order);

            OrderDto dto = modelMapper.map(savedOrder, OrderDto.class);
            return new Response<>("Order Create Successfully", "1", dto).getData();

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in createOrder of OrderServiceImpl : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public Object removeOrder(String orderId) {
        try{
            logger.info("Inside removeOrder of OrderServiceImpl");
            Optional<Order> findByOrderId = orderRepository.findById(orderId);
            if(findByOrderId.isEmpty()){
                throw new IdNotFoundException("Order id doesn't exist !!");
            }
            Order order = findByOrderId.get();
            orderRepository.delete(order);
            return new Response<>("Order delete Successfully", "1", order);
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in removeOrder of OrderServiceImpl : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        try{
            logger.info("Inside getOrdersOfUser of OrderServiceImpl");
            Optional<User> findByUserId = userRepository.findById(userId);
            if(findByUserId.isEmpty()){
                throw new IdNotFoundException("User id doesn't exist !!");
            }
            User user = findByUserId.get();
            List<Order> orders = orderRepository.findByUser(user);
            List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
            return new Response<>("Order fetched Successfully", "1", orderDtos).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getOrdersOfUser of OrderServiceImpl : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        try{
            logger.info("Inside getOrders of OrderServiceImpl");
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Order> page = orderRepository.findAll(pageable);
            PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(page, OrderDto.class);
            return new Response<>("Order fetched Successfully", "1", pageableResponse).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getOrders of OrderServiceImpl : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }
}
