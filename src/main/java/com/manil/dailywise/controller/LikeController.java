package com.manil.dailywise.controller;

import com.manil.dailywise.auth.CurrentUser;
import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.common.SingleItemResponse;
import com.manil.dailywise.dto.like.LikeReqDTO;
import com.manil.dailywise.dto.like.LikeRespDTO;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/like" )
@Slf4j
public class LikeController {
    private LikeService likeService;

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    @ApiOperation(value = "좋아요", notes = "해당 명언을 좋아요 합니다. response 에는 업데이트 이후 상태를 줍니다")
    public ResponseEntity<SingleItemResponse<LikeRespDTO>> follow(
            @RequestBody LikeReqDTO dto,
            @CurrentUser UserPrincipal currentUser
        ) throws KkeaException {
        LikeRespDTO response = likeService.like(dto.getTargetId(), currentUser);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }
}
