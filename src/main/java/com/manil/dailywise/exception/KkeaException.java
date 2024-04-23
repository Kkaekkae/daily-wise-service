package com.manil.dailywise.exception;


import com.manil.dailywise.dto.common.RCode;

public class KkeaException extends RuntimeException {
    RCode errorCode;
    String errorMsgParameter;

    String errorMessage;

    public KkeaException(RCode appErrorCode, String errorMsgParameter, Throwable t) {
        super(t);
        this.errorCode = appErrorCode;
        this.errorMsgParameter = errorMsgParameter;
    }

    public KkeaException(RCode appErrorCode, String errorMsgParameter) {
        super(String.format(appErrorCode.getResultMessage(), errorMsgParameter));
        this.errorCode = appErrorCode;
        this.errorMsgParameter = errorMsgParameter;

    }

    public KkeaException(RCode appErrorCode, Throwable t) {
        super(t);
        this.errorCode = appErrorCode;
    }

    public KkeaException(RCode appErrorCode) {
        super(appErrorCode.getResultMessage());
        this.errorCode = appErrorCode;
    }


    public RCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(RCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsgParameter() {
        return errorMsgParameter;
    }

    public void setErrorMsgParameter(String errorMsgParameter) {
        this.errorMsgParameter = errorMsgParameter;
    }

    public String getErrorMessage() {
        if(errorMsgParameter != null)
            return String.format(errorCode.getResultMessage(), errorMsgParameter);

        return errorCode.getResultMessage();
    }

    public RCode getAppErrorCode() {
        return errorCode;
    }

    public void setAppErrorCode(RCode appErrorCode) {
        this.errorCode = appErrorCode;
    }


}