package com.calendardev.calendardevelop.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    //게시글 보기는 로그아웃 상태에서도 진입 가능
    private static final String[] PUBLIC_URLS = {
            "/calendar/users/signup",
            "/calendar/users/login",
            "/calendar/board",
            "/calendar/board/detail/*"
    };

    //회원가입, 로그인 기능은 로그아웃 상태에서만 진입 가능 (로그인 상태는 진입 불가)
    private static final String[] LOGOUT_ONLY_URLS = {
            "/calendar/users/signup",
            "/calendar/users/login"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if(!validateWhitelistAccess(isLoggedIn(httpRequest), httpRequest.getRequestURI(), httpResponse)) return;
        
        //세션 확인 후 다음 Servlet 진행
        chain.doFilter(request, response);
    }

    //로그인 상태 확인
    private boolean isLoggedIn(HttpServletRequest httpRequest) throws IOException {

        HttpSession session = httpRequest.getSession(false);

        return session != null && session.getAttribute(Const.USER_ID) != null;

    }

    //로그인 상태와 화이트리스트 판별하여 조건에 따라 BadRequest 날려준다.
    private boolean validateWhitelistAccess(boolean isLoggedIn, String requestURI, HttpServletResponse httpResponse) throws IOException{
        if (isLoggedIn && isWhitelistedUrl(LOGOUT_ONLY_URLS, requestURI)) {
            throwBadRequest(ErrorCode.REQUIRED_LOGOUT, httpResponse);
            return false;
        }

        if (!isLoggedIn && !isWhitelistedUrl(PUBLIC_URLS, requestURI)) {
            throwBadRequest(ErrorCode.REQUIRED_LOGIN, httpResponse);
            return false;
        }
        return true;
    }

    //화이트 리스트 인지 확인
    private boolean isWhitelistedUrl(String[] URLS_LIST, String requestURI) {

        return PatternMatchUtils.simpleMatch(URLS_LIST, requestURI);
    }

    //Filter 는 Spring 예외처리의 범위 밖이기 때문에 Global 예외 처리로 잡을 수 없다
    //직접 수동으로 메세지를 남겨준다.
    //세션에 없을 시 예외 처리. 예외 메세지는 Json 형식으로 반환
    private void throwBadRequest(ErrorCode errorCode, HttpServletResponse httpResponse) throws IOException{
        int status = errorCode.getStatus().value();
        String location = "HttpServlet";
        String responseMessage = String.format("{\"status\": %d, \"message\": \"%s\", \"location\": \"%s\"}",status, errorCode.getMessage(), location);

        httpResponse.setStatus(status);
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.getWriter().write(responseMessage);
    }

}
