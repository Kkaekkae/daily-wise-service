package com.manil.dailywise.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.dto.common.RCode;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    public static Map ObjectToMap(Object vo) throws KkeaException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String objectString = objectMapper.writeValueAsString(vo);
            Map map = objectMapper.readValue(objectString, HashMap.class);
            return map;
        }catch(Exception e){
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }
}