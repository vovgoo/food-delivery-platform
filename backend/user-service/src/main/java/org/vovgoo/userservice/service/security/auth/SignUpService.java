package org.vovgoo.userservice.service.security.auth;

import org.vovgoo.userservice.dto.security.auth.request.ConfirmSignUpRequest;
import org.vovgoo.userservice.dto.security.auth.request.SignUpRequest;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;

public interface SignUpService {
    void signUp(SignUpRequest request);
    JwtPair confirmSignUp(ConfirmSignUpRequest request);
}
