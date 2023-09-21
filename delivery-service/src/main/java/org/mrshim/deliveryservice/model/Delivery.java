package org.mrshim.deliveryservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "courier_id")
    private String courierId;

    @Column(name = "isDeliver")
    private boolean isDeliver;

    @Column(name = "delivery_address")
    private String deliveryAddress;


}
