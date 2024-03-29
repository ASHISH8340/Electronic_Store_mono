package com.electronicstore.repository;

import com.electronicstore.model.Order;
import com.electronicstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);
}
