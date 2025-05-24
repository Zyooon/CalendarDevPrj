package com.calendardev.calendardevelop.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    //게시글 보기는 로그아웃 상태에서도 진입 가능
    private static final String[] PUBLIC_LIST = {
            "/calendar/users/signup",
            "/calendar/users/login",
            "/calendar/board",
            "/calendar/board/detail/*"
    };

    //회원가입, 로그인 기능은 로그아웃 상태에서만 진입 가능 (로그인 상태는 진입 불가)
    private static final String[] LOGOUT_ONLY_LIST = {
            "/calendar/users/signup",
            "/calendar/users/login"
    };

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();


        if(isLoggedIn(httpRequest, httpResponse)){
            if(isWhiteList(LOGOUT_ONLY_LIST,requestURI)){
                throwBadRequest("로그아웃을 먼저 해야합니다.",httpResponse);
                return;
            }
        }else{
            if(!isWhiteList(PUBLIC_LIST, requestURI)){
                throwBadRequest("로그인을 먼저 해야합니다.",httpResponse);
                return;
            }
        }
        
        //세션 확인 후 다음 Servlet 진행
        chain.doFilter(request, response);
    }

    //로그인 상태 확인
    private boolean isLoggedIn(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        HttpSession session = httpRequest.getSession(false);

        return session != null && session.getAttribute(Const.USER_ID) != null;

    }

    //화이트 리스트 인지 확인
    private boolean isWhiteList(String[] WHITE_LIST, String requestURI) {

        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

    //Filter 는 Spring 예외처리의 범위 밖이기 때문에 Global 예외 처리로 잡을 수 없다
    //직접 수동으로 메세지를 남겨준다.
    //세션에 없을 시 예외 처리. 예외 메세지는 Json 형식으로 반환
    private void throwBadRequest(String message, HttpServletResponse httpResponse) throws IOException{
        int status = HttpStatus.BAD_REQUEST.value();
        String location = "HttpServlet";
        String responseMessage = String.format("{\"status\": %d, \"message\": \"%s\", \"location\": \"%s\"}",status, message, location);

        httpResponse.setStatus(status);
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.getWriter().write(responseMessage);
    }

}
