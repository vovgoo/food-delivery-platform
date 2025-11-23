package org.vovgoo.userservice.service.security.auth;

import org.vovgoo.userservice.dto.security.auth.request.*;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;
import org.vovgoo.userservice.dto.security.jwt.response.JwtResponse;
import org.vovgoo.userservice.dto.verification.phone.request.ConfirmOtpRequest;

public interface AuthService {
    JwtPair signIn(SignInRequest signInRequest);
    void signUp(SignUpRequest signUpRequest);
    JwtPair confirmSignUp(ConfirmOtpRequest confirmOtpRequest);
    JwtResponse refreshAccessToken(String refreshToken);
}
