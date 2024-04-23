package com.manil.dailywise.dto.follow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowRespDTO {
    Boolean isFollow;

    public static FollowRespDTO from(Boolean isFollow){
        return FollowRespDTO.builder().isFollow(isFollow).build();
    }
}
