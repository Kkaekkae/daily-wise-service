package com.manil.dailywise.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manil.dailywise.dto.common.BaseResponse;
import com.manil.dailywise.dto.common.ListItemResponse;
import com.manil.dailywise.dto.common.SingleItemResponse;

public class TestUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String convertObjectToString(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }
    public static <T> T  convertStringToBaseResponse(String string, Class<? extends BaseResponse> myClass) throws JsonProcessingException{
        return (T) MAPPER.readValue(string, myClass);
    }

    public static <T> T  convertStringToErrorResponse(String string, Class<? extends BaseResponse> myClass) throws JsonProcessingException{
        return (T) MAPPER.readValue(string, myClass);
    }
    public static <T> T  convertObjectToErrorResponse(String string, Class<? extends BaseResponse> myClass) throws JsonProcessingException{
        return (T) MAPPER.readValue(string, myClass);
    }

    public static <T> SingleItemResponse<T> convertStringToSingleItem(String str, TypeReference<SingleItemResponse<T>> typeReference) throws JsonProcessingException{
        return  MAPPER.readValue(str, typeReference);
    }

    public static <T> ListItemResponse<T> convertStringToListItem(String str, TypeReference<ListItemResponse<T>> typeReference) throws JsonProcessingException{
        return  MAPPER.readValue(str, typeReference);
    }

    public static <T> T convertStringToObject(String str, Class myClass) throws Exception {
        return (T) MAPPER.readValue(str, myClass);
    }
}
