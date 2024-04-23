package com.manil.dailywise.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file",
        indexes = {@Index(columnList="created_at")})
@Data
public class FileUpload {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    private String name;
    private String filePath;
    private String mimeType;
    private Long size;
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
