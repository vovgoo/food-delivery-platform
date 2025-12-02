package org.vovgoo.userservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.common.domain.dto.exception.ExceptionResponse;
import org.vovgoo.userservice.dto.user.request.*;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.dto.user.request.ConfirmChangeEmailRequest;
import org.vovgoo.userservice.dto.user.request.ConfirmChangePhoneRequest;
import org.vovgoo.userservice.service.security.cookie.JwtCookieService;
import org.vovgoo.userservice.service.user.ChangeEmailService;
import org.vovgoo.userservice.service.user.ChangePhoneService;
import org.vovgoo.userservice.service.user.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User profile and update user data")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final ChangeEmailService changeEmailService;
    private final ChangePhoneService changePhoneService;
    private final JwtCookieService jwtCookieService;

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
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change user phone", description = "Initiate phone number change and send verification code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Phone change initiated"),
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
    public ResponseEntity<Void> changePhone(@Valid @RequestBody ChangePhoneRequest request) {
        changePhoneService.changePhone(request);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> confirmChangePhone(@Valid @RequestBody ConfirmChangePhoneRequest request) {
        changePhoneService.confirmChangePhone(request);
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
    public ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailRequest request) {
        changeEmailService.changeEmail(request);
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
    public ResponseEntity<Void> confirmChangeEmail(@Valid @RequestBody ConfirmChangeEmailRequest request) {
        changeEmailService.confirmChangeEmail(request);
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
    public ResponseEntity<Void> reactivateAccount() {
        userService.reactivateAccount();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Logout user", description = "Invalidate refresh token and remove cookie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully logged out"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        jwtCookieService.clearRefreshToken(response);
        return ResponseEntity.noContent().build();
    }
}
