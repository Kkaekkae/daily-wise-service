package com.manil.dailywise.repository;

import com.manil.dailywise.entity.TodayWise;
import com.manil.dailywise.entity.Wise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodayWiseRepository extends JpaRepository<TodayWise, Long> {
    TodayWise findByUserId(String userId);
}
