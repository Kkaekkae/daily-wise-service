package com.manil.dailywise.dto.file;

import com.manil.dailywise.entity.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {
    private Long id;
    private String filePath;
    private String mimeType;
    private String name;
    private Long size;
    private LocalDateTime createdAt;

    public static FileDTO of(FileUpload f) {
        return FileDTO.builder()
                .id(f.getId())
                .filePath(f.getFilePath())
                .mimeType(f.getMimeType())
                .name(f.getName())
                .size(f.getSize())
                .createdAt(f.getCreatedAt())
                .build();
    }
}
