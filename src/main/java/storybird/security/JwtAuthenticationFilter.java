package storybird.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import storybird.enums.ErrorCode;
import storybird.exception.ErrorResponse;
import storybird.exception.ToonflixErrorException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    // connectAccessToken없이도 접근 가능하게할 URL 정보
    // 해당 문구가 포함되는 모든 URL을 허가하기 때문에 Controller에서 URL 지정시 주의를 요함
    // 추후 수정해야될 로직이라고 생각함 by 2022-01-13 이한얼
    private final String excludeUrl = "/favicon.ico,/image/,/profile/,/download/," +
            "/contents/,/brochure/,/iamport-webhook,/error,/test/token/issue";

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException, ToonflixErrorException {
        HttpServletRequest req = (HttpServletRequest) request;

        String token = "";
        jwtTokenProvider.resolveToken(req); // 구현필요
        /*
        * token = jwtTokenProvider.resolveToken(req) 를 통해 header에 있는 토큰값 가져오기
         */

        //토큰 만료시 에러 발생
        /*
        * 1. token이 존재하나 유효기간이 지낫을때 403에러가 표시되도록 구현 후
        *       println("error") 출력
        *
        * 2. token이 존재하고 유효기간이 지나지 않았을때
        *    SecurityContextHolder.getContext().setAuthentication 에 인증 정보 저장 후  *필수
        *    println을 사용하여 토큰값 출력
        *
        * 사용해야할 메소드
        * jwtTokenProvider.validateToken - 토큰 유효 기간 만료 여부 반환 (boolean)
        * jwtTokenProvider.getAuthentication - 토큰 기반 인증 정보 반환 (Authentication)
        */

        chain.doFilter(request, response);


    }

    //토큰 만료 여부와 관계없이 응답할 수 있는 허용 URL
    private boolean isConnectionAccessFilterExcludeUrl(HttpServletRequest request) {
        String path = request.getServletPath();
        List<String> checkedUrls = Arrays.asList(excludeUrl.split(","));
        for (String url : checkedUrls) {
            if (path.contains(url)) return true;
        }
        return false;
    }
}
