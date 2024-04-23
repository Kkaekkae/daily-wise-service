package com.manil.dailywise.controller;

import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.dto.common.ErrorResponse;
import com.manil.dailywise.dto.common.RCode;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> requestHandlingNoHandlerFound() {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorCode(RCode.PAGE_NOT_FOUND.getResultCode());
        errorResponse.setErrorMessage(RCode.PAGE_NOT_FOUND.getResultMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(KkeaException.class)
    public ResponseEntity<ErrorResponse> handleSecondSpaceException(KkeaException exception) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorCode(exception.getErrorCode().getResultCode());
        errorResponse.setErrorMessage(exception.getErrorMessage());

        log.info(errorResponse.toString());

        if(exception.getCause() != null) {
            if(exception.getCause().getMessage() != null) {
                log.info(ExceptionUtils.getStackTrace(exception.getCause()));
            }
            log.info(exception.getCause().getMessage());

            if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                    env -> (env.equalsIgnoreCase("prd")
                            || env.equalsIgnoreCase("stg")) )) {
                Sentry.captureException(exception.getCause(), ExceptionUtils.getStackTrace(exception.getCause()));
//                rollbar.error(exception);
            }
        } else {
            if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                    env -> (env.equalsIgnoreCase("prd")
                            || env.equalsIgnoreCase("stg")) )) {
                Sentry.captureException(exception);
//                rollbar.error(exception);
            }
        }

        return new ResponseEntity<>(errorResponse, exception.getErrorCode().getHttpStatus());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse();

        String[] codes = e.getBindingResult().getAllErrors().get(0).getCodes();
        String[] split = codes[0].split("\\.");
        String badParameter = split[split.length - 1];
        String reason = codes[3];

        if("NotNull".equals(reason)) {
            errorResponse.setErrorCode(RCode.BODY_REQUIRED.getResultCode());
            errorResponse.setErrorMessage(String.format(RCode.BODY_REQUIRED.getResultMessage(), badParameter));
        } else {
            errorResponse.setErrorCode(RCode.BODY_INVALID.getResultCode());
            errorResponse.setErrorMessage(String.format(RCode.BODY_INVALID.getResultMessage(), badParameter));
        }

        log.info(errorResponse.toString());
        if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prd")
                        || env.equalsIgnoreCase("stg")) )) {
            Sentry.captureException(e);
//            rollbar.error(e);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> maxFileSizeOverException(MaxUploadSizeExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(RCode.MAX_FILE_SIZE.getResultCode());
        errorResponse.setErrorMessage(String.format(RCode.MAX_FILE_SIZE.getResultMessage(), "test"));
        log.info(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handlerMissingParameter(MissingServletRequestParameterException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        String parameter = e.getParameterName();
        errorResponse.setErrorCode(RCode.PARAMETER_REQUIRED.getResultCode());
        errorResponse.setErrorMessage(String.format(RCode.PARAMETER_REQUIRED.getResultMessage(), parameter));
        log.info(errorResponse.toString());
        if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prd")
                        || env.equalsIgnoreCase("stg")) )) {
            Sentry.captureException(e);
//            rollbar.error(e);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleNotAllowException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorCode(RCode.HTTP_METHOD_NOT_ALLOWED.getResultCode());
        errorResponse.setErrorMessage(RCode.HTTP_METHOD_NOT_ALLOWED.getResultMessage());
        if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prd")
                        || env.equalsIgnoreCase("stg")) )) {
            Sentry.captureException(exception);
//            rollbar.error(exception);
        }
        return new ResponseEntity<>(errorResponse, RCode.HTTP_METHOD_NOT_ALLOWED.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorCode(RCode.JSON_PARSE_ERROR.getResultCode());
        errorResponse.setErrorMessage(RCode.JSON_PARSE_ERROR.getResultMessage());
        log.info(errorResponse.toString());
        if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prd")
                        || env.equalsIgnoreCase("stg")) )) {
            Sentry.captureException(exception);
//            rollbar.error(exception);
        }
        return new ResponseEntity<>(errorResponse, RCode.JSON_PARSE_ERROR.getHttpStatus());
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHandleMediaTypeNotSupportedException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorCode(RCode.MEDIA_TYPE_NOT_SUPPORTED.getResultCode());
        errorResponse.setErrorMessage(RCode.MEDIA_TYPE_NOT_SUPPORTED.getResultMessage());
        log.info(errorResponse.toString());
        if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prd")
                        || env.equalsIgnoreCase("stg")) )) {
            Sentry.captureException(exception);
//            rollbar.error(exception);
        }
        return new ResponseEntity<>(errorResponse, RCode.MEDIA_TYPE_NOT_SUPPORTED.getHttpStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorCode(RCode.INTERNAL_SERVER_ERROR.getResultCode());
        errorResponse.setErrorMessage(RCode.INTERNAL_SERVER_ERROR.getResultMessage());

        log.info(errorResponse.toString());


        if(exception.getMessage() != null) {
            log.info(exception.getMessage());
        }

        log.info(ExceptionUtils.getStackTrace(exception));
        if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prd")
                        || env.equalsIgnoreCase("stg")) )) {
            Sentry.captureException(exception, ExceptionUtils.getStackTrace(exception));
//            rollbar.error(exception);
        }

        return new ResponseEntity<>(errorResponse, RCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}