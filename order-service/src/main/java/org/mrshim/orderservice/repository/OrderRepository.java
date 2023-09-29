package org.mrshim.orderservice.repository;

import org.mrshim.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


    public List<Order> findAllByUserId(String userId);

    public List<Order> findByStatus(String status);

}
