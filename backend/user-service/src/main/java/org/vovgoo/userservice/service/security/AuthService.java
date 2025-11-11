package org.vovgoo.userservice.service.security;

import org.vovgoo.userservice.dto.auth.request.LoginRequest;
import org.vovgoo.userservice.dto.auth.request.RegisterRequest;
import org.vovgoo.userservice.dto.internal.JwtPair;

public interface AuthService {
    JwtPair login(LoginRequest loginRequest);
    JwtPair register(RegisterRequest registerRequest);
    JwtPair refreshAccessToken(String refreshToken);
}