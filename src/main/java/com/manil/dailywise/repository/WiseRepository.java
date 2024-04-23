package com.manil.dailywise.repository;

import com.manil.dailywise.entity.Notice;
import com.manil.dailywise.entity.Wise;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WiseRepository extends JpaRepository<Wise, Long> {
    List<Wise> findAllByOrderByCreatedAt();
    List<Wise> findAllByWriterUniqId(String writer, Sort sort);
    List<Wise> findAllByWriterUniqIdOrderByLikesDesc(String writer);
    @Query(value = "SELECT * FROM wise limit :offset, 1", nativeQuery = true)
    Wise findByOffset(int offset);
    @Query(value = "SELECT w.* " +
            "FROM wise w " +
            "LEFT JOIN wise_like wl ON w.id = wl.like_to_id " +
            "INNER JOIN users u ON w.writer = u.uniq_id " +
            "LEFT JOIN follow f ON u.id = f.follow_to_id " +
            "WHERE w.title LIKE %:search% " +
            "GROUP BY w.id " +
            "ORDER BY u.is_writer DESC, u.is_master_piece DESC, count(wl.id) DESC, count(f.id) DESC"
            , nativeQuery = true
    )
    List<Wise> getWises(String search);
}
