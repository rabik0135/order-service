package com.rabinchuk.orderservice.model;

public enum OrderStatus {
    CREATED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    PROCESSING,
    COMPLETED,
    CANCELED
}
