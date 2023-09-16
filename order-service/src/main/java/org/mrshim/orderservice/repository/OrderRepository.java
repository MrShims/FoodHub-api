package org.mrshim.orderservice.repository;

import org.mrshim.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {


    public List<Order> findAllByUserId(String userId);

}
