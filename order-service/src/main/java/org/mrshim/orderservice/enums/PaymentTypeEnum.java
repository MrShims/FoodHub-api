package org.mrshim.orderservice.enums;

public enum PaymentTypeEnum {
    CREDIT_CARD("CREDIT CARD");

    private final String type;

    PaymentTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
