package com.manil.dailywise.repository;

import com.manil.dailywise.entity.Comment;
import com.manil.dailywise.entity.Wise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByToOrderByCreatedAtDesc(Wise to);
}
