package storybird.controller;

import com.google.gson.Gson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import storybird.enums.ErrorCode;
import storybird.exception.ErrorResponse;
import storybird.exception.ToonflixErrorException;
import storybird.security.JwtTokenProvider;
import storybird.security.JwtUserDetailsService;
import storybird.security.ToonflixUsers;
import storybird.service.*;
import storybird.util.DateUtil;
import storybird.vo.*;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 로그인 관련 기능 컨트롤러
 *
 */
@Controller
public class LoginController {
private static final Log logger = LogFactory.getLog(LoginController.class);

	@Autowired
	private JwtUserDetailsService jwtUserDetailService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private LoginService loginService;
	@Autowired
	private MemberService memberService;

	@ApiOperation(value = "플랫폼 로그인 처리 URL")
	@PostMapping("/login")
	public ResponseEntity<?> login(String id, String password) throws Exception {
		final ToonflixUsers userDetails = null;
		/*
		* 입력받은 parameter를 사용해 계정의 유효성을 확인하고 AccessToken을 생성하는 기능 구현
		*
		* 사용해야할 메소드
		* 1. loginService.authenticateMember - 계정 유효성 확인
		* 2. userDetails = jwtUserDetailService.loadUserByUsername
		* 	- 계정 정보 불러오기(ToonflixUsers 객체, userType = "BS")
		* 3. jwtTokenProvider.createToken - AccessToken 생성
		*/

		Map<String,Object> tokenMap = new HashMap<>();
		/*
		* tokenMap에 다음과 같은 값을 저장해서 반환
		* 1. ID
		* 2. level - ToonflixUsers.getUser_level()
		* 3. token - AccessToken
		* 4. nickName - ToonflixUsers.getNickname()
		* 5. userNo - ToonflixUsers.getUser_no()
		* 6. 기타 계정 정보(ToonflixUsers.getOther_info에 저장된 key, value 값을 저장)
		*/

		jwtUserDetailService.updateMemberLoginDate(id, userDetails.getUserType());

		return ResponseEntity.ok(DataResponse.builder()
				.message("Token access")
				.status(200)
				.data(tokenMap)
				.build());
	}

	@ApiOperation(value = "SNS 로그인 처리 URL")
	@GetMapping("/sns/login")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="SNS 아이디", dataType ="string", required = true),
			@ApiImplicitParam(name="userType", value="SNS 타입(카카오 : KO, 네이버 : NA)", dataType ="string", required = true)
	})
	public ResponseEntity<?> snsLogin(@ApiIgnore HttpServletRequest request, String id, String userType) throws Exception {
		Map<String,Object> tokenMap = new HashMap<>();
		return ResponseEntity.ok(DataResponse.builder()
				.message("Token access")
				.status(200)
				.data(tokenMap)
				.build());
	}

	@ApiOperation(value="회원가입 처리")
	@PostMapping("/signup")
	@ApiImplicitParams({
			@ApiImplicitParam(name="mem_id", value="유저 아이디", dataType ="string", required = true),
			@ApiImplicitParam(name="password", value="비밀번호", dataType ="string", required = true),
			@ApiImplicitParam(name="sub_email", value="추가 이메일", dataType ="string", required = true),
			@ApiImplicitParam(name="nickname", value="닉네임", dataType ="string", required = false),
			@ApiImplicitParam(name="gender", value="성별(남자:M, 여자:W)", dataType="string", required = false),
			@ApiImplicitParam(name="event_alarm_yn", value="이벤트 혜택 및 마케팅 정보 알림(Y or N)",dataType="string", required = false)
	})
	public ResponseEntity<?> signUp(@ApiIgnore HttpServletRequest request, @ApiIgnore MemberVo vo) throws Exception{
		//계정등록
		memberService.registerMember(vo);

		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				.build());
	}

	@ApiOperation(value="가입 계정 중복확인")
	@GetMapping("/duplicate/id")
	public ResponseEntity<?> duplicateId(String id){
		/*
		memberService.isDuplicateId 구현 필요
		*/
		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				.data(memberService.isDuplicateId(id))
				.build());
	}

	@ApiOperation(value="추가 이메일 중복확인")
	@GetMapping("/duplicate/sub/email")
	public ResponseEntity<?> duplicateSubEmail(String email){

		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				/*
				memberService.isDuplicateEmail 구현 필요
				*/
				.data(memberService.isDuplicateEmail(email))
				.build());
	}

	@ApiOperation(value = "유저 아이디 찾기")
	@GetMapping("/find/id")
	public ResponseEntity<?> findId(String email) throws Exception{
		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				/*
				loginService.getMemberId 구현 필요
				*/
				.data(loginService.getMemberId(email))
				.build());
	}


	@ApiOperation(value = "이메일 발송 PathVariable인 type에 맞춰 이메일 전송")
	@GetMapping("/email/send/{type}")
	@ApiImplicitParam(name = "type", value = "비밀번호재설정메일:pw, 이메일인증메일:auth, 휴면계정해제:release", paramType="path")
	public ResponseEntity<?> sendEmail(@PathVariable String type, String id) throws Exception {
		switch(type) {
		case "pw":
			/*
			* loginService.sendFindPwEmail 구현 필요
			*/
			String authCode = loginService.sendFindPwEmail(id);
			return ResponseEntity.ok(DataResponse.builder()
					.message("success")
					.status(200)
					.data(authCode)
					.build());
		case "auth":
			String secCode;
			secCode = loginService.sendAuthEmail(id);
			return ResponseEntity.ok(DataResponse.builder()
					.message("success")
					.status(200)
					.data(secCode)
					.build());
		case "release":
			String secCode2;
			secCode2 = loginService.sendMemberReleaseEmail(id);
			return ResponseEntity.ok(DataResponse.builder()
					.message("success")
					.status(200)
					.data(secCode2)
					.build());
		default :
			throw new Exception();
		}
	}

	@ApiOperation(value = "비밀번호 변경 권유 여부 확인")
	@GetMapping("/check/pw/date")
	public ResponseEntity<?> checkMemberPasswordDate() throws Exception {
		ToonflixUsers user = null;
		/*
		* 1. user = ToonflixUsers.getAuthorizedUsers() - AccessToken을 통해 등록된 유저 정보 조회
		*/
		if(user.getUserType().equals("BS")){
			memberService.getMemberDetail(user.getUser_no()); // - 계정 정보 조회
			/*
			* 1. 위에서 가져온 계정정보를 통해 계정 비밀번호 변경일 조회(last_pw_date)
			* 2. 비밀번호 변경일이 60일이 경과하지않았다면
			* 	ToonflixErrorException("변경 6개월 경과", ErrorCode.AFTER_SIX_MONTHS_OF_ACCESS) 발생
			*/
		}
		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				.build());

	}

	@ApiOperation(value = "비밀번호 변경")
	@PutMapping("/pw/change")
	public ResponseEntity<?> changeMemberPassword(String oldPassword, String newPassword) throws AuthException {
		ToonflixUsers user = null;
		/*
		 * 요청 클라이언트의 계정 정보를 조회하고 비밀번호 변경을 위해
		 * oldPassword와 DB에 등록된 비밀번호의 동일 여부를 검사하고 DB에 저장된 비밀번호를 변경한다
		 *
		 * 1. user = ToonflixUsers.getAuthorizedUsers - AccessToken을 통해 등록된 유저 정보 조회
		 * 2. BCryptPasswordEncoder().matches - 계정 비밀번호와 oldPassword가 동일한지 비교
		 * 2-1. 불일치시 BadCredentialsException("비밀번호 불일치") 발생
		 * 3. memberService.changeMemberPassword - 계정 비밀번호 변경
		 */

		/* 수정 필요 */
		memberService.changeMemberPassword(user.getUser_no(), newPassword);
		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				.build());
	}

	@ApiOperation(value = "보안키로 비밀번호 변경 여부 확인")
	@GetMapping("/pw/auth")
	public ResponseEntity<?> checkChangePasswordByAuthKey(String authKey) {
		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				.data(loginService.isSecCode(authKey))
				.build());

	}

	@ApiOperation(value = "보안키로 비밀번호 변경하기")
	@PutMapping("/pw/auth")
	public ResponseEntity<?> changePasswordByAuthKey(String authKey, String password) throws Exception{

		loginService.changeMemberPassword(authKey, password);
		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				.build());
	}

	@ApiOperation(value = "휴면 계정 해제를 위한 마지막 로그인 갱신")
	@PutMapping("/release/member")
	public ResponseEntity<?> releaseDormantMember(String id){
		loginService.updateMemberLoginDate(id, "BS");
		return ResponseEntity.ok(DataResponse.builder()
				.message("success")
				.status(200)
				.build());
	}
/*
	// refreshToken을 사용한 accessToken 재발급
	@ApiOperation(value = "토큰 재발급")
	@GetMapping("/token/reissue")
	@ApiImplicitParams({
			@ApiImplicitParam(name="Authorization", value="accessToken", required = true, dataType="String", paramType="header"),
			@ApiImplicitParam(name="RefreshToken", value="refreshToken", required = true, dataType="String", paramType="header")
	})
	public ResponseEntity<DataResponse> tokenTest2(@ApiIgnore HttpServletRequest request) {
		String accessToken = jwtTokenProvider.resolveToken(request);
		String refreshToken = request.getHeader("RefreshToken");

		String payload = jwtTokenProvider.decode(accessToken);
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(payload, Map.class);
		String id = map.get("sub").toString();
		String userType = map.get("userType").toString();
		List roles = (List<String>) map.get("roles");

		if (memberService.isCheckRefreshToken(id, refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
			accessToken = jwtTokenProvider.createToken(id, roles, userType);
		} else {
			throw new ToonflixErrorException("RefreshToken Error", ErrorCode.JWT_TOKEN_EXPIRATION);
		}

		if (!jwtTokenProvider.validateToken(refreshToken)) {
			throw new ToonflixErrorException("RefreshToken Error", ErrorCode.JWT_TOKEN_EXPIRATION);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("accessToken", accessToken);
		headers.set("refreshToken", refreshToken);

		return new ResponseEntity<>(DataResponse.builder()
				.status(200)
				.message("success")
				.build(), headers, HttpStatus.OK);
	}
	*/
}