package com.rabinchuk.orderservice.service.impl;

import com.rabinchuk.orderservice.dto.ItemRequestDto;
import com.rabinchuk.orderservice.dto.ItemResponseDto;
import com.rabinchuk.orderservice.exception.ExceptionFactory;
import com.rabinchuk.orderservice.mapper.ItemMapper;
import com.rabinchuk.orderservice.model.Item;
import com.rabinchuk.orderservice.repository.ItemRepository;
import com.rabinchuk.orderservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAll() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> ExceptionFactory.itemNotFoundException(id)
        );
        return itemMapper.toDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getByIds(List<Long> ids) {
        return itemRepository.findAllById(ids).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemResponseDto create(ItemRequestDto itemRequestDto) {
        Item item = itemMapper.toEntity(itemRequestDto);
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem);
    }

    @Override
    @Transactional
    public ItemResponseDto updateById(Long id, ItemRequestDto itemRequestDto) {
        Item existingItem = itemRepository.findById(id).orElseThrow(
                () -> ExceptionFactory.itemNotFoundException(id)
        );
        itemMapper.updateItemFromDto(itemRequestDto, existingItem);
        itemRepository.save(existingItem);
        return itemMapper.toDto(existingItem);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!itemRepository.existsById(id)) {
            throw ExceptionFactory.itemNotFoundException(id);
        }
        itemRepository.deleteById(id);
    }

}
