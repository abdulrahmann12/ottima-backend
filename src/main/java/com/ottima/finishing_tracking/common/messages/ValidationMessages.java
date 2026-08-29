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

    //========================= Standard Item =========================
    public static final String ITEM_NAME_AR_REQUIRED = "Arabic name is required";
    public static final String ITEM_NAME_EN_REQUIRED = "English name is required";

    //========================= Project =========================
    public static final String PROJECT_NAME_AR_REQUIRED = "Project Arabic name is required";
    public static final String PROJECT_NAME_EN_REQUIRED = "Project English name is required";
    public static final String CLIENT_ID_REQUIRED = "Client ID is required";
    public static final String ENGINEER_ID_REQUIRED = "Engineer ID is required";
    public static final String BUDGET_REQUIRED = "Budget must be specified";
    public static final String BUDGET_MIN = "Budget cannot be negative";
    public static final String DATE_REQUIRED = "Date is required";
    public static final String DATE_MUST_BE_IN_FUTURE = "Date must be in the future";

    //========================= Project Item =========================
    public static final String STANDARD_ITEM_ID_REQUIRED = "Standard item ID is required";
    public static final String WEIGHT_REQUIRED = "Weight percentage is required";
    public static final String WEIGHT_MIN = "Weight percentage cannot be less than 0";
    public static final String WEIGHT_MAX = "Weight percentage cannot exceed 100";
    public static final String PROGRESS_REQUIRED = "Completion percentage is required";
    public static final String PROGRESS_MIN = "Completion percentage cannot be less than 0";

    //========================= Daily Update =========================
    public static final String PROJECT_ITEM_ID_REQUIRED = "Project item ID is required";
    public static final String TITLE_REQUIRED = "Title is required";
    public static final String UPDATE_STATUS_REQUIRED = "Status is required";
    public static final String UPDATE_IMAGE_ID_REQUIRED = "Image ID is required";
    public static final String APPROVAL_STATUS_REQUIRED = "Approval status is required";
    public static final String TITLE_SIZE = "Title must be between 1 and 255 characters";
    public static final String NOTES_SIZE = "Notes must be less than 2000 characters";

    //========================= Comment =========================
    public static final String COMMENT_TEXT_REQUIRED = "Comment text cannot be empty";
    public static final String COMMENT_TEXT_SIZE = "Comment must be less than 1000 characters";
    public static final String ADMIN_REPLY_REQUIRED = "Admin reply cannot be blank";
    public static final String ADMIN_REPLY_SIZE = "Admin reply must be less than 1000 characters";

    //========================= Financial =========================
    public static final String FINANCIAL_RECORD_TYPE_REQUIRED = "Record type is required";
    public static final String FINANCIAL_AMOUNT_REQUIRED = "Amount is required";
    public static final String FINANCIAL_AMOUNT_POSITIVE = "Amount must be greater than zero";
    public static final String FINANCIAL_PAYMENT_METHOD_REQUIRED = "Payment method is required";
    public static final String FINANCIAL_TRANSACTION_DATE_REQUIRED = "Transaction date is required";
    public static final String FINANCIAL_NOTES_SIZE = "Notes must be less than 500 characters";
    public static final String FINANCIAL_DOCUMENT_URL_SIZE = "Document URL must be less than 2048 characters";
}



