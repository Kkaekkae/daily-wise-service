package com.manil.dailywise.dto.wise;

import com.manil.dailywise.entity.User;
import com.manil.dailywise.entity.Wise;
import com.manil.dailywise.entity.Writer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Builder
@Slf4j
public class WiseRespDTO {
    Long id;
    String title;
    String writer;
    Boolean isLike;
    Long likeCount;
    LocalDateTime createdAt;

    public static WiseRespDTO from(Wise w, User u){
        if( w == null ){
            return WiseRespDTO.builder().build();
        }

        return WiseRespDTO.builder()
                .id(w.getId())
                .title(w.getTitle())
                .writer(u.getNickname())
                .likeCount((long) w.getLikes().size())
                .isLike(u.getLikes().stream().anyMatch(like -> like.getTo() == w))
                .createdAt(w.getCreatedAt())
                .build();
    }

    public static WiseRespDTO from(Wise w, User u,Boolean isLike){
        return WiseRespDTO.builder()
                .id(w.getId())
                .title(w.getTitle())
                .writer(u.getNickname())
                .likeCount((long) w.getLikes().size())
                .isLike(isLike)
                .createdAt(w.getCreatedAt())
                .build();
    }
}
