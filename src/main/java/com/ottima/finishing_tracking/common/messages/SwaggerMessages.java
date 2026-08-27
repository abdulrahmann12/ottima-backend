package com.ottima.finishing_tracking.common.messages;

public class SwaggerMessages {

    //========================= Auth Tag =========================

    public static final String TAG_AUTH = "Auth";
    public static final String TAG_AUTH_DESC = "Endpoints for authentication and token management";

    public static final String LOGIN = "Login";
    public static final String LOGIN_DESC = "Authenticate with username/email and password, returns access and refresh tokens";

    public static final String REFRESH_TOKEN = "Refresh Token";
    public static final String REFRESH_TOKEN_DESC = "Exchange a valid refresh token for a new access token and rotated refresh token";

    public static final String LOGOUT = "Logout";
    public static final String LOGOUT_DESC = "Revoke the refresh token and end the session";

    public static final String VERIFY_ACCOUNT = "Verify Account";
    public static final String VERIFY_ACCOUNT_DESC = "Verify a user's account using a verification code sent via email";
    public static final String REGENERATE_CODE = "Regenerate Verification Code";
    public static final String REGENERATE_CODE_DESC = "Regenerate a new verification code for a user and send it via email";

    public static final String FORGOT_PASSWORD = "Forgot Password";
    public static final String FORGOT_PASSWORD_DESC = "Send a password reset email to the user with a reset link or code";

    public static final String RESET_PASSWORD = "Reset Password";
    public static final String RESET_PASSWORD_DESC = "Send a password reset email to the user with a reset link or code";

    public static final String CHANGE_PASSWORD = "Change Password";
    public static final String CHANGE_PASSWORD_DESC = "Change the password of the authenticated user by providing the current and new password";

    //========================= Role Tag =========================

    public static final String TAG_ROLE = "Role";
    public static final String TAG_ROLE_DESC = "Endpoints for managing roles";

    public static final String CREATE_ROLE = "Create Role";
    public static final String CREATE_ROLE_DESC = "Creates a new role with a unique name";

    public static final String UPDATE_ROLE = "Update Role";
    public static final String UPDATE_ROLE_DESC = "Updates an existing role by its ID";

    public static final String GET_ALL_ROLES = "Get All Roles";
    public static final String GET_ALL_ROLES_DESC = "Returns a paginated list of all roles";

    public static final String GET_ROLE_BY_ID = "Get Role By ID";
    public static final String GET_ROLE_BY_ID_DESC = "Returns a single role by its numeric ID";

    public static final String GET_ROLE_BY_NAME = "Get Role By Name";
    public static final String GET_ROLE_BY_NAME_DESC = "Returns a single role by its name";

    public static final String DELETE_ROLE = "Delete Role";
    public static final String DELETE_ROLE_DESC = "Deletes a role by its ID";

    //========================= User Tag =========================

    public static final String TAG_USER = "User";
    public static final String TAG_USER_DESC = "Endpoints for managing users";

    public static final String CREATE_USER = "Register User";
    public static final String CREATE_USER_DESC = "Registers a new user with default USER role";

    public static final String UPDATE_USER = "Update User";
    public static final String UPDATE_USER_DESC = "Updates username, email, fullName and profile info";

    public static final String GET_USER_BY_ID = "Get User By ID";
    public static final String GET_USER_BY_ID_DESC = "Returns a single active user by their ID";

    public static final String GET_USER_BY_IDENTIFIER = "Get User By Username or Email or Phone Number";
    public static final String GET_USER_BY_IDENTIFIER_DESC = "Finds an active user by username or email or Phone Number";

    public static final String GET_ALL_USERS = "Get All Users";
    public static final String GET_ALL_USERS_DESC = "Returns a paginated list of all users";

    public static final String DELETE_USER = "Deactivate User";
    public static final String DELETE_USER_DESC = "Soft-deletes a user by setting active = false";

    public static final String ACTIVATE_USER = "Activate User";
    public static final String ACTIVATE_USER_DESC = "Reactivates a previously deactivated user";

    public static final String SEARCH_USERS = "Search Users";
    public static final String SEARCH_USERS_DESC = "Searches users by username, email, or full name";

    public static final String GET_USERS_BY_ROLE = "Get Users By Role";
    public static final String GET_USERS_BY_ROLE_DESC = "Returns paginated users filtered by role ID";

    public static final String UPDATE_PROFILE_PICTURE = "Update Profile Picture";
    public static final String UPDATE_PROFILE_PICTURE_DESC = "Uploads and sets a new profile picture via Cloudinary";

    public static final String GET_DEACTIVATED_USERS = "Get Deactivated Users";
    public static final String GET_DEACTIVATED_USERS_DESC = "Returns a paginated list of all deactivated users";

    public static final String UPDATE_USER_BY_ADMIN = "Update User By Admin";
    public static final String UPDATE_USER_BY_ADMIN_DESC = "Updates a user's information by an administrator";

    public static final String GET_USER_DATA = "Get User Data";
    public static final String GET_USER_DATA_DESC = "Returns the data of the authenticated user";

    //========================= Admin Tag =========================
    public static final String TAG_ADMIN = "Admin";
    public static final String TAG_ADMIN_DESC = "Endpoints for administrative tasks";


    public static final String CREATE_ADMIN_USER = "Create Admin User";
    public static final String CREATE_ADMIN_USER_DESC = "Creates a new user with ADMIN role";

    public static final String GET_DASHBOARD_SUMMARY = "Get Dashboard Summary";
    public static final String GET_DASHBOARD_SUMMARY_DESC = "Retrieves statistics for the admin dashboard, including user counts and other metrics";

    public static final String GET_ALL_ADMINS = "Get All Admins";
    public static final String GET_ALL_ADMINS_DESC = "Returns a paginated list of all admin users";

    //========================= Engineer Tag =========================
    public static final String TAG_ENGINEER = "Engineer";
    public static final String TAG_ENGINEER_DESC = "Endpoints for managing engineers";

    public static final String CREATE_ENGINEER_USER = "Create Engineer User";
    public static final String CREATE_ENGINEER_USER_DESC = "Creates a new user with ENGINEER role";

    public static final String GET_ALL_ENGINEERS = "Get All Engineers";
    public static final String GET_ALL_ENGINEERS_DESC = "Returns a paginated list of all engineer users";

    //========================= Client Tag =========================
    public static final String TAG_CLIENT = "Client";
    public static final String TAG_CLIENT_DESC = "Endpoints for managing clients";

    public static final String CREATE_CLIENT_USER = "Create Client User";
    public static final String CREATE_CLIENT_USER_DESC = "Creates a new user with CLIENT role";

    public static final String GET_ALL_CLIENTS = "Get All Clients";
    public static final String GET_ALL_CLIENTS_DESC = "Returns a paginated list of all client users";

    //========================= Standard Item Tag =========================
    public static final String TAG_STANDARD_ITEM = "Standard Items";
    public static final String TAG_STANDARD_ITEM_DESC = "Standard Items Management Module";
    public static final String CREATE_STANDARD_ITEM = "Create Standard Item";
    public static final String UPDATE_STANDARD_ITEM = "Update Standard Item";
    public static final String DELETE_STANDARD_ITEM = "Delete Standard Item";
    public static final String GET_STANDARD_ITEM = "Get Standard Item by ID";
    public static final String GET_ALL_STANDARD_ITEMS = "Get All Standard Items";
}
