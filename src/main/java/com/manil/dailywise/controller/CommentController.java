package com.manil.dailywise.controller;

import com.manil.dailywise.auth.CurrentUser;
import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.comment.AddCommentReqDTO;
import com.manil.dailywise.dto.common.ListItemResponse;
import com.manil.dailywise.dto.common.PageableParam;
import com.manil.dailywise.dto.common.SingleItemResponse;
import com.manil.dailywise.dto.common.SuccessfullyRespDTO;
import com.manil.dailywise.dto.wise.WiseRespDTO;
import com.manil.dailywise.enums.common.SortValue;
import com.manil.dailywise.enums.wise.WiseListSortType;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.service.CommentService;
import com.manil.dailywise.service.WiseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping(value = "/comment" )
@Slf4j
public class CommentController {
    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "댓글 등록", notes = "명언에 댓글을 추가합니다.")
    @PostMapping
    public ResponseEntity<SingleItemResponse<SuccessfullyRespDTO>> addComment(
            @RequestBody AddCommentReqDTO dto,
            @CurrentUser UserPrincipal currentUser
            ) throws KkeaException {
        SuccessfullyRespDTO response = commentService.addComment(dto, currentUser);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }
}
