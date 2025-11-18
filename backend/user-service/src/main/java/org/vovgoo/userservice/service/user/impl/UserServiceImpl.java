package org.vovgoo.userservice.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.dto.verification.response.PhoneVerificationResponse;
import org.vovgoo.userservice.dto.user.request.*;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.entity.Address;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.enums.UserStatus;
import org.vovgoo.userservice.exception.custom.user.EmailAlreadyExistsException;
import org.vovgoo.userservice.exception.custom.user.PasswordMismatchException;
import org.vovgoo.userservice.exception.custom.user.PhoneAlreadyExistsException;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.ChangeEmailRequestNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.ChangePhoneRequestNotFoundException;
import org.vovgoo.userservice.mapper.UserMapper;
import org.vovgoo.userservice.repository.AddressRepository;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.user.UserService;
import org.vovgoo.userservice.service.user.aspects.checkstatus.CheckUserStatus;
import org.vovgoo.userservice.service.verification.email.EmailVerificationService;
import org.vovgoo.userservice.service.verification.email.enums.EmailVerificationType;
import org.vovgoo.userservice.service.verification.phone.PhoneVerificationService;
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;
import org.vovgoo.userservice.utils.CurrentUserUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RedisService redisService;
    private final PhoneVerificationService phoneVerificationService;
    private final EmailVerificationService emailVerificationService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @CheckUserStatus
    public UserResponse getProfile() {
        User user = userRepository.findByIdWithRoles(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        Address address = addressRepository.findDefaultByUserId(user.getId())
                .orElse(null);

        return userMapper.toResponse(user, address);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public UserResponse updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest) {
        User user = userRepository.findByIdWithRoles(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        user.setFullName(updateUserProfileRequest.fullName());
        user.setBirthDate(updateUserProfileRequest.birthDate());

        user = userRepository.save(user);

        Address address = addressRepository.findDefaultByUserId(user.getId())
                .orElse(null);

        return userMapper.toResponse(user, address);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPasswordHash())) {
            throw new PasswordMismatchException();
        }

        user.setPasswordHash(passwordEncoder.encode(changePasswordRequest.newPassword()));

        userRepository.save(user);
    }

    @Override
    @CheckUserStatus
    public PhoneVerificationResponse changePhone(ChangePhoneRequest changePhoneRequest) {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        userRepository.findByPhone(changePhoneRequest.phone())
                .ifPresent( u -> {throw new PhoneAlreadyExistsException(changePhoneRequest.phone()); });

        String token = phoneVerificationService.sendOtp(changePhoneRequest.phone(), PhoneVerificationType.CHANGE);

        redisService.set(RedisKey.PHONE_CHANGE_REQUEST, changePhoneRequest, user.getId().toString(), token);

        return PhoneVerificationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void confirmChangePhone(String token, ConfirmChangePhoneRequest confirmChangePhoneRequest) {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        String code = confirmChangePhoneRequest.code();

        ChangePhoneRequest changePhoneRequest = redisService.get(RedisKey.PHONE_CHANGE_REQUEST, ChangePhoneRequest.class, user.getId().toString(), token)
                .orElseThrow(ChangePhoneRequestNotFoundException::new);

        phoneVerificationService.validateOtp(token, code, PhoneVerificationType.CHANGE);

        userRepository.findByPhone(changePhoneRequest.phone())
                .ifPresent( u -> {throw new PhoneAlreadyExistsException(changePhoneRequest.phone()); });

        user.setPhone(changePhoneRequest.phone());

        user = userRepository.save(user);

        redisService.delete(RedisKey.PHONE_CHANGE_REQUEST, user.getId().toString(), token);
    }

    @Override
    @CheckUserStatus
    public void changeEmail(ChangeEmailRequest changeEmailRequest) {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        userRepository.findByEmail(changeEmailRequest.email())
                .ifPresent( u -> {throw new EmailAlreadyExistsException(changeEmailRequest.email()); });

        String token = emailVerificationService.sendVerificationLink(changeEmailRequest.email(), EmailVerificationType.CHANGE);

        redisService.set(RedisKey.EMAIL_CHANGE_REQUEST, changeEmailRequest, user.getId().toString(), token);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void confirmChangeEmail(String token) {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        ChangeEmailRequest changeEmailRequest = redisService.get(RedisKey.EMAIL_CHANGE_REQUEST, ChangeEmailRequest.class, user.getId().toString(), token)
                .orElseThrow(ChangeEmailRequestNotFoundException::new);

        emailVerificationService.validateVerificationLink(token, EmailVerificationType.CHANGE);

        userRepository.findByEmail(changeEmailRequest.email())
                .ifPresent( u -> {throw new EmailAlreadyExistsException(changeEmailRequest.email()); });

        user.setEmail(changeEmailRequest.email());

        user = userRepository.save(user);

        redisService.delete(RedisKey.EMAIL_CHANGE_REQUEST, user.getId().toString(), token);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void deactivateAccount() {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        user.setStatus(UserStatus.DEACTIVATED);

        userRepository.save(user);
    }

    @Override
    @Transactional
    @CheckUserStatus(forbidden = {UserStatus.BLOCKED, UserStatus.ACTIVE})
    public void reactivateAccount() {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }
}
