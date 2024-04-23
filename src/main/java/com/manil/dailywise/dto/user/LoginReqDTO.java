package com.manil.dailywise.dto.user;

import com.manil.dailywise.enums.user.SnsType;
import lombok.Data;

@Data
public class LoginReqDTO {
    String snsToken;
    SnsType snsType;
}
