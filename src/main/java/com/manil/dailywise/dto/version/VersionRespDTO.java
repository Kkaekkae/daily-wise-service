package com.manil.dailywise.dto.version;

import com.manil.dailywise.entity.Version;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VersionRespDTO {
    String version;
    String text;

    public static VersionRespDTO from(Version v) {
        return VersionRespDTO.builder()
                .version(v.getVersion())
                .text(v.getText())
                .build();
    }
}
