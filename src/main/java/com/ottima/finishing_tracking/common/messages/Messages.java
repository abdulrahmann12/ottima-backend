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
    public static final String CLIENT_NOT_FOUND = "Client not found";
    public static final String ENGINEER_NOT_FOUND = "Engineer not found";
    public static final String USER_NOT_ACTIVE = "This user account is deactivated";
    public static final String USER_ALREADY_DEACTIVATED = "This user account is already deactivated";
    public static final String USER_ALREADY_ACTIVATED = "This user account is already activated";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String EMAIL_ALREADY_OR_USERNAME_EXISTS = "Email or Username already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String USER_NOT_CLIENT = "The selected user does not have a CLIENT role";
    public static final String USER_NOT_ENGINEER = "The selected user does not have an ENGINEER role";

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

    //========================= Standard Item ============================
    public static final String STANDARD_ITEM_CREATED = "Standard item created successfully";
    public static final String STANDARD_ITEM_UPDATED = "Standard item updated successfully";
    public static final String STANDARD_ITEM_DELETED = "Standard item deleted successfully";
    public static final String STANDARD_ITEM_FETCHED = "Standard item fetched successfully";
    public static final String STANDARD_ITEMS_FETCHED = "Standard items fetched successfully";
    public static final String STANDARD_ITEM_NOT_FOUND = "Standard item not found";
    public static final String STANDARD_ITEM_ALREADY_EXISTS = "Standard item with the same name already exists";
    public static final String STANDARD_ITEM_IN_USE = "Cannot delete standard item because it is currently used in one or more projects";

    //========================= Project ============================
    public static final String PROJECT_CREATED = "Project created successfully";
    public static final String PROJECT_UPDATED = "Project updated successfully";
    public static final String PROJECT_DELETED = "Project deleted successfully";
    public static final String PROJECT_STATUS_CHANGED = "Project status updated successfully";
    public static final String PROJECT_FETCHED = "Project fetched successfully";
    public static final String PROJECTS_FETCHED = "Projects fetched successfully";
    public static final String PROJECT_NOT_FOUND = "Project not found";
    public static final String PROJECT_ACCESS_DENIED = "You do not have permission to view this project.";
    public static final String INVALID_ROLE = "Invalid role assignment";

    //========================= Project Item ============================
    public static final String PROJECT_ITEM_ADDED = "Item added to project successfully";
    public static final String PROJECT_ITEM_UPDATED = "Project item updated successfully";
    public static final String PROJECT_ITEM_REMOVED = "Project item removed successfully";
    public static final String PROJECT_ITEM_PROGRESS_UPDATED = "Item progress updated successfully";
    public static final String PROJECT_ITEM_NOT_FOUND = "Project item not found";
    public static final String PROJECT_ITEM_ALREADY_EXISTS = "This item has already been added to the project";
    public static final String WEIGHT_LIMIT_EXCEEDED = "Total project items weight cannot exceed 100%";

    //========================= Daily Update ============================
    public static final String DAILY_UPDATE_CREATED = "Daily update created successfully";
    public static final String DAILY_UPDATE_FETCHED = "Daily update fetched successfully";
    public static final String DAILY_UPDATES_FETCHED = "Daily updates fetched successfully";
    public static final String DAILY_UPDATE_EVALUATED = "Daily update evaluated successfully";
    public static final String DAILY_UPDATE_NOT_FOUND = "Daily update not found";
    public static final String DAILY_UPDATE_IMAGE_NOT_FOUND = "Daily update image not found";
    public static final String CANNOT_COMMENT_ON_UNAPPROVED_UPDATE = "Cannot comment on unapproved daily updates";

    //========================= Comment ============================
    public static final String COMMENT_ADDED = "Comment added successfully";
    public static final String COMMENT_UPDATED = "Comment updated successfully";
    public static final String COMMENT_DELETED = "Comment deleted successfully";
    public static final String COMMENT_REPLIED = "Reply added successfully";
    public static final String COMMENTS_FETCHED = "Comments fetched successfully";
    public static final String COMMENT_NOT_FOUND = "Comment not found";
    public static final String COMMENT_ACCESS_DENIED = "You do not have permission to modify or delete this comment.";
    public static final String CANNOT_DELETE_COMMENT = "Cannot edit a comment that has already been replied to by an admin.";


}


