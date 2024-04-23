package com.manil.dailywise.controller;

import com.manil.dailywise.enums.user.SnsType;
import com.manil.dailywise.repository.UserRepository;
import com.manil.dailywise.config.LoginProperty;
import com.manil.dailywise.entity.AuthProvider;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.util.UidUtil;
import com.manil.dailywise.dto.CreateTokenReqDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping(value = "/token" )
@Profile({"default", "stg", "build","dev"})
public class TokenController {
    UserRepository userRepository;
    LoginProperty loginProperty;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setLoginProperty(LoginProperty loginProperty) {
        this.loginProperty = loginProperty;
    }
}
