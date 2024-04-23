package com.manil.dailywise.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class UserReqDTO {

    String uniqId;
}
