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

public class JwtAuthenticationFilterOrigin extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilterOrigin(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException, ToonflixErrorException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        //토큰 만료시 에러 발생
        if (token != null && !jwtTokenProvider.validateToken(token)) {
            HttpServletResponse res = (HttpServletResponse) response;
            ObjectMapper objectMapper = new ObjectMapper();
            res.setStatus(403);
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.JWT_TOKEN_EXPIRATION);
            objectMapper.writeValue(res.getWriter(), errorResponse);
        }else if (token != null && jwtTokenProvider.validateToken(token)) {
            System.out.println("userRole > " + jwtTokenProvider.getUserRole(token));
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);


    }
}
