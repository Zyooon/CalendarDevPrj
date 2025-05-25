package com.calendardev.calendardevelop.common;

import com.calendardev.calendardevelop.enums.ErrorCode;
import com.calendardev.calendardevelop.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class WhitelistManager {

    private final ExceptionResponse exceptionResponse;
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

    //로그인 상태와 화이트리스트 판별하여 조건에 따라 BadRequest 날려준다.
    public boolean validateWhitelistAccess(boolean isLoggedIn, String requestURI, HttpServletResponse httpResponse) throws IOException {
        if (isLoggedIn && isWhitelistedUrl(LOGOUT_ONLY_URLS, requestURI)) {
            exceptionResponse.writeExceptionResponse(ErrorCode.REQUIRED_LOGOUT, httpResponse);
            return false;
        }

        if (!isLoggedIn && !isWhitelistedUrl(PUBLIC_URLS, requestURI)) {
            exceptionResponse.writeExceptionResponse(ErrorCode.REQUIRED_LOGIN, httpResponse);
            return false;
        }
        return true;
    }

    //화이트 리스트 인지 확인
    private boolean isWhitelistedUrl(String[] URLS_LIST, String requestURI) {

        return PatternMatchUtils.simpleMatch(URLS_LIST, requestURI);
    }
}

