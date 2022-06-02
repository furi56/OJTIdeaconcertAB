package storybird.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import storybird.annotation.SearchValue;

import javax.servlet.http.HttpServletRequest;

@Component
public class SearchHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    // 현재 파라미터를 resolver에서 지원하는지 여부 반환 (boolean)
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class) &&
                parameter.hasParameterAnnotation(SearchValue.class);
    }

    // 실제 바인딩 객체 리턴
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String paramName = parameter.getParameterName();

        String search = request.getParameter(parameter.getParameterName());
        search = search.replace("%", "\\%");
        search = search.replace("_", "\\_");
        return search;
    }
}
