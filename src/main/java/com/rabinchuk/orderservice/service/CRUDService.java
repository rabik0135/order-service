package com.rabinchuk.orderservice.service;

import java.util.List;

public interface CRUDService<T, C> {

    List<T> getAll();

    T getById(Long id);

    List<T> getByIds(List<Long> ids);

    T create(C c);

    void deleteById(Long id);

}
