package org.vovgoo.userservice.domain.rabbit.event;

import lombok.Builder;

@Builder
public record PhoneChangeEvent(String phone, String otp) {}
