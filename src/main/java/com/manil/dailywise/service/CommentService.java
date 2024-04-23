package com.manil.dailywise.service;

import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.comment.AddCommentReqDTO;
import com.manil.dailywise.dto.common.RCode;
import com.manil.dailywise.dto.common.SuccessfullyRespDTO;
import com.manil.dailywise.entity.Comment;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.entity.Wise;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.repository.CommentRepository;
import com.manil.dailywise.repository.UserRepository;
import com.manil.dailywise.repository.WiseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class CommentService {
    private WiseRepository wiseRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setWiseRepository(WiseRepository wiseRepository) {
        this.wiseRepository = wiseRepository;
    }

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    public SuccessfullyRespDTO addComment(AddCommentReqDTO dto, UserPrincipal currentUser) {
        try {
            User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
            Wise wise = wiseRepository.findById(dto.getWiseId()).orElseThrow(() -> new KkeaException(RCode.WISE_NOT_EXISTS));

            Comment comment = new Comment();
            comment.setFrom(user);
            comment.setTo(wise);
            comment.setText(dto.getText());
            comment.setCreatedAt(LocalDateTime.now());

            commentRepository.save(comment);

            return new SuccessfullyRespDTO(true);
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }


    }
}
