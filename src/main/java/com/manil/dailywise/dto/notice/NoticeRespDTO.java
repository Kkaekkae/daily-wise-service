package com.manil.dailywise.dto.notice;

import com.manil.dailywise.entity.Notice;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Builder
@Slf4j
public class NoticeRespDTO {
    Long id;
    String title;
    String description;
    LocalDateTime createdAt;

    public static NoticeRespDTO from(Notice n){
        if( n == null ){
            return NoticeRespDTO.builder().build();
        }

        return NoticeRespDTO.builder()
                .id(n.getId())
                .title(n.getTitle())
                .description(n.getDescription())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
