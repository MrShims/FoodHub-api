package org.mrshim.orderservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableOrderResponseListDTO {
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private int quantity;
}
