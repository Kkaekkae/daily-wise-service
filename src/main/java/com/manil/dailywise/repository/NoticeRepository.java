package com.manil.dailywise.repository;

import com.manil.dailywise.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice findFirstByEndAtGreaterThanEqualAndStartAtLessThanEqualOrderByCreatedAtDesc(Long currentTime1,Long currentTime2);
}
