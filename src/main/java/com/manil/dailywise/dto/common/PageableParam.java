package com.manil.dailywise.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageableParam {
    Integer offset = 0;
    Integer limit = 1000;
    @ApiModelProperty(hidden = true)
    Long count;
}
