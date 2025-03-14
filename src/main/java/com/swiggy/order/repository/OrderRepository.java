package com.swiggy.order.repository;

import com.swiggy.order.entity.Order;
import com.swiggy.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndUser(Long orderId, User user);
}
