package org.vovgoo.userservice.service.security.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.userservice.dto.auth.request.LoginRequest;
import org.vovgoo.userservice.dto.auth.request.RegisterRequest;
import org.vovgoo.userservice.dto.internal.JwtPair;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.enums.RoleType;
import org.vovgoo.userservice.exception.EmailAlreadyExistsException;
import org.vovgoo.userservice.repository.RoleRepository;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.security.AuthService;
import org.vovgoo.userservice.service.security.JwtService;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtPair login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        User user = (User) authentication.getPrincipal();

        return new JwtPair(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user)
        );
    }

    @Override
    @Transactional
    public JwtPair register(RegisterRequest registerRequest) {
        userRepository.findByEmail(registerRequest.email())
                .ifPresent(u -> { throw new EmailAlreadyExistsException(registerRequest.email()); });

        Role defaultRole = roleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> new EntityNotFoundException("Роль пользователя не найдена."));

        User user = User.builder()
                .email(registerRequest.email())
                .fullName(registerRequest.fullName())
                .passwordHash(passwordEncoder.encode(registerRequest.password()))
                .roles(Set.of(defaultRole))
                .build();

        userRepository.save(user);

        return new JwtPair(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user)
        );
    }

    @Override
    public JwtPair refreshAccessToken(String refreshToken) {
        String email = jwtService.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        String newAccessToken = jwtService.refreshAccessToken(refreshToken, user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new JwtPair(newAccessToken, newRefreshToken);
    }
}
