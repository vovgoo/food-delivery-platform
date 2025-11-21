package org.vovgoo.userservice.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vovgoo.dto.address.AddressShortResponse;
import org.vovgoo.userservice.service.address.AddressService;

import java.util.UUID;

@RestController
@RequestMapping("/internal/addresses")
@RequiredArgsConstructor
@Hidden
public class InternalAddressController {

    private final AddressService addressService;

    @GetMapping("/{userId}/{addressId}")
    public ResponseEntity<AddressShortResponse> getUserAddress(@PathVariable("userId") UUID userId, @PathVariable("addressId") UUID addressId) {
        return ResponseEntity.ok(addressService.getUserAddress(userId, addressId));
    }
}
