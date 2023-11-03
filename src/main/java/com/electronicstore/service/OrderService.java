package com.electronicstore.service;

import com.electronicstore.dto.CreateOrderRequest;
import com.electronicstore.dto.OrderDto;
import com.electronicstore.dto.PageableResponse;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest orderDto);

    Object removeOrder(String orderId);

    List<OrderDto> getOrdersOfUser(String userId);

    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

}
