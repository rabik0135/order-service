package com.rabinchuk.orderservice.controller.api;

import com.rabinchuk.orderservice.dto.CreateOrderRequestDto;
import com.rabinchuk.orderservice.dto.OrderWithUserResponseDto;
import com.rabinchuk.orderservice.dto.UpdateOrderRequestDto;
import com.rabinchuk.orderservice.model.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Order Management", description = "API for managing customer orders")
public interface OrderApi {

    @Operation(summary = "Get all orders", description = "Returns a list of all existing orders.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderWithUserResponseDto.class)))
    ResponseEntity<List<OrderWithUserResponseDto>> getAll();

    @Operation(summary = "Get order by ID", description = "Returns a single order by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderWithUserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    ResponseEntity<OrderWithUserResponseDto> getById(
            @Parameter(description = "ID of the order to retrieve", required = true) @PathVariable Long id);

    @Operation(summary = "Get orders by a list of IDs", description = "Returns a list of orders matching the provided IDs.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderWithUserResponseDto.class)))
    ResponseEntity<List<OrderWithUserResponseDto>> getByIds(
            @Parameter(description = "List of order IDs to retrieve", required = true) @RequestParam List<Long> ids);

    @Operation(summary = "Create a new order", description = "Creates a new order and returns the created entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderWithUserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<OrderWithUserResponseDto> create(
            @Parameter(description = "Order creation data", required = true) @RequestBody CreateOrderRequestDto createOrderRequestDto);

    @Operation(summary = "Update an existing order", description = "Updates an order's details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderWithUserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<OrderWithUserResponseDto> updateById(
            @Parameter(description = "ID of the order to update", required = true) @PathVariable Long id,
            @Parameter(description = "Data to update the order with", required = true) @RequestBody UpdateOrderRequestDto updateOrderRequestDto);

    @Operation(summary = "Delete an order by ID", description = "Deletes an order by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    ResponseEntity<Void> deleteById(
            @Parameter(description = "ID of the order to delete", required = true) @PathVariable Long id);

    @Operation(summary = "Get orders by statuses", description = "Returns a list of orders filtered by one or more statuses.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderWithUserResponseDto.class)))
    ResponseEntity<List<OrderWithUserResponseDto>> getByStatuses(
            @Parameter(description = "List of statuses to filter by", required = true) @RequestParam List<OrderStatus> statuses);

    @Operation(summary = "Get orders by user ID", description = "Returns all orders placed by a specific user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderWithUserResponseDto.class)))
    ResponseEntity<List<OrderWithUserResponseDto>> getByUserId(
            @Parameter(description = "ID of the user", required = true) @PathVariable Long userId);

    @Operation(summary = "Update order status", description = "Updates the status of a specific order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderWithUserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    ResponseEntity<OrderWithUserResponseDto> updateOrderStatus(
            @Parameter(description = "ID of the order", required = true) @PathVariable Long orderId,
            @Parameter(description = "The new status for the order", required = true) @RequestParam OrderStatus orderStatus);
}