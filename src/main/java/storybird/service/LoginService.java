package storybird.service;

import java.util.List;

import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import storybird.dao.MemberDAO;
import storybird.enums.ErrorCode;
import storybird.exception.ToonflixErrorException;
import storybird.security.JwtTokenProvider;
import storybird.security.JwtUserDetailsService;
import storybird.security.ToonflixUsers;
import storybird.util.MailSendUtil;

@Service
public class LoginService extends AbstractService{
	@Autowired
	MemberDAO memberDao;

	@Autowired
	private JwtUserDetailsService userDetailService;

	@Autowired
	MailSendUtil mailSendUtil;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public void authenticateMember(String id, String password) throws Exception{
		try {
			/* [member 테이블 사용]
			* 입력받은 id 와 password를 토대로 DB에 접근해 (1)계정 정보를 가져온뒤
			* parameter로 전달받은 비밀번호와 DB에 (1)계정 정보의 비밀번호를 비교하여
			* 전달받은 비밀번호와 저장된 비밀번호가 다를 경우 BadCredentialsException를 발생시킨다
			*
			* 사용해야할 메소드
			* 1. userDetailService.loadUserByUsername - 계정 정보 불러오기(userType="BS")
			* 2. BCryptPasswordEncoder.matches(입력받은 비밀번호, DB에 저장된 비밀번호(암호화상태)) - 비밀번호가 동일한지 비교하는 메소드
			*/
		}catch(ToonflixErrorException e){
			if(e.getErrorCode().getCode().equals("M000")){
				throw new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
			}
		}
	}

	public void authenticateSNSMember(String id, String userType) throws Exception{
		try{
			final ToonflixUsers userDetails = (ToonflixUsers) userDetailService.loadUserByUsername(id, userType);
			if(userDetails.getUser_no() == 0){
				throw new BadCredentialsException("등록되지않은 계정입니다.");
			}
		}catch(ToonflixErrorException e){
			if(e.getErrorCode().getCode().equals("M000")){
				throw new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
			}
		}
	}
	/*
	public void authenticateAdmin(String id, String password) throws BadCredentialsException{
		try{
			final ToonflixUsers userDetails = (ToonflixUsers) userDetailService.loadAdminByUsername(id);
			BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
			if(!passEncoder.matches(password, userDetails.getPassword())) {
				throw new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
			}
		}catch(ToonflixErrorException e){
			if(e.getErrorCode().getCode().equals("M000")){
				throw new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
			}
		}
	}
	public void authenticateCompanyOrAuthor(String id, String password) throws BadCredentialsException{
		final ToonflixUsers userDetails = (ToonflixUsers) userDetailService.loadCompanyOrAuthorByUsername(id);
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
		if(!passEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
		}
	}
	*/
	//이메일 인증 번호 전송 후 인증번호 반환
	public String sendAuthEmail(String id) throws DuplicateMemberException {
		String authKey = getRandomInt(6);
		if(memberDao.findDuplicateId(id)) throw new DuplicateMemberException("계정 중복");
		mailSendUtil.sendAuthEmail(id, "StoryBird 이메일 인증 요청", authKey);
		return authKey;
	}

	public String sendMemberReleaseEmail(String id) {
		String authKey = getRandomInt(6);
		mailSendUtil.sendAuthEmail(id, "StoryBird 이메일 인증 요청", authKey);
		return authKey;
	}

	//비밀번호 찾기 메일 전송 메소드
	public String sendFindPwEmail(String id) throws Exception{
		String secCode = getRandomStr(20);
		/*
		[member 테이블 사용]
		* member 테이블 sec_code 값을 String secCode에 저장된 값으로 변경 필요 - 해당 계정 SecCode 변경
		*/
		if(/**/memberDao.updateSecCodeById(id, secCode, "BS") > 0) {
			mailSendUtil.sendFindPwEmail(id, "StoryBird 비밀번호 찾기", secCode);
			return secCode;
		}
		else
			throw new ToonflixErrorException("계정을 찾을 수 없음", ErrorCode.INVALID_INPUT_VALUE);
	}
	public List<String> getMemberId(String email) throws Exception{
		/*
		* [member 테이블 사용]
		* parameter email과 동일한 값을 가진 값을 가진 리스트를 조회하고 mem_id 값을 리스트화 하여 반환하도록 구현
		*
		*/

		return null;
	}

	public void changeMemberPassword(String authKey, String password) throws Exception{
		int result = memberDao.updateMemberPasswordBySecCode(authKey, new BCryptPasswordEncoder().encode(password));
		if(result == 0) throw new ToonflixErrorException("잘못된 인증키 입니다.", ErrorCode.INVALID_AUTH_CODE);
		memberDao.updateMemberSecCodeEmpty(authKey);
	}

	public boolean isSecCode(String authKey) {
		return memberDao.findSecCode(authKey);
	}

	public void updateMemberLoginDate(String mem_id, String userType) {
		if(memberDao.findDuplicateId(mem_id)){
			memberDao.updateLastLoginDateFromMemberById(mem_id, userType);
		}else{
			throw new UsernameNotFoundException("계정을 찾을 수 없음");
		}
	}
}
