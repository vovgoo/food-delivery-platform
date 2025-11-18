package org.vovgoo.userservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.userservice.dto.address.request.CreateAddressRequest;
import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.dto.common.PageParams;
import org.vovgoo.userservice.dto.common.PageResponse;
import org.vovgoo.userservice.service.address.AddressService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/me/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<AddressResponse>> getAll(@Valid PageParams pageParams) {
        PageResponse<AddressResponse> response = addressService.getAll(pageParams);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/me/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody CreateAddressRequest createAddressRequest) {
        AddressResponse response = addressService.create(createAddressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/me/remove/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        addressService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/default/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> setDefault(@PathVariable UUID id) {
        addressService.setDefault(id);
        return ResponseEntity.noContent().build();
    }

}
