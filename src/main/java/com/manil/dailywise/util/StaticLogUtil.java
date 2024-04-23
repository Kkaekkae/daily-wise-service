package com.manil.dailywise.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StaticLogUtil {
    public static void insertStaticLog(StaticLogType type){
        long currentTimeMillis = System.currentTimeMillis();
        log.info(
                "{ " +
                    "\"type\" : \"" + type.name() + "\", " +
                    "\"createdAt\" : " + currentTimeMillis + ", " +
                    "\"count\" : 1 " +
                "} ");

    }
}
