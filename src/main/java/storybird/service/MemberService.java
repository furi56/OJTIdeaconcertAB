package storybird.service;

import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import storybird.dao.MemberDAO;
import storybird.enums.ErrorCode;
import storybird.enums.FilePath;
import storybird.enums.PageSetting;
import storybird.exception.ToonflixErrorException;
import storybird.security.JwtUserDetailsService;
import storybird.security.ToonflixUsers;
import storybird.util.FileUtil;
import storybird.util.PaginationInfo;
import storybird.util.ValidationUtil;
import storybird.vo.MemberVo;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Locale;

@Service
public class MemberService extends AbstractService{

	@Autowired
	MemberDAO memberDao;

	@Autowired
	JwtUserDetailsService jwtUserDetailsService;

	public MemberVo getMemberDetail(int memNo) {
		return memberDao.selectMemberByNo(memNo);
	}

	public MemberVo getMemberDetail(String memId, String userType) {
		return memberDao.selectMemberById(memId, userType);
	}
	public List<Integer> getAlluser(){
		return memberDao.selectAllUser();
	}

	//유저 등록(회원가입)
	public void registerMember(MemberVo vo) throws Exception {
		/*
		* [member 테이블 사용]
		* 1. 유저ID(mem_id)와 동일한 ID가 이미 등록되어있는지 확인하고 중복시 DuplicateMemberException 발생
		* 2. 유저ID(mem_id)와 동일한 추가이메일(sub_email)이 이미 등록되어 있는지 확인 중복시 DuplicateMemberException 발생
		* 3. vo.getNickname에 값이 없다면 임의의 닉네임으로 재설정
		* 4. vo.setPassword에 비밀번호 인코딩(new BCryptPasswordEncoder().encode 사용)
		*
		 */
		if(vo.getUser_type() == null || vo.getUser_type().equals("")){
			vo.setUser_type("BS");
		}
		if(vo.getSns_id() == null){
			vo.setSns_id("");
		}
		/* [member 테이블 사용]
		* 위에서 에러가 발생하지 않았다면 DB에 유저 정보 등록 진행
		 */
	}

	public void registerSnsMember(String nationCode, String id, String userType){
		MemberVo vo = new MemberVo();
		vo.setSns_id(id);
		vo.setMem_id(id);
		vo.setUser_type(userType);
		vo.setBirth_date(null);
		vo.setGender("");
		if(vo.getNickname() == null) vo.setNickname("USER"+getRandomInt(10));
		vo.setProfile_path("user/profile/null");
		vo.setNation_code(nationCode);
		memberDao.insertMember(vo);
	}
	//계정 중복 확인
	public boolean isDuplicateId(String id) {
		/*
		* [member 테이블 사용]
		*  member 테이블 id(mem_id)가 등록되었는지 확인 후
		* 등록되었다면 true, 등록되지않았다면 false 값 return
		*
		*/
		return false;
	}

	//계정 중복 확인
	public boolean isDuplicateEmail(String email) {
		/*
		 * [member 테이블 사용]
		 *  DB에 email(sub_email)이 등록되었는지 확인 후
		 * 등록되었다면 true, 등록되지않았다면 false 값 return
		 *
		 */
		return false;
	}

	public boolean isDuplicateSNSId(String snsId, String userType){
		return memberDao.findDuplicateSNSId(snsId, userType);
	}

	public boolean isCheckRefreshToken(String id, String token){
		return memberDao.findRefreshTokenById(id, token);
	}

	//유저 정보 수정
	public void modifyMember(MemberVo vo) throws Exception {
			MemberVo tempVo = memberDao.selectMemberByNo(vo.getMem_no());
			memberDao.updateMemberByNo(vo);

	}
	//비밀번호 확인 후 수정
	public void modifyMemberByBasic(MemberVo vo, String oldPw) throws Exception {
		final ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
		vo.setMem_no(user.getUser_no());

		if(user != null && user.getUserType().equals("BS")){
			boolean isCheck = new BCryptPasswordEncoder().matches(oldPw,user.getPassword());
			if(!isCheck) throw new BadCredentialsException("비밀번호 불일치");
		}

		if(!vo.getPassword().equals("") && user.getUserType().equals("BS")) {
			if(!ValidationUtil.isMemberPw(vo.getPassword())) throw new ValidationException("비밀번호 유효성 오류","M004");
			vo.setPassword(new BCryptPasswordEncoder().encode(vo.getPassword()));
			memberDao.updateMemberLastPasswordDateByMemNo(vo.getMem_no());
		}else {
			vo.setPassword(null);
		}
		modifyMember(vo);
	}

	//refreshToken 저장
	public void modifyRefreshToken(int memNo, String token){
		memberDao.updateRefreshTokenByNo(memNo, token);
	}

	//유저 삭제
	public void deleteMember(int memNo) throws Exception {
		//final ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
		//if(memNo == user.getUser_no() || user.getUser_level() == UserRole.ROLE_ADMIN.getLevel()) {
			MemberVo tempVo = memberDao.selectMemberByNo(memNo);
			//FileUtil.delFile(FilePath.DEFAULT_UPLOAD_PATH + tempVo.getProfile_path());
			memberDao.updateDelStateByNo(memNo);
		//}
	}

	//유저 탈퇴
	public void secessionMember(MemberVo vo) throws Exception {
		final ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
		vo.setMem_no(user.getUser_no());
		if(user != null && user.getUserType().equals("BS")){
			boolean isCheck = new BCryptPasswordEncoder().matches(vo.getPassword(),user.getPassword());
			if(!isCheck) throw new BadCredentialsException("비밀번호 불일치");
		}
		memberDao.updateSecessionFromMemberByNo(vo);
	}
	//전체 유저 수 조회
	public int getTotalCountMember() {
		return memberDao.countMember();
	}

	//유저 리스트 
	public PaginationInfo getMemberPagingList(int page,int limit, String search, String start_date, String end_date){
		PaginationInfo pginfo = new PaginationInfo();

		pginfo.setRecordCountPerPage(limit);
		pginfo.setTotalRecordCount(memberDao.countSearchMember(search, start_date, end_date));
		pginfo.setPageSize(PageSetting.PAGE_SIZE.get());
		pginfo.setCurrentPageNo(page);

		pginfo.setList(memberDao.selectAllMember(
				pginfo.getFirstRecordIndex(), limit, search, start_date, end_date));
		return pginfo;
	}

	public void changeMemberUseStatus(int memNo) {
		memberDao.updateMemberUseStatusByNo(memNo);
	}
	public void changeMemberPassword(int memNo, String password) {
		/* [member 테이블 사용]
		* 비밀번호를 암호화 시킨뒤 비밀번호 변경(DB 수정 - member.password)
		* 비밀번호 마지막 수정일을 오늘 날짜로 변경(DB 수정 - member.last_pw_date)
		*
		* 사용 메소드
		* BCryptPasswordEncoder().encode - 비밀번호 암호화
		*/
		if(!ValidationUtil.isMemberPw(password))
			throw new ToonflixErrorException("비밀번호 형식 오류", ErrorCode.PASSWORD_FORMAT_NOT_CORRECT);
		memberDao.updateMemberPasswordByMemNo(memNo, new BCryptPasswordEncoder().encode(password));
		memberDao.updateMemberLastPasswordDateByMemNo(memNo);
	}

	public int getNewMemberCount(){
		return memberDao.countNewMember();
	}
	public MemberVo getMemberCount(String start_date,String end_date){
		return memberDao.selectstatis(start_date,end_date);
	}
	public MemberVo getMemberDateCount(String start_date,String end_date){
		return memberDao.selectstatisDate(start_date,end_date);
	}
	public void changeMemberCustomerUid(int memNo, String cstUid){
		memberDao.updateCustomerUidByNo(memNo, cstUid);
	}

}
