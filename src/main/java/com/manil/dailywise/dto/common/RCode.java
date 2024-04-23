package com.manil.dailywise.dto.common;

import org.springframework.http.HttpStatus;

public enum RCode {
    OK(100000, "OK", HttpStatus.OK),

    // Common Validation
    HEADER_REQUIRED     (101001, "Parameter '%s' required", HttpStatus.BAD_REQUEST),
    HEADER_INVALID      (101002, "Header '%s' invalid", HttpStatus.BAD_REQUEST),
    PARAMETER_REQUIRED  (101011, "Parameter '%s' required", HttpStatus.BAD_REQUEST),
    PARAMETER_INVALID   (101012, "Parameter '%s' invalid", HttpStatus.BAD_REQUEST),
    BODY_REQUIRED       (101021, "Body '%s' required", HttpStatus.BAD_REQUEST),
    BODY_INVALID        (101022, "Body '%s' invalid", HttpStatus.BAD_REQUEST),
    HTTP_METHOD_NOT_ALLOWED        (101024, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
    JSON_PARSE_ERROR (101025, "JSON PARSE ERROR", HttpStatus.BAD_REQUEST),
    MEDIA_TYPE_NOT_SUPPORTED (101025, "Media type not supported", HttpStatus.BAD_REQUEST),
    MAX_FILE_SIZE (101026,"Max File size limit ('%s')",HttpStatus.BAD_REQUEST),
    PAGE_NOT_FOUND (101030, "Endpoint is invalid", HttpStatus.NOT_FOUND),
    VERSION_IS_NOT_ALLOW (101031, "This version is not allowed.", HttpStatus.BAD_REQUEST),


    RESOURCE_NOT_EXISTS     (101031, "Resource not found '%s'", HttpStatus.BAD_REQUEST),
    RESOURCE_OWNER_MISMATCH (101032, "Resouce not found '%s'", HttpStatus.BAD_REQUEST),

    // Server Error
    INTERNAL_SERVER_ERROR   (101500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_DATABASE_ERROR (101501, "Internal Database Error", HttpStatus.INTERNAL_SERVER_ERROR),
    REFERENCE_SERVER_ERROR  (101502, "Reference Server Error", HttpStatus.SERVICE_UNAVAILABLE),
    S3_UPLOAD_ERROR (101503, "Internal Database Error", HttpStatus.INTERNAL_SERVER_ERROR),

    // Account 2
    TOKEN_EXPIRED(102001, "Token is expired", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(102002, "Unauthorized", HttpStatus.UNAUTHORIZED),
    ALREADY_JOINED(102003, "Email address '%s' is already joined", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCHED(102004, "Password is not matched", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_IN_USE(102005, "Email is already in use", HttpStatus.BAD_REQUEST),
    INVALID_VALIDATION_CODE(102006, "Validation code is not invalid",HttpStatus.BAD_REQUEST),
    CANNOT_FIND_USER_FOR_PROFILE(102007, "Cannot find user '%s'", HttpStatus.BAD_REQUEST),
    CANNOT_FIND_EMAIL(102008, "Cannot find email '%s'" , HttpStatus.BAD_REQUEST ),
    INVALID_PASSWORD(102009, "Invalid password", HttpStatus.BAD_REQUEST),
    YOUTUBE_DURATION_TYPE_IS_INVALID(102010, "Youtube duration data type is invalid", HttpStatus.BAD_REQUEST),
    CANNOT_FIND_USER_FOR_AUTH(102010, "Cannot find user '%s'", HttpStatus.UNAUTHORIZED),
    DO_NOT_HAVE_PERMISSION(102011, "Do not have permission for this resource", HttpStatus.BAD_REQUEST),
    ALREADY_INVITED(1020012, "Email address '%s' is already invited", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1020013, "This email is malformed", HttpStatus.BAD_REQUEST),
    // User 3
    USER_NOT_EXISTS     (103001, "Not exists user", HttpStatus.BAD_REQUEST),
    FAIL_SNS_VERIFY (103002, "Sns token not vefiry %s", HttpStatus.BAD_REQUEST),
    USER_NOT_VERIFY (103003, "User not verify", HttpStatus.BAD_REQUEST),

    // Wise
    WRITER_NOT_EXISTS (104001, "not exists writer", HttpStatus.BAD_REQUEST),
    WISE_NOT_EXISTS (104002, "not exists wise", HttpStatus.BAD_REQUEST),

    // File
    FILE_NOT_FOUND (105002, "not exists wise", HttpStatus.BAD_REQUEST);

    private final int resultCode;
    private final HttpStatus httpStatus;
    private final String resultMessage;

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    RCode(int errorCode, String errorMessage, HttpStatus status) {
        this.resultCode = errorCode;
        this.resultMessage = errorMessage;
        this.httpStatus = status;
    }
}
