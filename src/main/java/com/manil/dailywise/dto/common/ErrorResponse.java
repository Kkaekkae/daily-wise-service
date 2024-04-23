package com.manil.dailywise.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends BaseResponse {
    @ApiModelProperty(value = "Result Code", position = 1)
    protected int errorCode;
    @ApiModelProperty(value = "Result Message", position = 2)
    protected String errorMessage;
    EmptyJsonBody data = new EmptyJsonBody();

    @JsonSerialize
    public static class EmptyJsonBody {
        @Override
        public String toString() {
            return "{ }";
        }
    }
}
