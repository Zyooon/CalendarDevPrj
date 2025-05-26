package com.calendardev.calendardevelop.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter implements Filter {

    private final LoginManager loginManager;
    private final WhitelistManager whitelistManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        //로그인 상태인지 확인
        boolean isLoggedIn = loginManager.isLoggedIn(httpRequest);

        //입력받은 URI
        String requestURI = httpRequest.getRequestURI();
        
        //GET, POST 등 요청받은 메소드 방식
        String httpMethod = httpRequest.getMethod();

        //화이트 리스트 판별. 리턴 시 예외처리
        if(!whitelistManager.validateWhitelistAccess(isLoggedIn, requestURI, httpMethod, httpResponse)) return;
        
        //세션 확인 후 다음 Servlet 진행
        chain.doFilter(request, response);
    }

}
