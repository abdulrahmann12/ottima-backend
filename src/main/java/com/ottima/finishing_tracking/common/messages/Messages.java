package com.ottima.finishing_tracking.common.messages;

public class Messages {

    //========================= User =========================

    public static final String USER_CREATED = "User created successfully";
    public static final String USER_UPDATED = "User updated successfully";
    public static final String USER_DELETED = "User deactivated successfully";
    public static final String USER_ACTIVATED = "User activated successfully";
    public static final String USER_FETCHED = "User retrieved successfully";
    public static final String USERS_FETCHED = "Users retrieved successfully";
    public static final String PROFILE_PICTURE_UPDATED = "Profile picture updated successfully";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_ACTIVE = "This user account is deactivated";
    public static final String USER_ALREADY_DEACTIVATED = "This user account is already deactivated";
    public static final String USER_ALREADY_ACTIVATED = "This user account is already activated";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String EMAIL_ALREADY_OR_USERNAME_EXISTS = "Email or Username already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";

    //========================= Role =========================

    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String ROLE_ALREADY_EXISTS = "Role name already exists";
    public static final String ROLE_CREATED = "Role created successfully";
    public static final String ROLE_UPDATED = "Role updated successfully";
    public static final String ROLE_DELETED = "Role deleted successfully";
    public static final String ROLE_FETCHED = "Role retrieved successfully";
    public static final String ROLES_FETCHED = "Roles retrieved successfully";

    //========================= Auth =========================

    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logged out successfully";
    public static final String ALREADY_LOGGED_OUT = "You are already logged out";
    public static final String TOKEN_REFRESHED = "Token refreshed successfully";
    public static final String INVALID_TOKEN = "Invalid or expired refresh token";
    public static final String BAD_CREDENTIALS = "Invalid username or password";
    public static final String AUTH_FAILED = "Authentication failed";
    public static final String ACCESS_DENIED = "You do not have permission to perform this action";
    public static final String SESSION_EXPIRED = "Your session has expired, please log in again";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String INVALID_VERIFICATION_CODE = "Invalid verification code";
    public static final String ACCOUNT_VERIFIED = "Account verified successfully";
    public static final String ACCOUNT_ALREADY_VERIFIED = "Account is already verified";
    public static final String ACCOUNT_NOT_VERIFIED = "Account is not verified";
    public static final String VERIFICATION_CODE_REGENERATED = "Verification code regenerated successfully";
    public static final String RESET_SUCCESS = "Password reset successfully";
    public static final String VERIFICATION_CODE_EXPIRED = "Verification code has expired, please request a new one";
    public static final String VERIFICATION_CODE_ALREADY_SENT = "A verification code has already been sent, please check your email";
    public static final String INVALID_NEW_PASSWORD = "Invalid new password";
    public static final String WRONG_PASSWORD = "Incorrect password";
    public static final String PASSWORD_CHANGED = "Password changed successfully";

    //========================= Mail =========================

    public static final String FAILED_EMAIL = "Failed to send email, please try again later";
    public static final String RESET_PASSWORD = "Reset your password";
    public static final String RESEND_CODE = "A new code has been sent to your email.";
    public static final String MAIL_CHANGE = "Verify Your Email Change";
    public static final String WELCOME_MAIL = "Welcome to Blink!";
    public static final String VERIFY_EMAIL = "Verify your email";

    //========================= Image =========================

    public static final String IMAGE_UPLOAD_FAILED = "Failed to upload image, please try again later";
    public static final String IMAGE_NULL = "Image must not be null or empty";
    public static final String IMAGE_DELETED_FAILED = "Failed to delete image, please try again later";

    //========================= Rate Limit =========================
    public static final String TOO_MANY_REQUESTS = "Too many requests";

    //========================= General =========================

    public static final String INVALID_DATA = "Invalid or malformed request body";
    public static final String REQUEST_NOT_SUPPORTED = "HTTP method not supported for this endpoint";

    //========================= Admin =============================
    public static final String ADMIN_CREATED = "Admin created successfully";
    public static final String DASHBOARD_SUMMARY_FETCHED = "Dashboard summary fetched successfully";
    public static final String ADMIN_FETCHED = "Admin fetched successfully";

    //========================= Engineer ==========================
    public static final String ENGINEER_CREATED = "Engineer created successfully";
    public static final String ENGINEERS_FETCHED = "Engineers fetched successfully";

    //========================= Client ============================
    public static final String CLIENT_CREATED = "Client created successfully";
    public static final String CLIENTS_FETCHED = "Clients fetched successfully";
}
