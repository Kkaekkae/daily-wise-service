package com.manil.dailywise.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "wise_like")
@Data
public class Like {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="like_from_id")
    private User from;

    @ManyToOne
    @JoinColumn(name="like_to_id")
    private Wise to;

    public Like() {}

    @Builder
    public Like(User from, Wise to){
        this.from = from;
        this.to = to;
    }
}
