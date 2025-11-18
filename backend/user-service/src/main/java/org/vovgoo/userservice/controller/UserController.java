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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.userservice.dto.user.request.*;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.dto.verification.response.PhoneVerificationResponse;
import org.vovgoo.userservice.exception.dto.ExceptionResponse;
import org.vovgoo.userservice.service.user.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User profile and update user data")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user profile", description = "Retrieve the current authenticated user's profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @Operation(summary = "Update user profile", description = "Update full name and birth date of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
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
    @PutMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        return ResponseEntity.ok(userService.updateUserProfile(request));
    }

    @Operation(summary = "Change user password", description = "Change password of the current authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Old password mismatch or invalid input",
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
    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change user phone", description = "Initiate phone number change and send verification code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone change initiated",
                    content = @Content(schema = @Schema(implementation = PhoneVerificationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Phone already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "429", description = "OTP attempts exceeded",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/me/phone")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PhoneVerificationResponse> changePhone(@Valid @RequestBody ChangePhoneRequest request) {
        return ResponseEntity.ok(userService.changePhone(request));
    }

    @Operation(summary = "Confirm phone change", description = "Confirm new phone with verification token and code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Phone change confirmed"),
            @ApiResponse(responseCode = "400", description = "Invalid input or OTP code",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Change phone request not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Phone already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "429", description = "OTP attempts exceeded",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/me/phone/confirm")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> confirmChangePhone(@Valid @RequestParam("token") String token, @Valid @RequestBody ConfirmChangePhoneRequest request) {
        userService.confirmChangePhone(token, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change user email", description = "Initiate email change and send verification link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email change initiated"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/me/email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailRequest request) {
        userService.changeEmail(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Confirm email change", description = "Confirm new email using verification token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email change confirmed"),
            @ApiResponse(responseCode = "400", description = "Invalid token",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Change email request not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/me/email/confirm")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> confirmChangeEmail(@Valid @RequestParam("token") String token) {
        userService.confirmChangeEmail(token);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deactivate account", description = "Deactivate the current user's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deactivated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or deactivated",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/me/deactivate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deactivateAccount() {
        userService.deactivateAccount();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reactivate account", description = "Reactivate a previously deactivated user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account reactivated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or active",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/me/reactivate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> reactivateAccount() {
        userService.reactivateAccount();
        return ResponseEntity.noContent().build();
    }
}
