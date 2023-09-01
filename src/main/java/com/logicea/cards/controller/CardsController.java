package com.logicea.cards.controller;

import com.logicea.cards.enums.CardStatus;
import com.logicea.cards.model.dto.request.CardRequestDto;
import com.logicea.cards.model.dto.request.LoginRequestDto;
import com.logicea.cards.model.dto.response.ApiResponseDto;
import com.logicea.cards.model.dto.response.CardDto;
import com.logicea.cards.model.dto.response.ResponseBodyDto;
import com.logicea.cards.model.dto.response.UserDetailsDto;
import com.logicea.cards.service.CardsService;
import com.logicea.cards.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Cards Controller", description = "Api to manage Cards")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CardsController {

    private final CardsService cardsService;

    @Operation(
            summary = "Add Cards",
            description = "Endpoint for adding more cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    @PostMapping("/1.0/cards")
    public ResponseEntity<ApiResponseDto<CardDto>> addCard(@RequestHeader Map<String, String> headers,
                                                           @RequestBody @Valid CardRequestDto cardRequestDto) {
        return ResponseEntity.ok(cardsService.addCard(headers, cardRequestDto));
    }

    @Operation(
            summary = "Fetch Cards",
            description = "Endpoint for fetching cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    @GetMapping("/1.0/cards")
    public ResponseEntity<ApiResponseDto<ResponseBodyDto<List<CardDto>>>> getCards(@RequestHeader Map<String, String> headers,
                                                                                   @RequestParam(defaultValue = "1") int page,
                                                                                   @RequestParam(defaultValue = "5") int size,
                                                                                   @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(cardsService.getCards(headers, Long.valueOf(headers.get("x-user-id")), page, size, sort));
    }

    @Operation(
            summary = "Get Single Card",
            description = "Endpoint for fetching a single card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    @GetMapping("/1.0/cards/{cardId}")
    public ResponseEntity<ApiResponseDto<CardDto>> getCard(@RequestHeader Map<String, String> headers,
                                                           @PathVariable Long cardId) {
        return ResponseEntity.ok(cardsService.getCard(headers, cardId));
    }

    @Operation(
            summary = "Updated Card",
            description = "Endpoint for updating cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    @PutMapping("/1.0/cards/{cardId}")
    public ResponseEntity<ApiResponseDto<CardDto>> updateCard(@RequestHeader Map<String, String> headers,
                                                              @RequestBody @Valid CardRequestDto cardRequestDto,
                                                              @PathVariable Long cardId) {
        return ResponseEntity.ok(cardsService.updateCard(headers, cardRequestDto, cardId));
    }

    @Operation(
            summary = "Delete Card",
            description = "Endpoint for deleting cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    @DeleteMapping("/1.0/cards/{cardId}")
    public ResponseEntity<ApiResponseDto<Object>> deleteCard(@RequestHeader Map<String, String> headers,
                                                             @PathVariable Long cardId) {
        return ResponseEntity.ok(cardsService.deleteCard(headers, cardId));
    }

    @Operation(
            summary = "Search Cards",
            description = "Endpoint for search for a card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    @GetMapping("/1.0/cards/search")
    public ResponseEntity<ApiResponseDto<ResponseBodyDto<List<CardDto>>>> searchCards(@RequestHeader Map<String, String> headers,
                                                                                      @RequestParam(defaultValue = "1") int page,
                                                                                      @RequestParam(defaultValue = "5") int size,
                                                                                      @RequestParam(defaultValue = "id,asc") String[] sort,
                                                                                      @RequestParam(required = false) String name,
                                                                                      @RequestParam(required = false) CardStatus status,
                                                                                      @RequestParam(required = false) String color,
                                                                                      @RequestParam(required = false) String createdAt) {
        return ResponseEntity.ok(cardsService.searchCards(headers, page, size, sort, name, status, color, createdAt));
    }

    @Operation(
            summary = "Fetch All Cards",
            description = "Endpoint for fetching all cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/1.0/admin/cards")
    public ResponseEntity<ApiResponseDto<ResponseBodyDto<List<CardDto>>>> getAllCards(@RequestHeader Map<String, String> headers,
                                                                                      @RequestParam(defaultValue = "1") int page,
                                                                                      @RequestParam(defaultValue = "5") int size,
                                                                                      @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(cardsService.getCards(headers, null, page, size, sort));
    }

}