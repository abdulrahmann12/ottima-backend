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

    //========================= Project Tag ===============================
    public static final String TAG_PROJECT_ADMIN = "Admin - Project Management";
    public static final String TAG_PROJECT_ADMIN_DESC = "Admin endpoints for creating, updating, deleting projects and managing project items";
    public static final String TAG_PROJECT_ENGINEER = "Engineer - Project Tracking";
    public static final String TAG_PROJECT_ENGINEER_DESC = "Engineer endpoints for viewing assigned projects and updating item progress";
    public static final String TAG_PROJECT_CLIENT = "Client - Project Dashboard";
    public static final String TAG_PROJECT_CLIENT_DESC = "Client endpoints for viewing owned projects and their financial/progress details";

    // Admin - Project
    public static final String CREATE_PROJECT = "Create Project";
    public static final String CREATE_PROJECT_DESC = "Creates a new project and assigns roles to engineers and clients";
    public static final String UPDATE_PROJECT = "Update Project";
    public static final String UPDATE_PROJECT_DESC = "Updates the main details of an existing project by its ID";
    public static final String DELETE_PROJECT = "Delete Project";
    public static final String DELETE_PROJECT_DESC = "Soft-deletes a project by its ID";
    public static final String CHANGE_PROJECT_STATUS = "Change Project Status";
    public static final String CHANGE_PROJECT_STATUS_DESC = "Updates the overall status of a project (e.g. IN_PROGRESS, COMPLETED)";
    public static final String GET_ALL_PROJECTS_ADMIN = "Get All Projects (Admin)";
    public static final String GET_ALL_PROJECTS_ADMIN_DESC = "Returns a paginated summary of all projects for admin overview";
    public static final String GET_PROJECT_DETAILS_ADMIN = "Get Project Details (Admin)";
    public static final String GET_PROJECT_DETAILS_ADMIN_DESC = "Returns full project details including items and assignments";

    // Admin - Project Items
    public static final String ASSIGN_PROJECT_ITEMS = "Assign Project Items";
    public static final String ASSIGN_PROJECT_ITEMS_DESC = "Bulk-assigns multiple standard items to a project";
    public static final String UPDATE_PROJECT_ITEM_CONFIG = "Update Project Item Config";
    public static final String UPDATE_PROJECT_ITEM_CONFIG_DESC = "Updates the budget, weight, or sequence of a specific project item";
    public static final String REMOVE_PROJECT_ITEM = "Remove Project Item";
    public static final String REMOVE_PROJECT_ITEM_DESC = "Removes an item from a project by item ID";

    // Engineer - Project
    public static final String GET_ALL_PROJECTS_ENGINEER = "Get All Projects (Engineer)";
    public static final String GET_ALL_PROJECTS_ENGINEER_DESC = "Returns a paginated project summary for the engineer's overview";
    public static final String GET_ASSIGNED_PROJECTS_ENGINEER = "Get My Assigned Projects";
    public static final String GET_ASSIGNED_PROJECTS_ENGINEER_DESC = "Returns a paginated list of projects specifically assigned to the logged-in engineer";
    public static final String GET_PROJECT_DETAILS_ENGINEER = "Get Project Details (Engineer)";
    public static final String GET_PROJECT_DETAILS_ENGINEER_DESC = "Returns engineering-focused project details without financials";
    public static final String UPDATE_ITEM_PROGRESS = "Update Item Progress";
    public static final String UPDATE_ITEM_PROGRESS_DESC = "Updates the on-site progress percentage and status for a specific project item";

    // Client - Project
    public static final String GET_MY_PROJECTS = "Get My Projects";
    public static final String GET_MY_PROJECTS_DESC = "Returns a paginated summary of all projects owned by the logged-in client";
    public static final String GET_PROJECT_DETAILS_CLIENT = "Get Project Details (Client)";
    public static final String GET_PROJECT_DETAILS_CLIENT_DESC = "Returns full project details including financials and progress for the client";

    //========================= Daily Update Tag ===============================
    public static final String TAG_DAILY_UPDATE_ADMIN = "Admin - Daily Updates";
    public static final String TAG_DAILY_UPDATE_ADMIN_DESC = "Endpoints for admins to monitor and evaluate daily updates";
    public static final String TAG_DAILY_UPDATE_ENGINEER = "Engineer - Daily Updates";
    public static final String TAG_DAILY_UPDATE_ENGINEER_DESC = "Endpoints for engineers to manage daily updates";
    public static final String TAG_DAILY_UPDATE_CLIENT = "Client - Daily Updates";
    public static final String TAG_DAILY_UPDATE_CLIENT_DESC = "Endpoints for clients to view approved item updates";

    // Admin - Daily Updates
    public static final String GET_ALL_DAILY_UPDATES_ADMIN = "Get All Project Updates (Admin)";
    public static final String GET_ALL_DAILY_UPDATES_ADMIN_DESC = "Admin views all updates with dynamic filtering";
    public static final String EVALUATE_DAILY_UPDATE = "Evaluate Daily Update";
    public static final String EVALUATE_DAILY_UPDATE_DESC = "Admin approves/rejects an update and its images";

    // Engineer - Daily Updates
    public static final String CREATE_DAILY_UPDATE = "Create Daily Update";
    public static final String CREATE_DAILY_UPDATE_DESC = "Engineer creates a new daily update for a project item";
    public static final String GET_MY_DAILY_UPDATES = "Get My Daily Updates";
    public static final String GET_MY_DAILY_UPDATES_DESC = "Engineer views their own updates with optional filters";

    // Client - Daily Updates
    public static final String GET_APPROVED_ITEM_UPDATES_CLIENT = "Get Approved Item Updates (Client)";
    public static final String GET_APPROVED_ITEM_UPDATES_CLIENT_DESC = "Client views only approved updates for a specific project item";
}

