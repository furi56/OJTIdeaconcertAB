package storybird.configure;

import com.fasterxml.classmate.TypeResolver;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import storybird.enums.ErrorCode;
import storybird.resolver.SearchHandlerMethodArgumentResolver;
import storybird.vo.*;
import storybird.vo.join.MyPageOpusInfoVo;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class WebConfig implements WebMvcConfigurer{

	@Value("${admin.hostname}")
	String adminHostname;

	@Value("${platform.hostname}")
	String platFormHostname;

	@Autowired
	SearchHandlerMethodArgumentResolver searchValueResolver;

	//swagger2 적용
	@Bean
	public Docket api() {
		TypeResolver typeResolver = new TypeResolver();

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.additionalModels(typeResolver.resolve(OpusHistoryVo.class),
						typeResolver.resolve(ChargeHistoryVo.class),typeResolver.resolve(ChargeVo.class),
						typeResolver.resolve(DisplayOpusVo.class),
						typeResolver.resolve(EpisodeFileVo.class),typeResolver.resolve(EpisodeVo.class),
						typeResolver.resolve(MemberVo.class),typeResolver.resolve(OpusVo.class),
						typeResolver.resolve(WatchHistoryVo.class), typeResolver.resolve(MyPageOpusInfoVo.class))
				.select()
				.apis(RequestHandlerSelectors.basePackage("storybird.controller"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, resMsgList());
	}
	
	//swagger2 전역 반환 메시지 설정
	// 기본적으로 500, 400, 404 에러 메시지를 미리 설정해둔다
	private List<ResponseMessage> resMsgList() {
		List<ResponseMessage> list = new ArrayList<ResponseMessage>();
		list.add(new ResponseMessageBuilder()
                .code(500)
                .message("Internal Server Error")
                .responseModel(new ModelRef("Error"))
                .build());
		list.add(new ResponseMessageBuilder()
                .code(400)
                .message("Bad Request")
                .build());
		list.add(new ResponseMessageBuilder()
                .code(404)
                .message("Not Found")
                .build());
		return list;
	}

	
	private ApiInfo apiInfo() { 
    return new ApiInfoBuilder()
            .title("StoryBird API DOC")
            .description("StoryBird 의 API 문서입니다.")
            .build();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("*")
				.allowedOrigins("*",adminHostname, platFormHostname
				,"http://localhost:8177","http://localhost:3000");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(searchValueResolver);
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.removeIf(v -> v.getSupportedMediaTypes().contains(MediaType.APPLICATION_JSON));
		converters.add(new MappingJackson2HttpMessageConverter());
	}

	//Lucy Xss Filter
	@Bean
	public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean(){
		FilterRegistrationBean<XssEscapeServletFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new XssEscapeServletFilter());
		registrationBean.setOrder(1);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
}
 