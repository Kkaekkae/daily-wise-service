package com.manil.dailywise.dto.comment;

import com.manil.dailywise.entity.Comment;
import com.manil.dailywise.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentRespDTO {
    private String text;
    private String profileImage;
    private String nickname;
    private LocalDateTime createdAt;

    public static CommentRespDTO create(Comment c){
        User u = c.getFrom();

        return CommentRespDTO.builder()
                .profileImage(u.getProfileImage())
                .text(c.getText())
                .nickname(u.getNickname())
                .createdAt(c.getCreatedAt()).build();
    }
}
