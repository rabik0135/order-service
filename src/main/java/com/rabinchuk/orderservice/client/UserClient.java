package com.rabinchuk.orderservice.client;

import com.rabinchuk.orderservice.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "http://user-service:8080")
public interface UserClient {

    @GetMapping("/api/users/getById/{id}")
    UserResponseDto getUserById(@PathVariable Long id);

    @GetMapping("/api/users/getByIds")
    List<UserResponseDto> getUsersByIds(@RequestParam List<Long> ids);

    @GetMapping("/api/users/getByEmail/{email}")
    UserResponseDto getUserByEmail(@PathVariable String email);

}
