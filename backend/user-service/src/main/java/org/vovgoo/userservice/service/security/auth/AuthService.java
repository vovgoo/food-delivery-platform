package org.vovgoo.userservice.service.security.auth;

import org.vovgoo.userservice.dto.security.auth.request.*;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;
import org.vovgoo.userservice.dto.security.jwt.response.JwtResponse;

public interface AuthService {
    JwtPair signIn(SignInRequest request);
    JwtResponse refreshAccessToken(String refreshToken);
}
