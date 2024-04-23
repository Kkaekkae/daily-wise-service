package com.manil.dailywise.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserUpdateReqDTO {
    Boolean agreePush;
    @NotNull
    String nickname;
    String profileImage;
}
