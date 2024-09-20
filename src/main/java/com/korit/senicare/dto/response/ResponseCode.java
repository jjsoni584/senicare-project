package com.korit.senicare.dto.response;

// ResponseDTO의 code 상수 

public interface ResponseCode {
    
    String SUCCESS = "SU";
    // 400번대 오류//
    String VALIDATION_FAIL = "VF";
    String DUPLICATED_USER_ID = "DI";
    String DUPLICATED_TEL_NUMBER = "DT";
    String NO_EXIST_USER_ID = "NI";
    String NO_EXIST_TOOL = "NT";
    
    // 401번대 오류//
    String TEL_AUTH_FAIL = "TAF";
    String Sign_IN_FAIL = "SF";
    String AUTHENTICATION_FAIL = "AF";

    // 500번대 오류//
    String MESSAGE_SEND_FAIL = "TF";
    String TOKEN_CAREATE_FAIL = "TCF";
    String DATABASE_ERROR = "DBE";


}
