package com.manil.dailywise.controller;

import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.repository.UserRepository;
import com.manil.dailywise.repository.VersionRepository;
import com.manil.dailywise.config.LoginProperty;
import com.manil.dailywise.entity.Version;
import com.manil.dailywise.dto.version.VersionReqDTO;
import com.manil.dailywise.dto.common.ErrorResponse;
import com.manil.dailywise.dto.common.SingleItemResponse;
import com.manil.dailywise.dto.version.VersionRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/version" )
@Slf4j
public class VersionController {
    UserRepository userRepository;
    LoginProperty loginProperty;
    VersionRepository versionRepository;

    @Autowired
    public void setVersionRepository(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setLoginProperty(LoginProperty loginProperty) {
        this.loginProperty = loginProperty;
    }

    @PostMapping
    public ResponseEntity<SingleItemResponse<String>> getVersionResponse(@RequestBody VersionReqDTO versionReqVO) throws KkeaException {
        String response = "pass";

        Version newestVersion = versionRepository.findFirstByIsPublicOrderByCreatedAtDesc(true);
        String[] userVersion = versionReqVO.getVersion().split("\\.");
        String[] newestVersionArr = newestVersion.getVersion().split("\\.");

        if(userVersion.length < 2){
            response = "recommand";
        }else{
            if(Integer.parseInt(userVersion[1]) < Integer.parseInt(newestVersionArr[1])){
                response = "recommand";
            }
        }
        return ResponseEntity.ok(SingleItemResponse.create(response));
    }

    @GetMapping("/newest/register")
    public ResponseEntity<SingleItemResponse<ErrorResponse.EmptyJsonBody>> patchNewestVersion(
            @RequestParam String newestVersion) throws KkeaException {
        Version version = new Version();
        Version oldVersion = versionRepository.findByVersion(newestVersion);
        if(oldVersion != null){
            version.setId(oldVersion.getId());
            version.setCreatedAt(oldVersion.getCreatedAt());
        }else{
            version.setCreatedAt(LocalDateTime.now());
        }

        version.setIsPublic(true);
        version.setVersion(newestVersion);
        versionRepository.save(version);
        return ResponseEntity.ok(SingleItemResponse.create(new ErrorResponse.EmptyJsonBody()));
    }

    @GetMapping("/newest")
    public ResponseEntity<SingleItemResponse<String>> getNewestVersion() throws KkeaException {
        Version version = versionRepository.findFirstByIsPublicOrderByCreatedAtDesc(true);

        return ResponseEntity.ok(SingleItemResponse.create(version.getVersion()));
    }
    @GetMapping("/newestWithText")
    public ResponseEntity<SingleItemResponse<VersionRespDTO>> getNewestVersionWithText() throws KkeaException {
        Version version = versionRepository.findFirstByIsPublicOrderByCreatedAtDesc(true);

        return ResponseEntity.ok(SingleItemResponse.create(VersionRespDTO.from(version)));
    }
}
