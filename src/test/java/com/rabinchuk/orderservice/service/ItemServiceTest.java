package com.rabinchuk.orderservice.service;

import com.rabinchuk.orderservice.dto.ItemRequestDto;
import com.rabinchuk.orderservice.dto.ItemResponseDto;
import com.rabinchuk.orderservice.mapper.ItemMapper;
import com.rabinchuk.orderservice.model.Item;
import com.rabinchuk.orderservice.repository.ItemRepository;
import com.rabinchuk.orderservice.service.impl.ItemServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private ItemRequestDto itemRequestDto;
    private ItemResponseDto itemResponseDto;

    @BeforeEach
    public void setUp() {
        item = Item.builder()
                .id(1L)
                .name("Test Item")
                .price(BigDecimal.valueOf(100.00))
                .build();

        itemResponseDto = ItemResponseDto.builder()
                .id(1L)
                .name("Test Item")
                .price(BigDecimal.valueOf(100.00))
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .name("Test Item")
                .price(BigDecimal.valueOf(100.00))
                .build();
    }

    @Test
    @DisplayName("Get all items")
    public void testGetAllItems() {
        Item item2 = Item.builder()
                .id(2L)
                .name("Test Item 2")
                .price(BigDecimal.valueOf(100.00))
                .build();
        ItemResponseDto itemResponseDto2 = ItemResponseDto.builder()
                .id(2L)
                .name("Test Item 2")
                .price(BigDecimal.valueOf(100.00))
                .build();
        List<Item> items = List.of(item, item2);

        when(itemRepository.findAll()).thenReturn(items);
        when(itemMapper.toDto(item)).thenReturn(itemResponseDto);
        when(itemMapper.toDto(item2)).thenReturn(itemResponseDto2);
        List<ItemResponseDto> expected = List.of(itemResponseDto, itemResponseDto2);

        List<ItemResponseDto> result = itemService.getAll();

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result).isEqualTo(expected);
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get item by id")
    public void testGetItemById() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toDto(item)).thenReturn(itemResponseDto);

        ItemResponseDto foundItem = itemService.getById(1L);

        assertThat(foundItem).isNotNull();
        assertThat(foundItem).isEqualTo(itemResponseDto);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get item by ids")
    public void testGetItemByIds() {
        Item item2 = Item.builder()
                .id(2L)
                .name("Test Item 2")
                .price(BigDecimal.valueOf(100.00))
                .build();
        ItemResponseDto itemResponseDto2 = ItemResponseDto.builder()
                .id(2L)
                .name("Test Item 2")
                .price(BigDecimal.valueOf(100.00))
                .build();
        List<Item> items = List.of(item, item2);

        when(itemRepository.findAllById(List.of(1L, 2L))).thenReturn(items);
        when(itemMapper.toDto(item)).thenReturn(itemResponseDto);
        when(itemMapper.toDto(item2)).thenReturn(itemResponseDto2);
        List<ItemResponseDto> expected = List.of(itemResponseDto, itemResponseDto2);

        List<ItemResponseDto> result = itemService.getByIds(List.of(1L, 2L));

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result).isEqualTo(expected);
        verify(itemRepository, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Create item")
    public void testCreateItem() {
        when(itemMapper.toEntity(itemRequestDto)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(itemResponseDto);

        ItemResponseDto createdItem = itemService.create(itemRequestDto);

        assertThat(createdItem).isNotNull();
        assertThat(createdItem).isEqualTo(itemResponseDto);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    @DisplayName("Update item by id")
    public void testUpdateById() {
        ItemRequestDto updateRequestDto = ItemRequestDto.builder()
                .name("Updated Item")
                .price(BigDecimal.valueOf(200.00))
                .build();

        ItemResponseDto updateResponseDto = ItemResponseDto.builder()
                .id(1L)
                .name("Updated Item")
                .price(BigDecimal.valueOf(200.00))
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(updateResponseDto);
        doNothing().when(itemMapper).updateItemFromDto(updateRequestDto, item);

        ItemResponseDto updatedItem = itemService.updateById(1L, updateRequestDto);

        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem).isEqualTo(updateResponseDto);
        verify(itemRepository, times(1)).findById(1L);
        verify(itemMapper, times(1)).updateItemFromDto(updateRequestDto, item);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    @DisplayName("Delete item by id")
    public void testDeleteById() {
        when(itemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(itemRepository).deleteById(1L);

        itemService.deleteById(1L);

        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Get all items should return empty list")
    public void testGetAllItemsShouldReturnEmptyList() {
        when(itemRepository.findAll()).thenReturn(List.of());

        List<ItemResponseDto> result = itemService.getAll();

        assertThat(result).isNotNull().isEmpty();
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get item by invalid id should throw exception")
    public void testGetItemByInvalidIdShouldThrowException() {
        when(itemRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.getById(100L));
    }

    @Test
    @DisplayName("Update item by id should throw exception")
    public void UpdateByInvalidIdShouldThrowException() {
        when(itemRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.updateById(100L, itemRequestDto));
    }

    @Test
    @DisplayName("Delete item by id should throw exception")
    public void DeleteByInvalidIdShouldThrowException() {
        when(itemRepository.existsById(100L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> itemService.deleteById(100L));
    }

}
