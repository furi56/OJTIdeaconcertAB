package storybird.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtTokenProviderOrigin { // JWT 토큰을 생성 및 검증 모듈

	@Value("${spring.jwt.secret}")
	private String secretKey;

	@Value("${jwt.header-name}")
	private String TOKEN_HEADER;

	@Value("${jwt.access.header-name}")
	private String ACCESS_TOKEN_HEADER;

	public static final String AUTHORIZATION = "Authorization";
	public static final String AUTHORIZATION_ACCESS = "AuthorizationAccess";

	private long tokenValidMilisecond = 1000L * 60 * 60 * 24 * 30; // 1달 토큰 유효
	private long accessTokenValidMilisecond = 1000L * 60 * 60 * 24 * 30;
	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// Jwt 토큰 생성
	public String createToken(String userId, List<String> roles, String userType) {
		return createToken(userId, roles, userType, accessTokenValidMilisecond);
	}

	// Jwt 토큰 생성
	public String createRefreshToken() {
		return createToken(null, null, null, tokenValidMilisecond);
	}

	//Jwt 토큰 유효기간 설정
	public String createToken(String userId, List<String> roles, String userType, long validTime){
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("roles", roles);
		claims.put("userType", userType);
		Date now = new Date();
		return Jwts.builder().setClaims(claims)
				.setHeaderParam("typ",  "JWT")
				.setIssuer("StoryBird")
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + validTime))
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
				.compact();

	}

	// Connection Access Token 생성 - 홈페이지 접근 허가 토큰
	public String createAccessPageToken() {
		Claims claims = Jwts.claims().setSubject("StoryBird");
		Date now = new Date();
		return Jwts.builder().setClaims(claims) // 데이터
				.signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
				.compact();
	}


	// Jwt 토큰으로 인증 정보를 조회
	public Authentication getAuthentication(String token) {
		String roleStr = getUserRole(token).get(0);
		UserDetails userDetails = null;
		if(roleStr.contains("ADMIN")) {
			// userDetails = userDetailsService.loadAdminByUsername(this.getUserIdFromToken(token));
		}else if(roleStr.contains("AUTHOR") || roleStr.contains("PARTNER") || roleStr.contains("EMPLOYEE")){
			// userDetails = userDetailsService.loadCompanyOrAuthorByUsername(this.getUserIdFromToken(token));
		}else {
			userDetails = userDetailsService.loadUserByUsername(this.getUserIdFromToken(token), this.getUserType(token));
		}

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// Jwt 토큰에서 회원 구별 정보 추출
//	public String getUserId(String token) {
//		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//	}

	public List<String> getUserRole(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("roles",List.class);
	}

	public String getUserType(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("userType",String.class);
	}

	// Request의 Header에서 token 파싱
	public String resolveToken(HttpServletRequest req) {

		Enumeration<String> headers = req.getHeaders(AUTHORIZATION);
		while(headers.hasMoreElements()){
			String value = headers.nextElement();
			if(value.toLowerCase().startsWith(TOKEN_HEADER.toLowerCase()) && !value.contains("undefined")){
				String token = value.substring(TOKEN_HEADER.length()).trim();
				return value.substring(TOKEN_HEADER.length()).trim();
			}
		}

		return null;
	}

	//retrieve username from jwt token
	public String getUserIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	public int getUserNoFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("no",Integer.class);
	}
	public String getEmailFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("email",String.class);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	//for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey.getBytes())
				.parseClaimsJws(token)
				.getBody();
	}

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validateConnectAccessToken(String jwtToken) {
		String tempToken = createAccessPageToken();
		if(tempToken.equals(jwtToken)){
			return true;
		}else{
			return false;
		}
	}

	//JWT 유효기간 상관없이 decode 진행
	public String decode(String token){
		String[] splitToken = token.split("\\.");
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] decodedBytes = decoder.decode(splitToken[1]);

		String decodedString = null;
		try{
			decodedString = new String(decodedBytes, "UTF-8");
		}catch(UnsupportedEncodingException e){

		}
		return decodedString;
	}
}
