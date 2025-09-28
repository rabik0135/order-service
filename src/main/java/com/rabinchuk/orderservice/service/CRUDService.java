package com.rabinchuk.orderservice.service;

import java.util.List;

public interface CRUDService<T, C, U> {

    List<T> getAll();

    T getById(Long id);

    List<T> getByIds(List<Long> ids);

    T create(C c);

    T updateById(Long id, U u);

    void deleteById(Long id);

}
