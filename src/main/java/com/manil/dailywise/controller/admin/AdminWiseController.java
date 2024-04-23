package com.manil.dailywise.controller.admin;

import com.manil.dailywise.auth.CurrentUser;
import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.service.WiseService;
import com.manil.dailywise.dto.common.SingleItemResponse;
import com.manil.dailywise.dto.wise.AddWiseDTO;
import com.manil.dailywise.dto.wise.WiseRespDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/wise" )
@Slf4j
public class AdminWiseController {
    private WiseService wiseService;

    @Autowired
    public void setWiseService(WiseService wiseService) {
        this.wiseService = wiseService;
    }

    @ApiOperation(value = "명언 추가하기", notes = "관리자가 명언을 추가합니다")
    @PostMapping(value = "add")
    public ResponseEntity<SingleItemResponse<WiseRespDTO>> addWise(
            @RequestBody AddWiseDTO dto,
            @CurrentUser UserPrincipal currentUser
    ) throws KkeaException {
        WiseRespDTO response = wiseService.addWise(dto);
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }
}
