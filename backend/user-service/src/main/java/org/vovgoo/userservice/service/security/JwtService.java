package org.vovgoo.userservice.service.security;

import org.vovgoo.userservice.entity.User;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
    String refreshAccessToken(String refreshToken, User user);
}
