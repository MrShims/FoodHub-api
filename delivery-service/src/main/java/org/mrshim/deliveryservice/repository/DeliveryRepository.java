package org.mrshim.deliveryservice.repository;

import org.mrshim.deliveryservice.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {


    List<Delivery> findAllByCourierIdIsNull();

    Optional<Delivery> findByOrderId(Long orderId);

    List<Delivery> findAllByCourierId(String courierId);


}
