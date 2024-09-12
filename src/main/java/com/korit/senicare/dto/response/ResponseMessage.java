package com.korit.senicare.dto.response;


public interface ResponseMessage {
    
    String SUCCESS = "Success.";

    String VALIDATION_FAIL = "Validation failed.";
    String DUPLICATED_USER_ID = "Duplicated user id.";
    String DUPLICATED_TEL_NUMBER = "Duplicated user tel number.";

    String TEL_AUTH_FAIL = "TELL";

    String MESSAGE_SEND_FAIL = "Auth number send failed.";
    String DATABASE_ERROR = "Database error.";

}