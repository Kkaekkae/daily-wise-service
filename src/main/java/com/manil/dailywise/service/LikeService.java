package com.manil.dailywise.service;

import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.common.RCode;
import com.manil.dailywise.dto.like.LikeRespDTO;
import com.manil.dailywise.entity.Like;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.entity.Wise;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.repository.LikeRepository;
import com.manil.dailywise.repository.UserRepository;
import com.manil.dailywise.repository.WiseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikeService {
    private UserRepository userRepository;
        private LikeRepository likeRepository;
        private WiseRepository wiseRepository;

    @Autowired
    public void setWiseRepository(WiseRepository wiseRepository) {
        this.wiseRepository = wiseRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setLikeRepository(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public LikeRespDTO like(String wiseId, UserPrincipal currentUser){
        try{
           Wise wise = wiseRepository.findById(Long.parseLong(wiseId)).orElseThrow(() -> new KkeaException(RCode.WISE_NOT_EXISTS));
           User fromUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
           Boolean isLike = likeRepository.existsByFromAndTo(fromUser, wise);

           if(isLike){
                Like target = likeRepository.findByFromAndTo(fromUser, wise);
                likeRepository.delete(target);
                return LikeRespDTO.from(false);
           }else{
               Like target = new Like(fromUser,wise);
               likeRepository.save(target);
               return LikeRespDTO.from(true);
           }
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }
}
