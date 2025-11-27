package org.vovgoo.common.event.user.rabbit.event;

import lombok.Builder;

@Builder
public record SignUpPhoneEvent(String phone, String otp) {}
