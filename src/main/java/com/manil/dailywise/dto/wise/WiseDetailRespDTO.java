package com.manil.dailywise.dto.wise;

import com.manil.dailywise.dto.comment.CommentRespDTO;
import com.manil.dailywise.entity.Comment;
import com.manil.dailywise.entity.Like;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.entity.Wise;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Slf4j
public class WiseDetailRespDTO {
    Long id;
    String title;
    String writer;
    Boolean isLike;
    List<CommentRespDTO> comments;
    Long likeCount;
    LocalDateTime createdAt;

    public static WiseDetailRespDTO from(Wise w, User u, User writer){
        if( w == null ){
            return WiseDetailRespDTO.builder().build();
        }
        return WiseDetailRespDTO.builder()
                .id(w.getId())
                .title(w.getTitle())
                .writer(writer.getNickname())
                .likeCount((long) w.getLikes().size())
                .comments(w.getComments().stream().map(CommentRespDTO::create).collect(Collectors.toList()))
                .isLike(u.getLikes().stream().anyMatch(like -> like.getTo() == w))
                .createdAt(w.getCreatedAt())
                .build();
    }

    public static WiseDetailRespDTO from(Wise w, User u, Boolean isLike){
        return WiseDetailRespDTO.builder()
                .id(w.getId())
                .title(w.getTitle())
                .writer(u.getNickname())
                .likeCount(w.getLikes().stream().count())
                .isLike(isLike)
                .createdAt(w.getCreatedAt())
                .build();
    }
}
