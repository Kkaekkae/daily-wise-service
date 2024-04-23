package com.manil.dailywise.repository;

import com.manil.dailywise.entity.Follow;
import com.manil.dailywise.entity.Like;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.entity.Wise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByFromAndTo(User from, Wise to);
    Like findByFromAndTo(User from, Wise to);
}
