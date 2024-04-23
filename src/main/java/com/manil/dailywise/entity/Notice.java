package com.manil.dailywise.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    @Column(name="title")
    private String title;
    @Column(name="description")
    private String description;
    @Column(name="start_at")
    private LocalDateTime startAt;
    @Column(name="end_at")
    private LocalDateTime endAt;
    @Column(name="created_at")
    private LocalDateTime createdAt;

}
