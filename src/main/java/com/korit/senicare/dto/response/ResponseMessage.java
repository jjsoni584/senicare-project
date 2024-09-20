package com.korit.senicare.dto.response;


public interface ResponseMessage {
    
    String SUCCESS = "Success.";

    String VALIDATION_FAIL = "Validation failed.";
    String DUPLICATED_USER_ID = "Duplicated user id.";
    String DUPLICATED_TEL_NUMBER = "Duplicated user tel number.";
    String NO_EXIST_USER_ID = "No exist user id";
    String NO_EXIST_TOOL = "No exist tool";

    String TEL_AUTH_FAIL = "Tel number authentication faild";
    String Sign_IN_FAIL = "SignIn faild";
    String AUTHENTICATION_FAIL = "Authentication faild";

    String MESSAGE_SEND_FAIL = "Auth number send failed.";
    String TOKEN_CAREATE_FAIL = "Token create failed.";
    String DATABASE_ERROR = "Database error.";

}