# OJTIdeaconcertAB

ERD 링크
https://drive.google.com/file/d/1-KwSTD2XILeX198GEvvCL5Uf8b2dr2HK/view?usp=sharing
    
   ### 프로젝트 과제 수행 순서

1. JWT 필터 처리 과정 확인
  - 작업 필요 Class   
  >storybird.security.JwtAuthenticationFilter.java - doFilter메소드   
  >storybird.security.JwtTokenProvider.java - resolveToken메소드

  - 원본 Class
  >storybird.security.JwtAuthenticationFilterOrigin.java   
  >storybird.security.JwtTokenProviderOrigin.java   

  [참고] 설정 Class
  >storybird.configure.WebSecurityConfig.java - configure 메소드


2. JWT 발급 방법
  - TestController에 발급 기능을 구현해놨습니다.
    > /user/token - 정상토큰 발급
    > /user/exp/token - 만료토큰 발급

  - JWT 필터 처리 테스트시 사용 [ URL /test/data ]

  - JWT 필터 처리 테스트 URL 호출시 Bearer Token 설정이 필요합니다.

  >포스트맨에서 Bearer Token 설정을 할 수 있고 방법은 아래와 같습니다.   
  >포스트맨 URL 전송화면 => Authorization탭 => Type값 Bearer Token으로 변경 => token값 입력

============================================
1. JWT 발급 처리 과정 확인
  - 작업 필요 Class
  >storybird.security.JwtUserDetailsService - loadUserByUsernagitme메소드
  
  - 원본 Class
  >storybird.security.JwtUserDetailsServiceOrigin
  
2. JWT 발급 테스트
  - TestController에 발급 기능을 구현해놨습니다.
    > /test/token - 토큰 발급
    > /test/user - 토큰 인증(Bearer Token 설정 후 URL 요청)
  
=============================================

LoginController 수정작업

=============================================

AudioBookController 수정작업

=============================================

