package storybird.exception;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import storybird.enums.ErrorCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ServletExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (UsernameNotFoundException e){
            setErrorResponse(ErrorCode.USER_NOT_FOUND, response, e);
        }catch(Exception e){
            setErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, response, e);
        }
    }

    public void setErrorResponse(ErrorCode errorCode, HttpServletResponse response, Throwable ex){
        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json");
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        try{
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
