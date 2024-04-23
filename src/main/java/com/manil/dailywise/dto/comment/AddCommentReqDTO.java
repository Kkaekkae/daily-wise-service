package com.manil.dailywise.dto.comment;

import lombok.Data;

@Data
public class AddCommentReqDTO {
    private Long wiseId;
    private String text;
}
