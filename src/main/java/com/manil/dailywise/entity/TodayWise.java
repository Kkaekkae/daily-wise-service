package com.manil.dailywise.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "today_wise",
        indexes = {@Index(columnList="user_id")})
@Data
public class TodayWise {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    @Column(name="wise_id")
    private Long wiseId;
    @Column(name="user_id",unique = true)
    private String userId;

}
