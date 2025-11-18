package org.vovgoo.userservice.service.address.impl;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.data.domain.*;
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
import org.vovgoo.userservice.utils.CurrentUserUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private AddressMapper addressMapper;
    private AddressServiceImpl addressService;

    private UUID currentUserId;
    private MockedStatic<CurrentUserUtils> currentUserUtilsMock;

    @BeforeEach
    void setUp() {
        addressRepository = mock(AddressRepository.class);
        userRepository = mock(UserRepository.class);
        addressMapper = mock(AddressMapper.class);

        addressService = new AddressServiceImpl(addressRepository, userRepository, addressMapper);

        currentUserId = UUID.randomUUID();
        currentUserUtilsMock = mockStatic(CurrentUserUtils.class);
        currentUserUtilsMock.when(CurrentUserUtils::getCurrentUserId).thenReturn(currentUserId);
    }

    @AfterEach
    void tearDown() {
        currentUserUtilsMock.close();
    }

    @Test
    void getAll_shouldReturnPagedAddresses() {
        PageParams pageParams = new PageParams(0, 10);
        Address address = Address.builder().id(UUID.randomUUID()).build();
        AddressResponse response = new AddressResponse(UUID.randomUUID(), "test", "test", "test", "test","test", "test", "test", "test", "test", true);
        Page<Address> page = new PageImpl<>(List.of(address));

        when(addressRepository.findAllByUserIdAndAddressStatus(any(Pageable.class), eq(currentUserId), eq(AddressStatus.ACTIVE)))
                .thenReturn(page);
        when(addressMapper.toResponse(address)).thenReturn(response);

        PageResponse<AddressResponse> result = addressService.getAll(pageParams);

        assertEquals(1, result.getContent().size());
        assertSame(response, result.getContent().get(0));
    }

    @Test
    void getAll_shouldReturnEmptyPage_whenNoAddresses() {
        PageParams pageParams = new PageParams(0, 10);
        Page<Address> page = Page.empty();

        when(addressRepository.findAllByUserIdAndAddressStatus(any(Pageable.class), eq(currentUserId), eq(AddressStatus.ACTIVE)))
                .thenReturn(page);

        PageResponse<AddressResponse> result = addressService.getAll(pageParams);

        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void create_shouldSaveAddress_andUnsetCurrentDefault() {
        CreateAddressRequest request = new CreateAddressRequest(
                "Country", "State", "City", "Street", "1", "B", "101", "Leave at door", "12345"
        );
        User user = new User();
        Address currentDefault = Address.builder().isDefault(true).build();
        AddressResponse response = new AddressResponse(
                UUID.randomUUID(), "Country", "State", "City", "Street",
                "1", "B", "101", "Leave at door", "12345", true
        );

        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(user));
        when(addressRepository.findByUserIdAndIsDefault(currentUserId, true)).thenReturn(Optional.of(currentDefault));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(addressMapper.toResponse(any(Address.class))).thenReturn(response);

        AddressResponse result = addressService.create(request);

        assertEquals(response, result);

        assertFalse(currentDefault.isDefault());

        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void create_shouldThrow_whenUserNotFound() {
        CreateAddressRequest request = new CreateAddressRequest(null, null, null, null, null, null, null, null, null);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> addressService.create(request));
    }

    @Test
    void remove_shouldMarkAddressDeleted() {
        UUID addressId = UUID.randomUUID();
        Address address = Address.builder().isDefault(true).addressStatus(AddressStatus.ACTIVE).build();
        when(addressRepository.findByIdAndUserId(addressId, currentUserId)).thenReturn(Optional.of(address));

        addressService.remove(addressId);

        assertFalse(address.isDefault());
        assertEquals(AddressStatus.DELETED, address.getAddressStatus());
        verify(addressRepository).save(address);
    }

    @Test
    void remove_shouldThrow_whenAddressNotFound() {
        UUID addressId = UUID.randomUUID();
        when(addressRepository.findByIdAndUserId(addressId, currentUserId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFound.class, () -> addressService.remove(addressId));
    }

    @Test
    void setDefault_shouldUpdateDefaultAddress() {
        UUID newDefaultId = UUID.randomUUID();
        Address currentDefault = Address.builder().isDefault(true).build();
        Address newDefault = Address.builder().isDefault(false).build();

        when(addressRepository.findByUserIdAndIsDefault(currentUserId, true)).thenReturn(Optional.of(currentDefault));
        when(addressRepository.findByIdAndUserId(newDefaultId, currentUserId)).thenReturn(Optional.of(newDefault));

        addressService.setDefault(newDefaultId);

        assertFalse(currentDefault.isDefault());
        assertTrue(newDefault.isDefault());
        verify(addressRepository).save(newDefault);
    }

    @Test
    void setDefault_shouldThrow_whenNewAddressNotFound() {
        UUID newDefaultId = UUID.randomUUID();
        when(addressRepository.findByIdAndUserId(newDefaultId, currentUserId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFound.class, () -> addressService.setDefault(newDefaultId));
    }
}
