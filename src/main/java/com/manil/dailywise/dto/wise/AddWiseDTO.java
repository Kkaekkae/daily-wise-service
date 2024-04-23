package com.manil.dailywise.dto.wise;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Slf4j
public class AddWiseDTO {

    @NotNull
    @ApiModelProperty(required = true)
    String title;
    @NotNull
    @ApiModelProperty(required = true)
    String writerId;
}
