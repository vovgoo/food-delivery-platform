package org.vovgoo.userservice.service.security.auth.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.dto.security.auth.request.*;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;
import org.vovgoo.userservice.dto.security.jwt.response.JwtResponse;
import org.vovgoo.userservice.dto.verification.response.PhoneVerificationResponse;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.enums.RoleType;
import org.vovgoo.userservice.exception.custom.role.RoleNotFoundException;
import org.vovgoo.userservice.exception.custom.security.InvalidRefreshTokenException;
import org.vovgoo.userservice.exception.custom.user.PhoneAlreadyExistsException;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.SignUpRequestNotFoundException;
import org.vovgoo.userservice.repository.RoleRepository;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.security.jwt.JwtTokenProvider;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;
import org.vovgoo.userservice.service.verification.phone.PhoneVerificationService;
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    private UserRepository userRepository;
    private RedisService redisService;
    private RoleRepository roleRepository;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;
    private PhoneVerificationService phoneVerificationService;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        redisService = mock(RedisService.class);
        roleRepository = mock(RoleRepository.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        passwordEncoder = mock(PasswordEncoder.class);
        phoneVerificationService = mock(PhoneVerificationService.class);

        authService = new AuthServiceImpl(
                userRepository,
                redisService,
                roleRepository,
                jwtTokenProvider,
                passwordEncoder,
                phoneVerificationService
        );
    }

    @Test
    void signIn_shouldReturnJwtPair_whenCredentialsValid() {
        SignInRequest request = new SignInRequest("123456", "pass");
        User user = User.builder().passwordHash("hashed").build();

        when(userRepository.findByPhoneWithRoles("123456")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);
        when(jwtTokenProvider.generateToken(JwtTokenType.ACCESS, user)).thenReturn("access");
        when(jwtTokenProvider.generateToken(JwtTokenType.REFRESH, user)).thenReturn("refresh");

        JwtPair result = authService.signIn(request);

        assertEquals("access", result.accessToken());
        assertEquals("refresh", result.refreshToken());
    }

    @Test
    void signIn_shouldThrow_whenUserNotFound() {
        SignInRequest request = new SignInRequest("123", "pass");
        when(userRepository.findByPhoneWithRoles("123")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authService.signIn(request));
    }

    @Test
    void signIn_shouldThrow_whenPasswordInvalid() {
        SignInRequest request = new SignInRequest("123", "pass");
        User user = User.builder().passwordHash("hashed").build();
        when(userRepository.findByPhoneWithRoles("123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "hashed")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.signIn(request));
    }

    @Test
    void signUp_shouldReturnPhoneVerificationResponse_whenNewPhone() {
        SignUpRequest request = new SignUpRequest("123", "Name", LocalDate.now(), null);
        when(userRepository.findByPhone("123")).thenReturn(Optional.empty());
        when(phoneVerificationService.sendOtp("123", PhoneVerificationType.SIGN_UP)).thenReturn("token123");

        PhoneVerificationResponse result = authService.signUp(request);

        assertEquals("token123", result.token());
        verify(redisService).set(RedisKey.SIGNUP_REQUEST, request, "token123");
    }

    @Test
    void signUp_shouldThrow_whenPhoneAlreadyExists() {
        SignUpRequest request = new SignUpRequest("123", "Name", LocalDate.now(), null);
        when(userRepository.findByPhone("123")).thenReturn(Optional.of(new User()));

        assertThrows(PhoneAlreadyExistsException.class, () -> authService.signUp(request));
    }

    @Test
    void resendSignUpOtpCode_shouldSendOtp_whenRequestExists() {
        SignUpRequest request = new SignUpRequest("123", "Name", LocalDate.now(), null);
        when(redisService.get(RedisKey.SIGNUP_REQUEST, SignUpRequest.class, "token")).thenReturn(Optional.of(request));

        authService.resendSignUpOtpCode("token");

        verify(phoneVerificationService).sendOtp("123", PhoneVerificationType.SIGN_UP);
    }

    @Test
    void resendSignUpOtpCode_shouldThrow_whenRequestNotFound() {
        when(redisService.get(RedisKey.SIGNUP_REQUEST, SignUpRequest.class, "token")).thenReturn(Optional.empty());

        assertThrows(SignUpRequestNotFoundException.class, () -> authService.resendSignUpOtpCode("token"));
    }

    @Test
    void confirmSignUp_shouldReturnJwtPair_whenSuccessful() {
        SignUpRequest request = new SignUpRequest("123", "Name", LocalDate.now(), null);
        Role role = new Role();
        User savedUser = User.builder().phone("123").roles(Set.of(role)).build();

        when(redisService.get(RedisKey.SIGNUP_REQUEST, SignUpRequest.class, "token")).thenReturn(Optional.of(request));
        doNothing().when(phoneVerificationService).validateOtp("token", "code123", PhoneVerificationType.SIGN_UP);
        when(roleRepository.findByName(RoleType.USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("pwd")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenProvider.generateToken(JwtTokenType.ACCESS, savedUser)).thenReturn("access");
        when(jwtTokenProvider.generateToken(JwtTokenType.REFRESH, savedUser)).thenReturn("refresh");

        JwtPair result = authService.confirmSignUp("token", new ConfirmSignUpRequest("code123"));

        assertEquals("access", result.accessToken());
        assertEquals("refresh", result.refreshToken());
        verify(redisService).delete(RedisKey.SIGNUP_REQUEST, "token");
    }

    @Test
    void confirmSignUp_shouldThrow_whenSignUpRequestNotFound() {
        when(redisService.get(RedisKey.SIGNUP_REQUEST, SignUpRequest.class, "token")).thenReturn(Optional.empty());

        assertThrows(SignUpRequestNotFoundException.class, () -> authService.confirmSignUp("token", new ConfirmSignUpRequest("code")));
    }

    @Test
    void confirmSignUp_shouldThrow_whenRoleNotFound() {
        SignUpRequest request = new SignUpRequest("123", "Name", LocalDate.now(), null);
        when(redisService.get(RedisKey.SIGNUP_REQUEST, SignUpRequest.class, "token")).thenReturn(Optional.of(request));
        doNothing().when(phoneVerificationService).validateOtp(anyString(), anyString(), any());

        when(roleRepository.findByName(RoleType.USER)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> authService.confirmSignUp("token", new ConfirmSignUpRequest("code")));
    }

    @Test
    void refreshAccessToken_shouldReturnNewAccessToken_whenTokenValid() {
        User user = User.builder().phone("123").build();
        when(jwtTokenProvider.validateToken(JwtTokenType.REFRESH, "refresh")).thenReturn(true);
        when(jwtTokenProvider.extractUserId(JwtTokenType.REFRESH, "refresh")).thenReturn(UUID.randomUUID());
        when(userRepository.findByIdWithRoles(any())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(JwtTokenType.ACCESS, user)).thenReturn("newAccess");

        JwtResponse result = authService.refreshAccessToken("refresh");

        assertEquals("newAccess", result.accessToken());
    }

    @Test
    void refreshAccessToken_shouldThrow_whenTokenInvalid() {
        when(jwtTokenProvider.validateToken(JwtTokenType.REFRESH, "refresh")).thenReturn(false);

        assertThrows(InvalidRefreshTokenException.class, () -> authService.refreshAccessToken("refresh"));
    }

    @Test
    void refreshAccessToken_shouldThrow_whenUserNotFound() {
        when(jwtTokenProvider.validateToken(JwtTokenType.REFRESH, "refresh")).thenReturn(true);
        when(jwtTokenProvider.extractUserId(JwtTokenType.REFRESH, "refresh")).thenReturn(UUID.randomUUID());
        when(userRepository.findByIdWithRoles(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.refreshAccessToken("refresh"));
    }
}
