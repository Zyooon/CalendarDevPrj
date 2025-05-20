package com.calendardev.calendardevelop.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class LoginManager {

    public Long getUserIdOrElseNotLogin(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);

        if(session == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인을 먼저 해야합니다.");
        }

        return (Long)session.getAttribute(Const.USER_ID);
    }
}
