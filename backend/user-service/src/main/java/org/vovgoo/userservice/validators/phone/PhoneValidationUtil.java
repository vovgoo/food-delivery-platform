package org.vovgoo.userservice.validators.phone;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneValidationUtil {

    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public static boolean isValid(String phone) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, null);
            return phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
