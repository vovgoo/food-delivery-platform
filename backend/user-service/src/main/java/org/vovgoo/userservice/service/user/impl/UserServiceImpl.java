package org.vovgoo.userservice.service.user.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.userservice.dto.address.request.AddressUpsertRequest;
import org.vovgoo.userservice.dto.user.request.UserUpdateRequest;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.entity.Address;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.EmailAlreadyExistsException;
import org.vovgoo.userservice.mapper.UserMapper;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getCurrentUserProfile() {
        User currentUser = getAuthenticatedUser();
        return userMapper.toResponse(currentUser);
    }

    @Override
    @Transactional
    public UserResponse updateCurrentUserProfile(UserUpdateRequest userUpdateRequest) {
        User currentUser = getAuthenticatedUser();

        if (!currentUser.getEmail().equals(userUpdateRequest.email()) && userRepository.existsByEmail(userUpdateRequest.email())) {
            throw new EmailAlreadyExistsException(userUpdateRequest.email());
        }

        currentUser.setEmail(userUpdateRequest.email());
        currentUser.setFullName(userUpdateRequest.fullName());

        List<AddressUpsertRequest> incomingAddresses = userUpdateRequest.addresses();
        List<Address> currentAddresses = currentUser.getAddresses();

        Map<Long, Address> existingAddressMap = currentAddresses.stream()
                .filter(a -> a.getId() != null)
                .collect(Collectors.toMap(Address::getId, a -> a));

        List<Address> updatedAddresses = new ArrayList<>();

        for (AddressUpsertRequest addrReq : incomingAddresses) {
            Address address;

            if (addrReq.id() != null && existingAddressMap.containsKey(addrReq.id())) {
                address = existingAddressMap.get(addrReq.id());
                address.setStreet(addrReq.street());
                address.setCity(addrReq.city());
                address.setZip(addrReq.zip());
                address.setState(addrReq.state());
                address.setCountry(addrReq.country());
            } else {
                address = Address.builder()
                        .user(currentUser)
                        .street(addrReq.street())
                        .city(addrReq.city())
                        .zip(addrReq.zip())
                        .state(addrReq.state())
                        .country(addrReq.country())
                        .build();
            }

            updatedAddresses.add(address);
        }

        currentAddresses.removeIf(addr -> !updatedAddresses.contains(addr));

        for (Address addr : updatedAddresses) {
            if (!currentAddresses.contains(addr)) {
                currentAddresses.add(addr);
            }
        }

        currentUser = userRepository.save(currentUser);

        return userMapper.toResponse(currentUser);
    }

    private User getAuthenticatedUser() {
<<<<<<< Updated upstream
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .flatMap(user -> userRepository.findByIdWithRolesAndAddresses(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
=======
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new UserNotFoundException();
        }

        Long userId;
        try {
            userId = Long.valueOf(auth.getPrincipal().toString());
        } catch (NumberFormatException e) {
            throw new UserNotFoundException();
        }

        return userRepository.findByIdWithRolesAndAddresses(userId)
                .orElseThrow(UserNotFoundException::new);
>>>>>>> Stashed changes
    }
}
