package org.vovgoo.userservice.domain.rabbit.event;

import lombok.Builder;

@Builder
public record SignUpPhoneEvent(String phone, String otp) {}
