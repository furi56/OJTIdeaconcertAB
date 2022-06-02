package storybird.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import storybird.exception.ServletExceptionHandler;
import storybird.security.JwtAuthenticationFilter;
import storybird.security.JwtTokenProvider;
import storybird.security.SecurityAccessDeniedHandler;
import storybird.security.SecurityAuthenticationEntryPoint;

/*
 *  WebSecurity를 위한 설정 정보가 담긴 Config</br>
 *
 * */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private SecurityAccessDeniedHandler securityAccessDeniedHandler;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;
	@Autowired
	private ServletExceptionHandler exceptionHandler;

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable()
				.csrf().disable()
				.cors().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(securityAuthenticationEntryPoint)
				.accessDeniedHandler(securityAccessDeniedHandler)
				.and()
				.authorizeRequests()
				.antMatchers("/**/login","/**/signup","/**/token").permitAll()
				/*.antMatchers("/admin/**").hasAnyRole(UserRole.ROLE_ADMIN1.getRole(), UserRole.ROLE_ADMIN2.getRole(),
						UserRole.ROLE_ADMIN3.getRole(), UserRole.ROLE_ADMIN4.getRole(), UserRole.ROLE_ADMIN5.getRole(),
						UserRole.ROLE_ADMIN6.getRole(), UserRole.ROLE_ADMIN7.getRole(), UserRole.ROLE_ADMIN8.getRole(),
						UserRole.ROLE_ADMIN9.getRole(), UserRole.ROLE_ADMIN10.getRole(), UserRole.ROLE_AUTHOR1.getRole(),
						UserRole.ROLE_AUTHOR2.getRole(), UserRole.ROLE_AUTHOR3.getRole(), UserRole.ROLE_PARTNER1.getRole(),
						UserRole.ROLE_PARTNER2.getRole(), UserRole.ROLE_PARTNER3.getRole(), UserRole.ROLE_EMPLOYEE.getRole())*/
				.antMatchers("/*/user/conn").authenticated()
				.antMatchers(HttpMethod.GET, "/actuator/health").permitAll()
				.anyRequest().permitAll()
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(exceptionHandler, JwtAuthenticationFilter.class);


	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
				"/swagger-ui.html","/webjars/**","/swagger/**");
	}
}