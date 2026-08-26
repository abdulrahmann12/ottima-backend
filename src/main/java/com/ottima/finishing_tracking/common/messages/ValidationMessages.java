package com.ottima.finishing_tracking.common.messages;

public class ValidationMessages {

    //========================= Password =========================

    public static final String PASSWORD_NOT_BLANK = "Password cannot be blank";
    public static final String CURRENT_PASSWORD_NOT_BLANK = "Password cannot be blank";

    //========================= Role =========================

    public static final String ROLE_NAME_NOT_BLANK = "Role name must not be blank";

    //========================= Auth =========================

    public static final String USERNAME_OR_EMAIL_OR_PHONE_NUMBER_REQUIRED = "Username or email or phone number is required";;
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required";
    public static final String VERIFICATION_CODE = "Verification code is required";

    //========================= User =========================
    public static final String USERNAME_NOT_BLANK = "Username is required";
    public static final String USERNAME_SIZE = "Username must be between 6 and 50 characters";
    public static final String EMAIL_NOT_BLANK = "Email is required";
    public static final String EMAIL_INVALID = "Email should be valid";
    public static final String EMAIL_TOO_LONG = "Email must be less than 100 characters";
    public static final String FULL_NAME_NOT_BLANK_AR = "Full Arabic name is required";
    public static final String FULL_NAME_NOT_BLANK_EN = "Full English name is required";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PHONE_NUMBER_REQUIRED = "Phone Number is required";
    public static final String PASSWORD_MIN_SIZE = "Password must be at least 8 characters long";
    public static final String PHONE_NUMBER_SIZE = "Phone Number must be 11 characters long";
    public static final String CURRENT_PASSWORD_REQUIRED = "Current password is required";
    public static final String NEW_PASSWORD_REQUIRED = "New password is required";
    public static final String NEW_PASSWORD_MIN_SIZE = "New password must be at least 8 characters long";
    public static final String ROLE_REQUIRED = "Role is required";

}
