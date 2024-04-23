package com.manil.dailywise.dto.writer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Slf4j
public class AddWriterDTO {
    @NotNull
    @ApiModelProperty(required = true)
    String name;
    Long bornedAt;
    Long deathedAt;
}
