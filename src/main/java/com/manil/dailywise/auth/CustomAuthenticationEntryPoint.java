package com.manil.dailywise.auth;

import com.manil.dailywise.dto.common.RCode;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import io.jsonwebtoken.*;

@Configuration
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static String tokenSecret;

    @Value("${login.auth.tokenSecret}")
    public void setTokenSecret(String path) {
        tokenSecret = path;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        JSONObject jsonObject = (JSONObject) request.getAttribute("RCode");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter out = response.getWriter();

        if(jsonObject == null) {
            jsonObject = new JSONObject();
            String jwt = getJwtFromRequest(request);
            try {
                Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(jwt);
            } catch (ExpiredJwtException ex) {
                jsonObject.put("errorCode", RCode.TOKEN_EXPIRED.getResultCode());
                jsonObject.put("errorMessage", RCode.TOKEN_EXPIRED.getResultMessage());
            } catch (Exception ex){
                jsonObject.put("errorCode", RCode.UNAUTHORIZED.getResultCode());
                jsonObject.put("errorMessage", RCode.UNAUTHORIZED.getResultMessage());
            }
        }
        out.print(jsonObject);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("content-type", "application/json");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length(), bearerToken.length());
        }
        return null;
    }
}
