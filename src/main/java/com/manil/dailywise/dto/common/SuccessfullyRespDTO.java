package com.manil.dailywise.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
public class SuccessfullyRespDTO {
    private Boolean success;

    @Builder
    public SuccessfullyRespDTO(Boolean success){
        this.success = success;
    }

}
