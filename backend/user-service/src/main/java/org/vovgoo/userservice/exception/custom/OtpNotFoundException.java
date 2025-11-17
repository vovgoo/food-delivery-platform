package org.vovgoo.userservice.exception.custom;

public class OtpNotFoundException extends RuntimeException {
  public OtpNotFoundException() {
    super("OTP код не найден или истёк");
  }
}
