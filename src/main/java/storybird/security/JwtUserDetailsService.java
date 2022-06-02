package storybird.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import storybird.dao.*;
import storybird.enums.ErrorCode;
import storybird.enums.UserRole;
import storybird.exception.ToonflixErrorException;
import storybird.service.ChargeHistoryService;
import storybird.util.DateUtil;
import storybird.vo.ChargeHistoryVo;
import storybird.vo.MemberVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * DB에 회원정보를 확인, 비교하는 Service Class<br />
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired private MemberDAO memberDao;
    @Autowired private ChargeHistoryService chargeHistoryService;

    public ToonflixUsers loadUserByUsername(String id, String userType) throws UsernameNotFoundException {
        MemberVo userInfo = null;
        /*
        [member 테이블 사용]
        * userInfo = member 테이블에 저장된 유저 정보를 저장하도록 구현(ID 검색)
        */

        //userInfo가 존재할때 ToonflixUsers(로그인 유저 정보)를 반환하도록 설정
        if (userInfo != null) {
            if (userInfo.getSeces_mem_yn().equals("Y")) throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
            HashMap otherInfoMap = new HashMap<>();
            String birthDate = userInfo.getBirth_date();
            if (birthDate != null) {
                LocalDate now = LocalDate.now();
                LocalDate parseBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                int age = now.minusYears(parseBirthDate.getYear()).getYear();

                if(parseBirthDate.plusYears(age).isAfter(now)){
                    age -=1;
                }
                String adultYn = age > 18 ? "Y" : "N";
                otherInfoMap.put("adultYn", adultYn);
            } else {
                otherInfoMap.put("adultYn", "N");
            }
            ChargeHistoryVo memberShipVo = chargeHistoryService.getMemberShipHistory(userInfo.getMem_no());
            otherInfoMap.put("birthDate", userInfo.getBirth_date());
            otherInfoMap.put("membershipYn", memberShipVo != null ? "Y" : "N");
            return ToonflixUsers.builder()
                    .user_no(userInfo.getMem_no())
                    .id(userType.equals("BS") ? userInfo.getMem_id() : userInfo.getSns_id())
                    .password(userInfo.getPassword())
                    .userType(userInfo.getUser_type())
                    .nickname(userInfo.getNickname())
                    .sub_email(userInfo.getSub_email())
                    .user_level(1)
                    .profile_path(userInfo.getProfile_path())
                    .roles(Collections.singletonList(UserRole.find(1).toString()))
                    .other_info(otherInfoMap)
                    .build();
        } else {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }
    }

    public void updateMemberLoginDate(String mem_id, String userType) throws ParseException {
        MemberVo userInfo = memberDao.selectMemberById(mem_id, userType);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date loginDate = format.parse(userInfo.getLast_login_date());
        long diffDays = DateUtil.getDifferenceDay(loginDate, new Date());

        if (diffDays > 180) {
            throw new ToonflixErrorException("접속 6개월 경과", ErrorCode.AFTER_SIX_MONTHS_OF_ACCESS);
        }
        memberDao.updateLastLoginDateFromMemberById(mem_id, userType);
    }

    @Override
    public ToonflixUsers loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
