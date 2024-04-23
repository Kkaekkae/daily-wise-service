package com.manil.dailywise.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment",
        indexes = {@Index(columnList="wise_to_id")})
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    @Column(name="text")
    private String text;

    @ManyToOne
    @JoinColumn(name="comment_from_id")
    private User from;

    @ManyToOne
    @JoinColumn(name="wise_to_id")
    private Wise to;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}
