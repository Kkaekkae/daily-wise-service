package com.manil.dailywise.auth;

import com.manil.dailywise.entity.User;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.repository.UserRepository;
import com.manil.dailywise.dto.common.RCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        System.out.println(id);
        User user = userRepository.findByUniqId(id).orElseThrow(
                () -> new KkeaException(RCode.USER_NOT_EXISTS)
        );

        return UserPrincipal.create(user);
    }
}
