package com.calendardev.calendardevelop.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

//로그인(세션) 관련 일괄 처리
@Component
public class LoginManager {

    //세션에서 유저 ID 정보 가져온다.
    public Long getUserIdFromSession(HttpServletRequest httpServletRequest){
        return (Long)httpServletRequest.getSession().getAttribute(Const.USER_ID);
    }

    //로그인 상태 확인
    public boolean isLoggedIn(HttpServletRequest httpRequest){
        HttpSession session = httpRequest.getSession(false);
        return session != null && session.getAttribute(Const.USER_ID) != null;
    }

    //세션에 유저 ID 저장
    public void setUserIdToSession(HttpServletRequest httpServletRequest, Long userId){
        httpServletRequest.getSession().setAttribute(Const.USER_ID, userId);
    }

    //세션 및 쿠키 만료
    public void resetSessionAndCookies(HttpServletRequest request, HttpServletResponse response){
        //세션 만료
        request.getSession().invalidate();

        // postman 에서는 서버에서 자체적으로 쿠키(JSESSIONID)를 계속 보냄
        // 쿠키도 같이 만료 필요
        Cookie cookie = new Cookie(Const.JSESSIONID, null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }

}
