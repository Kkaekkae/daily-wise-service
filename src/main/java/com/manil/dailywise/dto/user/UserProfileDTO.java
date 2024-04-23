package com.manil.dailywise.dto.user;

import com.manil.dailywise.dto.wise.WiseRespDTO;
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
public class UserProfileDTO {
    String uniqId;
    String nickname;
    String profileImage;
    Boolean isWriter;
    Boolean isMasterPiece;
    String description;
    Long followCount;
    Long followingCount;
    Long likeCount;
    Boolean isFollow;
    String time;
    Boolean isPush;
    LocalDateTime bornedAt;
    LocalDateTime deathedAt;
    LocalDateTime createdAt;

    public static UserProfileDTO from(User u){
        return UserProfileDTO.builder()
                .uniqId(u.getUniqId())
                .nickname(u.getNickname())
                .profileImage(u.getProfileImage())
                .isWriter(u.getIsWriter())
                .isMasterPiece(u.getIsMasterPiece())
                .followCount(u.getFollowers().stream().count())
                .followingCount(u.getFollowing().stream().count())
                .likeCount(u.getLikes().stream().count())
                .isFollow(false)
                .time(u.getUpdateWiseTime())
                .isPush(u.getAgreePush())
                .bornedAt(u.getBornedAt())
                .deathedAt(u.getDeathedAt())
                .createdAt(u.getCreatedAt()).build();
    }

    public static UserProfileDTO from(User u, Boolean isFollow){
        return UserProfileDTO.builder()
                .uniqId(u.getUniqId())
                .nickname(u.getNickname())
                .profileImage(u.getProfileImage())
                .isWriter(u.getIsWriter())
                .isMasterPiece(u.getIsMasterPiece())
                .followCount(u.getFollowers().stream().count())
                .followingCount(u.getFollowing().stream().count())
                .likeCount(u.getLikes().stream().count())
                .isFollow(isFollow)
                .time(u.getUpdateWiseTime())
                .isPush(u.getAgreePush())
                .bornedAt(u.getBornedAt())
                .deathedAt(u.getDeathedAt())
                .createdAt(u.getCreatedAt()).build();
    }
}
