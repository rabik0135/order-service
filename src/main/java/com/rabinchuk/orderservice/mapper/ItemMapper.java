package com.rabinchuk.orderservice.mapper;

import com.rabinchuk.orderservice.dto.ItemRequestDto;
import com.rabinchuk.orderservice.dto.ItemResponseDto;
import com.rabinchuk.orderservice.model.Item;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponseDto toDto(Item item);

    @Mapping(target = "id", ignore = true)
    Item toEntity(ItemRequestDto itemRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateItemFromDto(ItemRequestDto itemRequestDto, @MappingTarget Item item);

}
