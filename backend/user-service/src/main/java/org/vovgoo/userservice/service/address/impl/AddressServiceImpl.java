package org.vovgoo.userservice.service.address.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.userservice.dto.address.request.CreateAddressRequest;
import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.dto.common.PageParams;
import org.vovgoo.userservice.dto.common.PageResponse;
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
    @CheckUserStatus
    @Transactional
    public AddressResponse create(CreateAddressRequest createAddressRequest) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        addressRepository.resetDefaultForUser(userId);

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
    @CheckUserStatus
    @Transactional
    public void remove(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(AddressNotFound::new);

        address.setDefault(false);
        address.setAddressStatus(AddressStatus.DELETED);

        addressRepository.save(address);
    }

    @Override
    @CheckUserStatus
    @Transactional
    public void setDefault(UUID id) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        Address address = addressRepository.findById(id)
                .orElseThrow(AddressNotFound::new);

        addressRepository.resetDefaultForUser(userId);

        address.setDefault(true);

        addressRepository.save(address);
    }
}
