package org.vovgoo.userservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.userservice.dto.user.request.*;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.dto.verification.response.PhoneVerificationResponse;
import org.vovgoo.userservice.service.user.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Профиль и обновление данных пользователя")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getProfile() {
        UserResponse response = userService.getProfile();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest) {
        UserResponse response = userService.updateUserProfile(updateUserProfileRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/phone")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PhoneVerificationResponse> changePhone(@Valid @RequestBody ChangePhoneRequest changePhoneRequest) {
        PhoneVerificationResponse phoneVerificationResponse = userService.changePhone(changePhoneRequest);
        return ResponseEntity.ok(phoneVerificationResponse);
    }

    @PutMapping("/me/phone/confirm")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> confirmChangePhone(@Valid @RequestParam("token") String token, @Valid @RequestBody ConfirmChangePhoneRequest confirmChangePhoneRequest) {
        userService.confirmChangePhone(token, confirmChangePhoneRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailRequest changeEmailRequest) {
        userService.changeEmail(changeEmailRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/email/confirm")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> confirmChangeEmail(@Valid @RequestParam("token") String token) {
        userService.confirmChangeEmail(token);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/deactivate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deactivateAccount() {
        userService.deactivateAccount();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/reactivate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> reactivateAccount() {
        userService.reactivateAccount();
        return ResponseEntity.noContent().build();
    }
}
