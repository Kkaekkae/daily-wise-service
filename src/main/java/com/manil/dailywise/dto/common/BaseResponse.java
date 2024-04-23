package com.manil.dailywise.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class BaseResponse {
    @ApiModelProperty(value = "Result Code", position = 1)
    protected int errorCode;
    @ApiModelProperty(value = "Result Message", position = 2)
    protected String errorMessage;
    public static final BaseResponse OK = new BaseResponse();

    static {
        OK.errorCode = RCode.OK.getResultCode();
        OK.errorMessage = RCode.OK.getResultMessage();
    }
}