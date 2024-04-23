package com.manil.dailywise.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class CreateTokenReqDTO {
    @Email
    String email;
}
