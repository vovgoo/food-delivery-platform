package org.vovgoo.userservice.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vovgoo.security.utils.CurrentUserUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrentUserUtilsTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserId_shouldReturnUuid() {
        String userId = UUID.randomUUID().toString();

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn(userId);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);

        UUID result = CurrentUserUtils.getCurrentUserId();
        assertEquals(UUID.fromString(userId), result);
    }

    @Test
    void getCurrentUserId_shouldThrowWhenNotAuthenticated() {
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(context);

        assertThrows(IllegalStateException.class, CurrentUserUtils::getCurrentUserId);
    }

    @Test
    void getCurrentUserId_shouldThrowWhenUuidInvalid() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("not-a-uuid");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);

        assertThrows(IllegalStateException.class, CurrentUserUtils::getCurrentUserId);
    }
}
