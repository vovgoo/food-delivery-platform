package org.vovgoo.userservice.service.address.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.userservice.dto.address.request.CreateAddressRequest;
import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.entity.Address;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.enums.AddressStatus;
import org.vovgoo.userservice.exception.custom.address.AddressNotFound;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.mapper.AddressMapper;
import org.vovgoo.userservice.repository.AddressRepository;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.address.AddressService;
import org.vovgoo.userservice.service.user.aspects.checkstatus.CheckUserStatus;
import org.vovgoo.userservice.utils.CurrentUserUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    @CheckUserStatus
    public PageResponse<AddressResponse> getAll(PageParams pageParams) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());

        Page<AddressResponse> addresses = addressRepository.findAllByUserIdAndAddressStatus(pageRequest, userId, AddressStatus.ACTIVE)
                .map(addressMapper::toResponse);

        return PageResponse.of(addresses);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public AddressResponse create(CreateAddressRequest createAddressRequest) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        addressRepository.findByUserIdAndIsDefault(userId, true)
                .ifPresent(current -> current.setDefault(false));

        Address address = Address.builder()
                .country(createAddressRequest.country())
                .state(createAddressRequest.state())
                .city(createAddressRequest.city())
                .street(createAddressRequest.street())
                .house(createAddressRequest.house())
                .building(createAddressRequest.building())
                .apartment(createAddressRequest.apartment())
                .deliveryInstructions(createAddressRequest.deliveryInstructions())
                .zip(createAddressRequest.zip())
                .addressStatus(AddressStatus.ACTIVE)
                .isDefault(true)
                .user(user)
                .build();

        address = addressRepository.save(address);

        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void remove(UUID id) {
        Address address = addressRepository.findByIdAndUserId(id, CurrentUserUtils.getCurrentUserId())
                .orElseThrow(AddressNotFound::new);

        address.setDefault(false);
        address.setAddressStatus(AddressStatus.DELETED);

        addressRepository.save(address);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void setDefault(UUID id) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        addressRepository.findByUserIdAndIsDefault(userId, true)
                .ifPresent(current -> current.setDefault(false));

        Address newDefault = addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(AddressNotFound::new);

        newDefault.setDefault(true);

        addressRepository.save(newDefault);
    }
}
