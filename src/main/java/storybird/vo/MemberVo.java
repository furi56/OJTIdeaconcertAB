package storybird.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
public class MemberVo {
	@ApiModelProperty(value="멤버 PK")
	private int mem_no;

	@ApiModelProperty(value="유저 가입유형")
	private String user_type;

	@ApiModelProperty(value="SNS ID")
	@NotNull(message = "Error")
	@NotEmpty(message = "Error2")
	private String sns_id;

	@ApiModelProperty(value="ID")
	private String mem_id;
	
	@ApiModelProperty(value="보조 이메일")
	private String sub_email;

	@ApiModelProperty(value="비밀번호")
	private String password;
	
	@ApiModelProperty(value="닉네임")
	private String nickname;
	
	@ApiModelProperty(value="프로필 이미지 저장 경로")
	private String profile_path = "user/profile/null";
	
	@ApiModelProperty(value="프로필 원본 이미지명")
	private String profile_origin;

	@ApiModelProperty(value="프로필 파일 저장 변수")
	private MultipartFile profile_file;
	
	@ApiModelProperty(value="출생일자", example="2021-01-01")
	private String birth_date;
	
	@ApiModelProperty(value="성별", example="M")
	private String gender;

	@ApiModelProperty(value="추천인 아이디")
	private String rcmnd_id;

	@ApiModelProperty(value="작품 알림 여부", example="Y")
	private String event_alarm_yn = "Y";

	@ApiModelProperty(value="계정 사용 여부", example="Y")
	private String use_mem_yn;

	@ApiModelProperty(value="탈퇴 여부", example="Y")
	private String seces_mem_yn;

	@ApiModelProperty(value="삭제 여부", example="Y")
	private String del_yn;

	@ApiModelProperty(value="마지막 접속 일자")
	private String last_login_date;

	@ApiModelProperty(value="마지막 패스워드 변경 일자")
	private String last_pw_date;
	
	@ApiModelProperty(value="탈퇴 피드백 내용")
	private String seces_feed;

	@ApiModelProperty(value="탈퇴 사유")
	private String seces_content;

	@ApiModelProperty(value="탈퇴 일자")
	private String seces_date;

	@ApiModelProperty(value="보안코드(이메일 인증 전용)")
	private String sec_code;

	@ApiModelProperty(value="국가 코드")
	private String nation_code = "";

	@ApiModelProperty(value="아임포트 customerUid")
	private String customer_uid;
	
	@ApiModelProperty(value="가입 일자")
	private String reg_date;

	@ApiModelProperty(value="구매 개수")
	private int cash_cnt;

	@ApiModelProperty(value="총회원가입 수")
	private int cnt;

	@ApiModelProperty(value="pc회원가입 수")
	private int pccnt;

	@ApiModelProperty(value="모바일 회원가입 수")
	private int mocnt;

	@ApiModelProperty(value="휴면회원 수")
	private int slcnt;

	@ApiModelProperty(value="탈퇴회원 수")
	private int secnt;

	@ApiModelProperty(value="구매수")
	private int buy_cnt;

	@ApiModelProperty(value="총구매액")
	private int buy_cash;
}

