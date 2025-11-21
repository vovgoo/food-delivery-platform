package org.vovgoo.userservice.service.user.impl;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vovgoo.enums.user.UserStatus;
import org.vovgoo.security.utils.CurrentUserUtils;
import org.vovgoo.userservice.dto.user.request.*;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.dto.verification.response.PhoneVerificationResponse;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.Address;
import org.vovgoo.userservice.exception.custom.user.*;
import org.vovgoo.userservice.exception.custom.verification.*;
import org.vovgoo.userservice.mapper.UserMapper;
import org.vovgoo.userservice.repository.AddressRepository;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.verification.email.EmailVerificationService;
import org.vovgoo.userservice.service.verification.email.enums.EmailVerificationType;
import org.vovgoo.userservice.service.verification.phone.PhoneVerificationService;
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private AddressRepository addressRepository;
    private RedisService redisService;
    private PhoneVerificationService phoneVerificationService;
    private EmailVerificationService emailVerificationService;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;
    private UUID currentUserId;
    private MockedStatic<CurrentUserUtils> currentUserUtilsMock;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        redisService = mock(RedisService.class);
        phoneVerificationService = mock(PhoneVerificationService.class);
        emailVerificationService = mock(EmailVerificationService.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService = new UserServiceImpl(userRepository, addressRepository, redisService,
                phoneVerificationService, emailVerificationService, userMapper, passwordEncoder);

        currentUserId = UUID.randomUUID();
        currentUserUtilsMock = mockStatic(CurrentUserUtils.class);
        currentUserUtilsMock.when(CurrentUserUtils::getCurrentUserId).thenReturn(currentUserId);
    }

    @AfterEach
    void tearDown() {
        currentUserUtilsMock.close();
    }

    @Test
    void getProfile_shouldReturnUserProfile() {
        User user = new User();
        user.setId(currentUserId);
        Address address = new Address();
        UserResponse response = new UserResponse(
                currentUserId, "john@example.com", "1234567890", "John Doe",
                null, null, null, null, null, null
        );

        when(userRepository.findByIdWithRoles(currentUserId)).thenReturn(Optional.of(user));
        when(addressRepository.findDefaultByUserId(currentUserId)).thenReturn(Optional.of(address));
        when(userMapper.toResponse(user, address)).thenReturn(response);

        UserResponse result = userService.getProfile();
        assertEquals(response, result);
    }

    @Test
    void getProfile_shouldThrow_whenUserNotFound() {
        when(userRepository.findByIdWithRoles(currentUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getProfile());
    }

    @Test
    void updateUserProfile_shouldUpdateAndReturnUser() {
        UUID userId = UUID.randomUUID();

        currentUserUtilsMock.when(CurrentUserUtils::getCurrentUserId).thenReturn(userId);

        User user = new User();
        user.setId(userId);

        UpdateUserProfileRequest request = new UpdateUserProfileRequest("John Doe", null);

        Address address = new Address();
        UserResponse response = new UserResponse(
                userId,
                "john@example.com",
                "1234567890",
                "John Doe",
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(userRepository.findByIdWithRoles(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(addressRepository.findDefaultByUserId(userId)).thenReturn(Optional.of(address));
        when(userMapper.toResponse(user, address)).thenReturn(response);

        UserResponse result = userService.updateUserProfile(request);

        assertEquals(response, result);
        assertEquals("John Doe", user.getFullName());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserProfile_shouldThrow_whenUserNotFound() {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest("Name", null);
        when(userRepository.findByIdWithRoles(currentUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUserProfile(request));
    }

    @Test
    void changePassword_shouldUpdatePassword() {
        User user = new User();
        user.setPasswordHash("oldHash");
        ChangePasswordRequest request = new ChangePasswordRequest("old", "new");

        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "oldHash")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("newHash");
        when(userRepository.save(user)).thenReturn(user);

        userService.changePassword(request);

        assertEquals("newHash", user.getPasswordHash());
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_shouldThrow_whenOldPasswordMismatch() {
        User user = new User();
        user.setPasswordHash("oldHash");
        ChangePasswordRequest request = new ChangePasswordRequest("wrongOld", "new");

        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOld", "oldHash")).thenReturn(false);

        assertThrows(PasswordMismatchException.class, () -> userService.changePassword(request));
    }

    @Test
    void changePhone_shouldSendOtp() {
        UUID userId = UUID.randomUUID();

        currentUserUtilsMock.when(CurrentUserUtils::getCurrentUserId).thenReturn(userId);

        User user = new User();
        user.setId(userId);

        ChangePhoneRequest request = new ChangePhoneRequest("12345");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(phoneVerificationService.sendOtp("12345", PhoneVerificationType.CHANGE)).thenReturn("token");

        PhoneVerificationResponse result = userService.changePhone(request);

        assertEquals("token", result.token());
        verify(redisService).set(any(), eq(request), eq(userId.toString()), eq("token"));
    }

    @Test
    void changePhone_shouldThrow_whenPhoneExists() {
        User user = new User();
        ChangePhoneRequest request = new ChangePhoneRequest("12345");
        User existing = new User();

        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(userRepository.findByPhone("12345")).thenReturn(Optional.of(existing));

        assertThrows(PhoneAlreadyExistsException.class, () -> userService.changePhone(request));
    }

    @Test
    void confirmChangePhone_shouldUpdatePhone() {
        User user = new User();
        user.setId(currentUserId);
        ChangePhoneRequest changePhoneRequest = new ChangePhoneRequest("12345");
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(redisService.get(any(), eq(ChangePhoneRequest.class), eq(currentUserId.toString()), eq("token"))).thenReturn(Optional.of(changePhoneRequest));
        when(userRepository.save(user)).thenReturn(user);

        userService.confirmChangePhone("token", new ConfirmChangePhoneRequest("12345"));

        assertEquals("12345", user.getPhone());
        verify(redisService).delete(any(), eq(currentUserId.toString()), eq("token"));
    }

    @Test
    void confirmChangePhone_shouldThrow_whenTokenNotFound() {
        User user = new User();
        user.setId(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(redisService.get(any(), eq(ChangePhoneRequest.class), eq(currentUserId.toString()), eq("token"))).thenReturn(Optional.empty());

        assertThrows(ChangePhoneRequestNotFoundException.class, () -> userService.confirmChangePhone("token", new ConfirmChangePhoneRequest("12345")));
    }

    @Test
    void changeEmail_shouldSendVerificationLink() {
        UUID userId = UUID.randomUUID();

        currentUserUtilsMock.when(CurrentUserUtils::getCurrentUserId).thenReturn(userId);

        User user = new User();
        user.setId(userId);

        ChangeEmailRequest request = new ChangeEmailRequest("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(emailVerificationService.sendVerificationLink("test@example.com", EmailVerificationType.CHANGE))
                .thenReturn("token");

        userService.changeEmail(request);

        verify(redisService).set(any(), eq(request), eq(userId.toString()), eq("token"));
    }

    @Test
    void changeEmail_shouldThrow_whenEmailExists() {
        User user = new User();
        ChangeEmailRequest request = new ChangeEmailRequest("test@example.com");
        User existing = new User();

        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existing));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.changeEmail(request));
    }

    @Test
    void confirmChangeEmail_shouldUpdateEmail() {
        User user = new User();
        user.setId(currentUserId);
        ChangeEmailRequest request = new ChangeEmailRequest("new@example.com");
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(redisService.get(any(), eq(ChangeEmailRequest.class), eq(currentUserId.toString()), eq("token"))).thenReturn(Optional.of(request));
        when(userRepository.save(user)).thenReturn(user);

        userService.confirmChangeEmail("token");

        assertEquals("new@example.com", user.getEmail());
        verify(redisService).delete(any(), eq(currentUserId.toString()), eq("token"));
    }

    @Test
    void confirmChangeEmail_shouldThrow_whenTokenNotFound() {
        User user = new User();
        user.setId(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(redisService.get(any(), eq(ChangeEmailRequest.class), eq(currentUserId.toString()), eq("token"))).thenReturn(Optional.empty());

        assertThrows(ChangeEmailRequestNotFoundException.class, () -> userService.confirmChangeEmail("token"));
    }

    @Test
    void deactivateAccount_shouldSetStatusDeactivated() {
        User user = new User();
        user.setId(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deactivateAccount();

        assertEquals(UserStatus.DEACTIVATED, user.getStatus());
        verify(userRepository).save(user);
    }

    @Test
    void reactivateAccount_shouldSetStatusActive() {
        User user = new User();
        user.setId(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.reactivateAccount();

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        verify(userRepository).save(user);
    }

    @Test
    void reactivateAccount_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.reactivateAccount());
    }

}