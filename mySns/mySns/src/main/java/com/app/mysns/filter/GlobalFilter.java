package com.app.mysns.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.mysns.service.JwtService;
import com.app.mysns.service.SecureUtilsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalFilter implements Filter  {

    private static final String[] whiteList = { "/", "/login", "/auth/signup", "/auth/login", "/logout", "/favicon.ico", "/agent" };
    private static SecureUtilsService secureUtilsService = new SecureUtilsService();
    @Autowired
    private JwtService jwtService = new JwtService();

    // public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    //     System.out.println("this is filter");
    //     servletResponse.setContentType("text/html;charset=UTF-8");
    // }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {

            // 웹소켓은 예외
            if(requestURI.startsWith("/ws")) {
                chain.doFilter(request, response);
                return;
            }

            // sms or kakao 예외
            // 어뷰징이 될 수 있음
            if(requestURI.startsWith("/sms") || requestURI.startsWith("/kakao")) {
                chain.doFilter(request, response);
                return;
            }

            // 정적 파일은 예외
            if(requestURI.startsWith("/static")) {
                chain.doFilter(request, response);
                return;
            }

            // auth 쪽은 검증 안함
            if(requestURI.startsWith("/auth")) {
                chain.doFilter(request, response);
                return;
            }

            if(requiredAuthentication(requestURI)) {
                // System.out.println("로그인 필요 없는 페이지 : " + requestURI);    
                chain.doFilter(request, response);
                return;
            }

            // System.out.println("인증 필터 시작 : " + requestURI);
            // System.out.println("로그인 여부 확인 필요 : " + requestURI);

            // 쿠키 삭제 여부 검증
            // Cookie[] cookies = httpRequest.getCookies();
            Cookie[] cookies = httpRequest.getCookies();

            // 프록시 고려
            String clientIpAddress = (null != httpRequest.getHeader("X-FORWARDED-FOR")) ? httpRequest.getHeader("X-FORWARDED-FOR") : httpRequest.getRemoteAddr();

            boolean isLoggedIn = false;

            if(cookies != null) {
                for(Cookie c : cookies) {
                    System.out.println(c.getName());

                    if(!c.getName().startsWith("mysns_token")) { continue; }

                    // 기존 있을 경우, 만료 시간 검증 필요함!!!!!!!!
                    String[] data = c.getValue().split("\\.");
                    // String username = "";

                    if(data.length > 2) {
                        try {
                            if(!jwtService.isTokenExpired(c.getValue())) {
                                isLoggedIn = true;
                            }
                            // 토큰 갱신 필요할 수 있음
                            
                            break;
                        } catch (Exception e) {
                            // 토큰 가져오기 실패
                            System.out.println(e.getMessage());
                            break;
                        }

                    } else {
                        // 길이가 다르면 이 쿠키는 믿으면 안됨
                        // 쿠키 초기화
                        Cookie cookie = new Cookie("mysns_token", null);
                        cookie.setMaxAge(-1);
                        cookie.setSecure(true);
                        cookie.setHttpOnly(true);
                        cookie.setPath("/");
                        //add cookie to response
                        httpResponse.addCookie(cookie);
                        break;
                    }   
                }
            }

            if(isLoggedIn) {
                chain.doFilter(request, response);
                return;
            }
            httpResponse.sendRedirect("/login?redirect="+requestURI);
            // return ;
        } catch (Exception e) {
            throw e;
        } finally {
            // System.out.println("인증 필터 종료 : " + requestURI);
            // System.out.println("=========================================" );
        }
    }


    public Boolean requiredAuthentication(String uri) {
        return Arrays.asList(whiteList).contains(uri);
    }

}
