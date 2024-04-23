package com.manil.dailywise.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "version",
        indexes = {@Index(columnList="version")})
@Data
public class Version {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="text")
    private String text;

    @Column(name="version")
    String version;

    @Column(name="is_public")
    @ColumnDefault(value="false")
    Boolean isPublic;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}
