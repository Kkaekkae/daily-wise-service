package com.manil.dailywise.service;

import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.wise.WiseDetailRespDTO;
import com.manil.dailywise.entity.TodayWise;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.entity.Wise;
import com.manil.dailywise.entity.Writer;
import com.manil.dailywise.enums.common.SortValue;
import com.manil.dailywise.enums.wise.WiseListSortType;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.repository.LikeRepository;
import com.manil.dailywise.repository.TodayWiseRepository;
import com.manil.dailywise.repository.UserRepository;
import com.manil.dailywise.repository.WiseRepository;
import com.manil.dailywise.dto.common.RCode;
import com.manil.dailywise.dto.wise.AddWiseDTO;
import com.manil.dailywise.dto.wise.WiseRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WiseService {
    private WiseRepository wiseRepository;
    private UserRepository userRepository;
    private LikeRepository likeRepository;
    private TodayWiseRepository todayWiseRepository;

    @Autowired
    public void setLikeRepository(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Autowired
    public void setWiseRepository(WiseRepository wiseRepository) {
        this.wiseRepository = wiseRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTodayWiseRepository(TodayWiseRepository todayWiseRepository) {
        this.todayWiseRepository = todayWiseRepository;
    }

    public List<WiseRespDTO> getWises(String search, UserPrincipal currentUser) throws KkeaException {
        try{
            User nowUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
            List<WiseRespDTO> response = new ArrayList<>();
            List<Wise> wises = wiseRepository.findAllByOrderByCreatedAt();

            wises.forEach(wise -> {
                if(wise != null) {
                    Boolean isLike = likeRepository.existsByFromAndTo(nowUser, wise);
                    User user = userRepository.findByUniqId(wise.getWriterUniqId()).orElse(null);
                    response.add(WiseRespDTO.from(wise, user, isLike));
                }
            });
            return response;
        } catch (KkeaException e) {
            throw e;
        }
    }


    public List<WiseRespDTO> getWises4User(String userUniqId, WiseListSortType sortType, SortValue sortValue, UserPrincipal currentUser) throws KkeaException {
        try{
            User target = userRepository.findByUniqId(userUniqId).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
            User nowUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
            List<WiseRespDTO> response = new ArrayList<>();
            List<Wise> wises;

            if(sortType == WiseListSortType.LIKE){
                wises = wiseRepository.findAllByWriterUniqIdOrderByLikesDesc(userUniqId);
            }else{
                Sort sort = Sort.by("createdAt");
                sort = sortValue == SortValue.DESC ? sort.descending() : sort.ascending();
                wises = wiseRepository.findAllByWriterUniqId(userUniqId, sort);
            }

            wises.forEach(wise -> {
                Boolean isLike = likeRepository.existsByFromAndTo(nowUser,wise);
                User user = userRepository.findByUniqId(wise.getWriterUniqId()).orElse(null);
                response.add(WiseRespDTO.from(wise, user, isLike));
            });
            return response;
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    public WiseDetailRespDTO getWiseWithId(String wiseId, UserPrincipal currentUser){
        User nowUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
        Wise wise = wiseRepository.findById(Long.parseLong(wiseId)).orElseThrow(() -> new KkeaException(RCode.WISE_NOT_EXISTS));
        User writer = userRepository.findByUniqId(wise.getWriterUniqId()).orElseGet(User::anonymous);
        return WiseDetailRespDTO.from(wise, nowUser, writer);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private Wise getRandomWise(){
        long wiseLength = wiseRepository.count();
        int index = getRandomNumber(0, Long.valueOf(wiseLength - 1).intValue());
        Wise newWise = wiseRepository.findByOffset(index);
        return newWise;
    }

    public Wise updateTodayWise(Wise w, User u){
        TodayWise todayWise = new TodayWise();
        todayWise.setUserId(u.getId());
        todayWise.setWiseId(w.getId());
        todayWiseRepository.save(todayWise);
        return w;
    }

    @Transactional
    public void updateTodayWiseSchedule() throws KkeaException {
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            String nowDate = formatter.format(date);

            List<User> users = userRepository.findAllByUpdateWiseTime(nowDate);
//            List<User> users = userRepository.findAll();

            users.forEach(user -> {
                TodayWise oldTodayWise = todayWiseRepository.findByUserId(user.getId());
                Wise newWise = getRandomWise();
                if(oldTodayWise != null){
                    Wise oldWise = wiseRepository.findById(oldTodayWise.getWiseId()).orElseThrow(() ->new KkeaException(RCode.WISE_NOT_EXISTS));
                    // 초기 while 통과를 위해 동일하게 설정
                    newWise = oldWise;

                    // 다른 명언이 나올때까지 랜덤한 명언을 불러옴
                    while(oldWise == newWise){
                        newWise = getRandomWise();
                    }

                    todayWiseRepository.delete(oldTodayWise);
                }

                TodayWise todayWise = new TodayWise();
                todayWise.setWiseId(newWise.getId());
                todayWise.setUserId(user.getId());
                todayWiseRepository.save(todayWise);
            });

        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    public WiseRespDTO getTodayWise(UserPrincipal currentUser) throws KkeaException {
        try{

            User user = userRepository.findById(currentUser.getId()).orElseThrow(() ->new KkeaException(RCode.USER_NOT_EXISTS));
            TodayWise todayWise = todayWiseRepository.findByUserId(user.getId());
            Wise wise = null;
            if(todayWise == null) {
                Wise newWise = getRandomWise();
                wise = updateTodayWise(newWise, user);
            }else{
                wise = wiseRepository.findById(todayWise.getWiseId()).orElseThrow(() ->new KkeaException(RCode.WISE_NOT_EXISTS));
            }

            return WiseRespDTO.from(wise,user);

        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    public WiseRespDTO addWise(AddWiseDTO dto) throws KkeaException {
        try{
            User user = userRepository.findById(dto.getWriterId()).orElseThrow(() -> new KkeaException(RCode.WRITER_NOT_EXISTS));

            Wise wise = new Wise();
            wise.setTitle(dto.getTitle());
            wise.setWriterUniqId(dto.getWriterId());
            wise.setCreatedAt(LocalDateTime.MIN);

            wiseRepository.save(wise);

            return WiseRespDTO.from(wise, user);

         } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }
}
