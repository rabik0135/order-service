package com.rabinchuk.orderservice.service;

import com.rabinchuk.orderservice.dto.ItemRequestDto;
import com.rabinchuk.orderservice.dto.ItemResponseDto;

public interface ItemService extends CRUDService<ItemResponseDto, ItemRequestDto, ItemRequestDto> {

}
