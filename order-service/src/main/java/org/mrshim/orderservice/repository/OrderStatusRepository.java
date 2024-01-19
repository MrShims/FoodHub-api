package org.mrshim.orderservice.repository;

import org.mrshim.orderservice.enums.OrderStatusEnum;
import org.mrshim.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus,Long> {
    Optional<OrderStatus> findByStatusName(OrderStatusEnum statusName);
}
