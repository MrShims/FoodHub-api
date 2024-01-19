package org.mrshim.orderservice.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    ACCEPT("ACCEPT"),
    PAID("PAID"),
    PREPARING("PREPARING"),
    DELIVER("DELIVER"),
    CLOSED("CLOSED"),
    CANSEL("CANSEL");

    private final String status;

    OrderStatusEnum(String status) {
        this.status = status;
    }
}
