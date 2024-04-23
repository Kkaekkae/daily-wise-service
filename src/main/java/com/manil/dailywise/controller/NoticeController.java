package com.manil.dailywise.controller;

import com.manil.dailywise.auth.CurrentUser;
import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.service.NoticeService;
import com.manil.dailywise.dto.common.SingleItemResponse;
import com.manil.dailywise.dto.notice.NoticeRespDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/notice" )
@Slf4j
public class NoticeController {
    private NoticeService noticeService;

    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

   @ApiOperation(value = "공지 불러오기", notes = "현재 활성화 된 공지를 가져옵니다.")
   @GetMapping
   public ResponseEntity<SingleItemResponse<NoticeRespDTO>> getNotice(
       @CurrentUser UserPrincipal currentUser
   ) throws KkeaException {
       NoticeRespDTO response = noticeService.getNotice();
       return ResponseEntity.ok(SingleItemResponse.create(response));
   }
}
