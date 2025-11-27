package org.vovgoo.common.event.user.rabbit.event;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EmailChangeEvent(String email, UUID token) {}
