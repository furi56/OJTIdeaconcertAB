package storybird.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import storybird.enums.ErrorCode;
import storybird.exception.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final ErrorResponse errResponse = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
        response.setContentType("application/json");
        response.setContentType("UTF-8");
        response.setStatus(ErrorCode.HANDLE_ACCESS_DENIED.getStatus());
        try(OutputStream os = response.getOutputStream()){
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(os, errResponse);
            os.flush();
        }
    }
}
