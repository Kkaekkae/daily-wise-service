package com.manil.dailywise.service;

import com.manil.dailywise.auth.jwt.TokenProvider;
import com.manil.dailywise.dto.user.*;
import com.manil.dailywise.enums.user.SnsType;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.repository.FollowRepository;
import com.manil.dailywise.repository.LikeRepository;
import com.manil.dailywise.repository.UserRepository;
import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.entity.User;
import com.manil.dailywise.util.StaticLogType;
import com.manil.dailywise.util.StaticLogUtil;
import com.manil.dailywise.dto.common.RCode;
import com.manil.dailywise.util.UidUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private FollowRepository followRepository;
    private LikeRepository likeRepository;
    private TokenProvider tokenProvider;

    @Autowired
    public void setFollowRepository(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Autowired
    public void setLikeRepository(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public UserProfileDTO getMyInformation(String userId) throws KkeaException {
        User user = userRepository.findById(userId).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
        if (!user.getVerified()) {
            throw new KkeaException(RCode.USER_NOT_VERIFY);
        }
        return UserProfileDTO.from(user);
    }

    public List<UserProfileDTO> getUsers(String search, UserPrincipal currentUser) throws KkeaException {
        User toUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
        List<User> users = userRepository.getUsers(search);
        List<UserProfileDTO> response = new ArrayList<>();

        users.forEach(user -> {
            if (user != null) {
                Boolean isFollow = followRepository.existsByFromAndTo(user, toUser);
                response.add(UserProfileDTO.from(user, isFollow));
            }
        });

        return response;
    }

    public UserProfileDTO getUser(String userUniqId, UserPrincipal currentUser) throws KkeaException {
        User user = userRepository.findByUniqId(userUniqId).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
        User toUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
        Boolean isFollow = followRepository.existsByFromAndTo(user, toUser);
        return UserProfileDTO.from(user, isFollow);
    }

    public SnsUserInfoDTO verifyGoogleIdToken(String token) throws KkeaException {
        try {

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://oauth2.googleapis.com/tokeninfo?id_token=" + token);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse result = client.execute(httpPost);
            client.close();

            if (result.getStatusLine().getStatusCode() == 200) {
                result.getEntity().getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        result.getEntity().getContent()));

                String inputLine;
                StringBuffer buffer = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    buffer.append(inputLine);
                }


                // CONVERT RESPONSE STRING TO JSON ARRAY
                JSONObject userInfo = new JSONObject(buffer.toString());

                SnsUserInfoDTO response = new SnsUserInfoDTO();
                response.setEmail(userInfo.get("email").toString());
                response.setId(userInfo.get("sub").toString());
                response.setName(userInfo.get("name").toString());

                return response;
            } else {
                throw
                        new KkeaException(RCode.FAIL_SNS_VERIFY);
            }
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.FAIL_SNS_VERIFY, e);
        }
    }

    @Transactional
    public TokenRespDTO join(JoinReqDTO dto) throws KkeaException {
        try {
            SnsUserInfoDTO userInfo;
            if (dto.getSnsType() == SnsType.GOOGLE) {
                userInfo = verifyGoogleIdToken(dto.getSnsToken());
            } else {
                //TODO: APPLE 로 변경 예정
                userInfo = verifyGoogleIdToken(dto.getSnsToken());
            }

            User oldUser = userRepository.findByEmail(userInfo.getEmail()).orElse(null);

            if (oldUser != null) {
                throw new KkeaException(RCode.ALREADY_JOINED);
            }
//
//            User user = new User();
//
//            user.setId();UidUtil.generateUid()
//            user.setEmail(userInfo.getEmail());
//            user.setName(userInfo.getName());
//            user.setProvider(dto.getSnsType());
//            user.setProviderId(userInfo.getId());
//            user.setVerified(true);
//            user.setProfileImage(dto.getProfileImage());
//            user.setNickname(dto.getNickname());
//            user.setAgreePush(dto.getAgreePush());
//            user.setUniqId(dto.getUserUniqId());
//            user.setCreatedAt(LocalDateTime.now());
//            userRepository.save(user);

            TokenRespDTO response = new TokenRespDTO();
//            response.setToken(tokenProvider.createToken(user));

            return response;
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    @Transactional
    public TokenRespDTO login(LoginReqDTO dto) throws KkeaException {
        try {
            SnsUserInfoDTO userInfo;
            if (dto.getSnsType() == SnsType.GOOGLE) {
                userInfo = verifyGoogleIdToken(dto.getSnsToken());
            } else {
                //TODO: APPLE 로 변경 예정
                userInfo = verifyGoogleIdToken(dto.getSnsToken());
            }

            User user = userRepository.findByProviderIdAndProvider(userInfo.getId(), dto.getSnsType()).orElse(null);

            if (user == null) {
                throw new KkeaException(RCode.USER_NOT_EXISTS);
            }

//            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            TokenRespDTO response = new TokenRespDTO();
            response.setToken(tokenProvider.createToken(user));

            return response;
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    @Transactional
    public UserProfileDTO updateUser(UserUpdateReqDTO dto, UserPrincipal currentUser) throws KkeaException {
        try {
            User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));

            user.update(dto.getNickname(), dto.getAgreePush(), dto.getProfileImage());
            userRepository.save(user);

            return UserProfileDTO.from(user);
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    @Transactional
    public UserProfileDTO updateUserPushTime(UserTimeUpdateReqDTO dto, UserPrincipal currentUser) throws KkeaException {
        try {
            User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
            user.setWiseTime(dto.getTime());
            userRepository.save(user);

            return UserProfileDTO.from(user);
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    @Transactional
    public UserProfileDTO updateUserPushActive(UserActivePushUpdateReqDTO dto, UserPrincipal currentUser) throws KkeaException {
        try {
            User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new KkeaException(RCode.USER_NOT_EXISTS));
            user.updateAgreePush(dto.getActive());
            userRepository.save(user);

            return UserProfileDTO.from(user);
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    @Transactional
    public void withDraw(UserPrincipal user) throws KkeaException {
        userRepository.deleteByEmail(user.getEmail());
        StaticLogUtil.insertStaticLog(StaticLogType.WITHDRAW);
    }

    public void updateLastLogin(UserPrincipal currentUser) throws KkeaException {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + currentUser.getEmail()));
        user.updateLastLogin();
        userRepository.save(user);
    }
}
