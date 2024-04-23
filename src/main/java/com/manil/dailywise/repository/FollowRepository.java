package com.manil.dailywise.repository;

import com.manil.dailywise.entity.Follow;
import com.manil.dailywise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFromAndTo(User from, User to);
    Follow findByFromAndTo(User from, User to);
}
