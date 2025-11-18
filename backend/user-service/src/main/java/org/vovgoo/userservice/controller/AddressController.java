package org.vovgoo.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.userservice.dto.address.request.CreateAddressRequest;
import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.exception.dto.ExceptionResponse;
import org.vovgoo.userservice.service.address.AddressService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@Tag(name = "Addresses", description = "User address management")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "Get all addresses", description = "Retrieve all addresses of the authenticated user with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/me/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<AddressResponse>> getAll(@Valid PageParams pageParams) {
        return ResponseEntity.ok(addressService.getAll(pageParams));
    }

    @Operation(summary = "Create new address", description = "Create a new address for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address created successfully",
                    content = @Content(schema = @Schema(implementation = AddressResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/me/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(request));
    }

    @Operation(summary = "Remove address", description = "Delete an address by ID for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/me/remove/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        addressService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Set default address", description = "Set a specific address as the default for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address set as default successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/me/default/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> setDefault(@PathVariable UUID id) {
        addressService.setDefault(id);
        return ResponseEntity.noContent().build();
    }
}
