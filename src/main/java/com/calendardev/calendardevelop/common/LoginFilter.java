package com.calendardev.calendardevelop.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {
            "/calendar/users/signup",
            "/calendar/users/login",
            "/calendar/board",
            "/calendar/board/detail/*"
    };

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        //화이트 리스트 확인
        if (!isWhiteList(requestURI)) {
            
            //세션(로그인) 여부 확인. 리턴시 URI 접근 불가
            if(!isAlreadyLogin(httpRequest, httpResponse)) return;
        }
        //세션 확인 후 다음 Servlet 진행
        chain.doFilter(request, response);
    }

    //로그인 상태 확인
    private boolean isAlreadyLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        HttpSession session = httpRequest.getSession(false);

        //Filter 는 Spring 예외처리의 범위 밖이기 때문에 Global 예외 처리로 잡을 수 없다
        //직접 수동으로 메세지를 남겨준다.
        //세션에 없을 시 예외 처리. 예외 메세지는 Json 형식으로 반환
        if (session == null || session.getAttribute(Const.USER_ID) == null) {
            int status = HttpStatus.BAD_REQUEST.value();
            String message = "로그인을 먼저 해야합니다.";
            String location = "HttpServlet";
            String responseMessage = String.format("{\"status\": %d, \"message\": \"%s\", \"location\": \"%s\"}",status, message, location);

            httpResponse.setStatus(status);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write(responseMessage);
            return false;
        }
        return true;
    }

    private boolean isWhiteList(String requestURI) {

        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

}
