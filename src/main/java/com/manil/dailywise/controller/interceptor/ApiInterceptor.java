package com.manil.dailywise.controller.interceptor;

import com.manil.dailywise.repository.VersionRepository;
import com.manil.dailywise.entity.Version;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.dto.common.RCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Service
public class ApiInterceptor implements HandlerInterceptor {
    Environment env;
    private VersionRepository versionRepository;

    @Autowired
    public void setVersionRepository(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws KkeaException {
        String profile = env.getProperty("spring.config.activate.on-profile");
        if(profile != null && (profile.equals("stg") || profile.equals("prd"))) {

            String userAgent = request.getHeader("user-agent");
            String version = request.getHeader("X-VERSION");

            if(userAgent != null && userAgent.contains("Electron")){
                String[] userVersion = version.split("\\.");
                Version newestVersion = versionRepository.findFirstByIsPublicOrderByCreatedAtDesc(true);
                String[] newestVersionArr = newestVersion.getVersion().split("\\.");
                if(userVersion.length < 2){
                    throw new KkeaException(RCode.VERSION_IS_NOT_ALLOW, "");
                }else{
                    if(Integer.parseInt(userVersion[0]) < Integer.parseInt(newestVersionArr[0])){
                        throw new KkeaException(RCode.VERSION_IS_NOT_ALLOW, "");
                    }
                }

            }
            //TODO : 버전관리 추가
        }

        MDC.put("requestURI", request.getMethod() + " " + request.getRequestURI());
        String requestId = request.getParameter("requestId");
        if(requestId == null) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put("requestId", requestId);
        MDC.put("userEmail", request.getRemoteUser());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.put("status", String.valueOf(response.getStatus()));
        log.info("");
        MDC.remove("requestURI");
        MDC.remove("requestId");
        MDC.remove("userEmail");
    }

}
