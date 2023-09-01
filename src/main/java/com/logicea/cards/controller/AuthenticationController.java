package com.logicea.cards.controller;

import com.logicea.cards.model.dto.request.LoginRequestDto;
import com.logicea.cards.model.dto.response.ApiResponseDto;
import com.logicea.cards.model.dto.response.UserDetailsDto;
import com.logicea.cards.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller", description = "Api to authenticate users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final LoginService loginService;

    @Operation(
            summary = "Login",
            description = "Allows user to login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PostMapping("/1.0/login")
    public ResponseEntity<ApiResponseDto<UserDetailsDto>> authenticateUser(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(loginService.authenticateUser(loginRequestDto));
    }

}