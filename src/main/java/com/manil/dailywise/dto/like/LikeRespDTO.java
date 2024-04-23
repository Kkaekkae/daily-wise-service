package com.manil.dailywise.dto.like;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeRespDTO {
    Boolean isLike;

    public static LikeRespDTO from(Boolean isLike){
        return LikeRespDTO.builder().isLike(isLike).build();
    }
}
