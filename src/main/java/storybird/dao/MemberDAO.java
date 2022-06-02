package storybird.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import storybird.vo.MemberVo;

import java.util.List;

@Mapper
public interface MemberDAO {
	public List<MemberVo> selectAllMember(int start, int num, String search,
										  String start_date, String end_date);
	public MemberVo selectMemberById(String mem_id, String user_type);
	public MemberVo selectSNSMemberById(String sns_id, String user_type);
	public MemberVo selectMemberByNo(int mem_no);
	public MemberVo selectstatis(String start_date, String end_date);
	public MemberVo selectstatisDate(String start_date, String end_date);
	public List<String> findMemberIdBySubEmail(String sub_email);
	public List<Integer> selectAllUser();
	public boolean findDuplicateId(String mem_id);
	public boolean findDuplicateSubEmail(String sub_email);
	public boolean findDuplicateSNSId(String sns_id, String user_type);
	public boolean findSecCode(String sec_code);
	public boolean findRefreshTokenById(String id, String token);
	public int countMember();
	public int countSearchMember(String search,
			String start_date, String end_date);
	public int countNewMember();

	public int insertMember(MemberVo vo);
	
	public int updateSecCodeById(String mem_id, String sec_code, String user_type);
	public int updateMemberByNo(MemberVo vo);
	public int updateRefreshTokenByNo(int mem_no, String token);
	public int updateMemberPasswordByMemNo(int mem_no, String password);
	public int updateMemberPasswordBySecCode(String sec_code, String password);
	public int updateMemberSecCodeEmpty(String sec_code);
	public int updateLastLoginDateFromMemberById(String mem_id, String user_type);
	public int updateSecessionFromMemberByNo(MemberVo vo);
	public int updateMemberUseStatusByNo(int mem_no);
	public int updateMemberLastPasswordDateByMemNo(int mem_no);
	public int updateCustomerUidByNo(int mem_no, String customer_uid);

	public int updateDelStateByNo(int mem_no);
	public int deleteMemberByNo(int mem_no);
}
