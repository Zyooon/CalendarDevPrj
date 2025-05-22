package com.calendardev.calendardevelop.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginManager {

    public Long getUserIdFromSession(HttpServletRequest httpServletRequest){
        return (Long)httpServletRequest.getSession().getAttribute(Const.USER_ID);
    }
}
