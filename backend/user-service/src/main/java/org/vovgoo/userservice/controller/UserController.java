package org.vovgoo.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.userservice.dto.user.request.UserUpdateRequest;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.service.user.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getProfile() {
        UserResponse profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse updatedProfile = userService.updateCurrentUserProfile(userUpdateRequest);
        return ResponseEntity.ok(updatedProfile);
    }
}
