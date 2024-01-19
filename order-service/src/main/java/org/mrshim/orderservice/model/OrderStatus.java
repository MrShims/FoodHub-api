package org.mrshim.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mrshim.orderservice.enums.OrderStatusEnum;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tbl_order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status_name")
    private OrderStatusEnum statusName;
}
