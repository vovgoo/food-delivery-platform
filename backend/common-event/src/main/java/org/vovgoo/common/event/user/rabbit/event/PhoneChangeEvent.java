package org.vovgoo.common.event.user.rabbit.event;

import lombok.Builder;

@Builder
public record PhoneChangeEvent(String phone, String otp) {}
