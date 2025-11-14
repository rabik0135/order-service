package com.rabinchuk.orderservice.controller;

import com.rabinchuk.orderservice.controller.api.ItemApi;
import com.rabinchuk.orderservice.dto.ItemRequestDto;
import com.rabinchuk.orderservice.dto.ItemResponseDto;
import com.rabinchuk.orderservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/items")
@RequiredArgsConstructor
public class ItemController implements ItemApi {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAll() {
        List<ItemResponseDto> itemResponseDtos = itemService.getAll();
        return ResponseEntity.ok(itemResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getById(@PathVariable Long id) {
        ItemResponseDto itemResponseDto = itemService.getById(id);
        return ResponseEntity.ok(itemResponseDto);
    }

    @GetMapping(value = "/getByIds", params = "ids")
    public ResponseEntity<List<ItemResponseDto>> getByIds(@RequestParam List<Long> ids) {
        List<ItemResponseDto> itemResponseDtos = itemService.getByIds(ids);
        return ResponseEntity.ok(itemResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ItemResponseDto> create(@Valid @RequestBody ItemRequestDto itemRequestDto) {
        ItemResponseDto itemResponseDto = itemService.create(itemRequestDto);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateById(@PathVariable Long id, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        ItemResponseDto itemResponseDto = itemService.updateById(id, itemRequestDto);
        return ResponseEntity.ok(itemResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        itemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
