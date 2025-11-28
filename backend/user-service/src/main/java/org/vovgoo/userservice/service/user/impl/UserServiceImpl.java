package org.vovgoo.userservice.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.common.domain.user.enums.UserStatus;
import org.vovgoo.common.security.exception.custom.UserDeactivatedException;
import org.vovgoo.common.security.utils.CurrentUserUtils;
import org.vovgoo.userservice.exception.custom.user.UserActiveException;
import org.vovgoo.userservice.dto.user.request.*;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.entity.Address;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.custom.user.PasswordAlreadyUsedException;
import org.vovgoo.userservice.exception.custom.user.PasswordMismatchException;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.mapper.UserMapper;
import org.vovgoo.userservice.repository.AddressRepository;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.user.UserService;
import org.vovgoo.common.domain.user.dto.UserInternalResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getProfile() {
        User user = userRepository.findByIdWithRoles(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        Address address = addressRepository.findDefaultByUserId(user.getId())
                .orElse(null);

        return userMapper.toResponse(user, address);
    }

    @Override
    @Transactional
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
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPasswordHash())) {
            throw new PasswordMismatchException();
        }

        if(passwordEncoder.matches(changePasswordRequest.newPassword(), user.getPasswordHash())) {
            throw new PasswordAlreadyUsedException();
        }

        user.setPasswordHash(passwordEncoder.encode(changePasswordRequest.newPassword()));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateAccount() {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        if(user.getStatus().equals(UserStatus.DEACTIVATED)) {
            throw new UserDeactivatedException();
        }

        user.setStatus(UserStatus.DEACTIVATED);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void reactivateAccount() {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId())
                .orElseThrow(UserNotFoundException::new);

        if(user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new UserActiveException();
        }

        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }

    @Override
    public UserInternalResponse getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toInternalResponse(user);
    }
}
