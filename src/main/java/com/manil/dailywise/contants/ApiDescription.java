package com.manil.dailywise.contants;

import org.springframework.http.HttpStatus;

public enum ApiDescription {
    CREATE_CHANNEL("create channel", "채널을 생성하는 API, imageUrl은 'channels/preparedProfileImages'을 통해 얻어온 리스트중에서 선택");

    private final String name;
    private final String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    ApiDescription(String name, String description) {
        this.name = name;
        this.description = description;
    }


}
