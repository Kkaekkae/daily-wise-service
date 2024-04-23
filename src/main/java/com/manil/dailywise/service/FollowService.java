package com.manil.dailywise.service;

import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.common.RCode;
import com.manil.dailywise.dto.follow.FollowRespDTO;
import com.manil.dailywise.entity.Follow;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.repository.FollowRepository;
import com.manil.dailywise.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FollowService {
    private UserRepository userRepository;
    private FollowRepository followRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setFollowRepository(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public FollowRespDTO follow(String userUniqId, UserPrincipal currentUser){
        try{
           User user = userRepository.findByUniqId(userUniqId).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
           User toUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
           Boolean isFollowing = followRepository.existsByFromAndTo(user, toUser);

           if(isFollowing){
                Follow target = followRepository.findByFromAndTo(user, toUser);
                followRepository.delete(target);
                return FollowRespDTO.from(false);
           }else{

                Follow target = new Follow(user,toUser);
                followRepository.save(target);
               return FollowRespDTO.from(true);
           }
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }
}
