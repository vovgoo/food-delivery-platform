package org.vovgoo.userservice.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vovgoo.enums.user.UserStatus;
import org.vovgoo.userservice.service.user.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@Hidden
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/{userId}/status")
    public ResponseEntity<UserStatus> getUserStatus(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserStatus(userId));
    }
}
