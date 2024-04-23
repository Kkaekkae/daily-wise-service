package com.manil.dailywise.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "writer",
        indexes = {@Index(columnList="created_at")})
@Data
public class Writer {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    @Column(name="name")
    private String name;
    @Column(name="borned_at")
    private Long bornedAt;
    @Column(name="deathed_at")
    private Long deathedAt;
    @Column(name="created_at")
    private Long createdAt;

}
