package com.manil.dailywise.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "follow")
@Data
public class Follow {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="follow_from_id")
    private User from;

    @ManyToOne
    @JoinColumn(name="follow_to_id")
    private User to;

    public Follow() {}

    @Builder
    public Follow(User from, User to){
        this.from = from;
        this.to = to;
    }
}
