package com.manil.dailywise.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "wise",
        indexes = {@Index(columnList="created_at")})
@Data
public class Wise {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    @Column(name="title")
    private String title;
    @Column(name="writer")
    private String writerUniqId;

    @OneToMany(mappedBy="to")
    private List<Like> likes;

    @OneToMany(mappedBy="to")
    private List<Comment> comments;

    @Column(name="created_at")
    private LocalDateTime createdAt;

}
