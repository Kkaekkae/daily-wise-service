package com.manil.dailywise.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleItemResponse<T> extends BaseResponse{

    @ApiModelProperty(value = "Content Data", position = 3)
    private T data;

    private SingleItemResponse() {
    }

    public static <T> SingleItemResponse<T> create(T data) {
        SingleItemResponse response = new SingleItemResponse();
        response.errorCode = RCode.OK.getResultCode();
        response.errorMessage = RCode.OK.getResultMessage();
        response.data = data;
        return response;
    }

    public static <T> SingleItemResponse<T> create(RCode resultCode, T data) {
        SingleItemResponse response = new SingleItemResponse();
        response.errorCode = resultCode.getResultCode();
        response.errorMessage = resultCode.getResultMessage();
        response.data = data;
        return response;
    }

}