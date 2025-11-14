package com.rabinchuk.orderservice.controller.api;

import com.rabinchuk.orderservice.dto.ItemRequestDto;
import com.rabinchuk.orderservice.dto.ItemResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Item API", description = "API for managing catalog items")
@RequestMapping("/api/items")
public interface ItemApi {

    @Operation(summary = "Create a new item", description = "Adds a new item to the catalog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    ResponseEntity<ItemResponseDto> create(@Valid @RequestBody ItemRequestDto requestDto);

    @Operation(summary = "Get an item by ID", description = "Retrieves a single item by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<ItemResponseDto> getById(
            @Parameter(description = "ID of the item to retrieve", required = true) @PathVariable Long id
    );

    @Operation(summary = "Get all items", description = "Retrieves a list of all items in the catalog.")
    @GetMapping
    ResponseEntity<List<ItemResponseDto>> getAll();

    @Operation(summary = "Get items by a list of IDs", description = "Retrieves multiple items matching the given IDs.")
    @GetMapping(params = "ids")
    ResponseEntity<List<ItemResponseDto>> getByIds(
            @Parameter(description = "A list of item IDs to retrieve", required = true) @RequestParam List<Long> ids
    );

    @Operation(summary = "Update an existing item", description = "Updates the details of an existing item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PutMapping("/{id}")
    ResponseEntity<ItemResponseDto> updateById(
            @Parameter(description = "ID of the item to update", required = true) @PathVariable Long id,
            @Valid @RequestBody ItemRequestDto requestDto
    );

    @Operation(summary = "Delete an item by ID", description = "Deletes an item from the catalog by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(
            @Parameter(description = "ID of the item to delete", required = true) @PathVariable Long id
    );

}
