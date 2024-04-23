package com.manil.dailywise.dto.user;

import com.manil.dailywise.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyInfo {
    String id;
    String email;
    String name;

    public static MyInfo from(User u) {
        return MyInfo.builder()
                .email(u.getEmail())
                .name(u.getName())
                .id(u.getUniqId())
                .build();
    }
}
