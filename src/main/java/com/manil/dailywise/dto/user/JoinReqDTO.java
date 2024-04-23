package com.manil.dailywise.dto.user;

import com.manil.dailywise.enums.user.SnsType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class JoinReqDTO {
    @NotNull
    String userUniqId;
    Boolean agreePush;
    @NotNull
    String nickname;
    @NotNull
    SnsType snsType;
    @NotNull
    String snsToken;
    String profileImage;
}
