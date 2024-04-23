package com.manil.dailywise.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListItemResponse<T> extends BaseResponse {
    Long total;
    Integer offset;
    Boolean hasMore = false;
    List<T> data;

    public static <T> ListItemResponse<T> create(List<T> data, PageableParam pageableParam) {
        ListItemResponse response = new ListItemResponse();
        response.errorCode = RCode.OK.getResultCode();
        response.errorMessage = RCode.OK.getResultMessage();
        response.data = data;
        response.total = pageableParam.getCount();
        response.offset = pageableParam.getOffset();

        if(data.size() > pageableParam.limit) {
            response.hasMore = true;
            data.remove(data.size()-1);
        }


        return response;
    }

    public static <T> ListItemResponse<T> create(List<T> list) {
        ListItemResponse response = new ListItemResponse();
        response.errorCode = RCode.OK.getResultCode();
        response.errorMessage = RCode.OK.getResultMessage();
        response.data = list;
        return response;
    }
}