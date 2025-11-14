package com.rabinchuk.orderservice.exception;

import jakarta.persistence.EntityNotFoundException;

public final class ExceptionFactory {

    private ExceptionFactory() {

    }

    public static EntityNotFoundException orderNotFoundException(Long orderId) {
        return new EntityNotFoundException("Order with id " + orderId + " not found");
    }

    public static EntityNotFoundException userNotFoundException(Long userId) {
        return new EntityNotFoundException("User with id " + userId + " not found");
    }

    public static EntityNotFoundException userNotFoundException(String email) {
        return new EntityNotFoundException("User with email " + email + " not found");
    }

    public static EntityNotFoundException itemsNotFoundException() {
        return new EntityNotFoundException("Some items were not found");
    }

    public static EntityNotFoundException usersNotFoundException() {
        return new EntityNotFoundException("Users were not found");
    }

    public static EntityNotFoundException itemNotFoundException(Long itemId) {
        return new EntityNotFoundException("Item with id " + itemId + " not found");
    }
}
