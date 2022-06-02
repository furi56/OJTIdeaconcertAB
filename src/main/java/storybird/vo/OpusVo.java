package storybird.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OpusVo {
	@ApiModelProperty(value="계약 고유 번호")
	private int cont_no;

	@ApiModelProperty(value="작품계약 고유 번호")
	private int o_no;

	@ApiModelProperty(value="계약 제목")
	private String o_title;
	
	@ApiModelProperty(value="작품 고유 번호")
	private int opus_no;
	
	@ApiModelProperty(value="작품 다음 번호")
	private int next_code;
	
	@ApiModelProperty(value="작품 고유 코드")
	private String opus_code;
	
	@ApiModelProperty(value="작품 연재 요일", example="월,화,수")
	private String day_week = "";
	
	@ApiModelProperty(value="작품 제목")
	private String opus_title;
	
	@ApiModelProperty(value="작품 종류", example="오디오북")
	private String opus_kind;
	
	@ApiModelProperty(value="장르")
	private String genre;

	@ApiModelProperty(value="작가 고유 번호들")
	private String author_no;

	@ApiModelProperty(value="노출용 작가")
	private String exposure_author;

	@ApiModelProperty(value="성우 고유 번호")
	private String voice_actor_no;

	@ApiModelProperty(value="노출용 성우")
	private String exposure_voice_actor;

	@ApiModelProperty(value="노출용 회사")
	private String exposure_company;

	@ApiModelProperty(value="회사 고유 번호")
	private String company_code;
	
	@ApiModelProperty(value="회사 고유 번호")
	private String company_name;

	@ApiModelProperty(value="연재 시작 일자")
	// @DateTimeFormat(pattern="yyyy-MM-dd")
	private String start_date;

	@ApiModelProperty(value="연재 종료 일자")
	// @DateTimeFormat(pattern="yyyy-MM-dd")
	private String end_date;
	
	@ApiModelProperty(value="나이 제한")
	private int age_limit;
	
	@ApiModelProperty(value="해쉬 태그", example="태그1,태그2,태그3")
	private String hash_tag;
	
	@ApiModelProperty(value="스토리 소개")
	private String story_intro;

	//삭제 예정
	@ApiModelProperty(value="작품 공개 여부", example="Y")
	private String open_yn = "N";

	@ApiModelProperty(value="작품 서비스 여부")
	private String service_state;

	@ApiModelProperty(value="작품 승인 여부", example="Y")
	private String per_yn;
	
	@ApiModelProperty(value="멤버쉽 무료 열람 여부", example="Y")
	private String vou_free_yn = "N";
	
	@ApiModelProperty(value="완결 여부", example="Y")
	private String comp_yn = "N";
	
	@ApiModelProperty(value="비정기 여부", example="Y")
	private String irregular_yn = "N";

	@ApiModelProperty(value="면세 태그명")
	private String tax_free_tag;
	
	@ApiModelProperty(value="면세 코드")
	private String tax_free_code;

	@ApiModelProperty(value="면세 여부", example="Y")
	private String tax_free_yn = "N";

	@ApiModelProperty(value="삭제 여부", example="Y")
	private String del_yn;
	
	@ApiModelProperty(value="연재 주기", example="Y")
	private int opus_cycle;
	
	@ApiModelProperty(value="대여 비용")
	private int rent_price;
	
	@ApiModelProperty(value="소장 비용")
	private int product_price;
	
	@ApiModelProperty(value="썸네일(1:1 Size) 원본명 ")
	private String thumb_origin;
	
	@ApiModelProperty(value="썸네일(1:1 Size) 저장경로")
	private String thumb_path;
	
	@ApiModelProperty(value="썸네일(1:1 Size) 파일")
	@JsonIgnore
	private MultipartFile thumb_file;
	
	@ApiModelProperty(value="썸네일(세로) 원본명")
	private String thumb_vert_origin;
	
	@ApiModelProperty(value="썸네일(세로) 저장 경로")
	private String thumb_vert_path;
	
	@ApiModelProperty(value="썸네일(세로) 파일")
	@JsonIgnore
	private MultipartFile thumb_vert_file;

	@ApiModelProperty(value="국가코드")
	private String nation_code;

	@ApiModelProperty(value="언어코드")
	private String lang_code;
	
	@ApiModelProperty(value="등록일")
	@JsonIgnore
	private Date reg_date;
	
	@ApiModelProperty(value="보유 회차 총합")
	private int epi_total;

	@ApiModelProperty(value="회차 타입 (화/권)")
	private String epi_type;

	@ApiModelProperty(value="작품 조회수")
	private int opus_count;

	@ApiModelProperty(value="작품 조회수")
	private double star_score;

	@ApiModelProperty(value="JOIN 최근 회차 등록 일자")
	private String last_epi_date;

	@ApiModelProperty(value="작품 조회수")
	private int total_cash;

	@ApiModelProperty(value="작품 구매 일자")
	private String use_date;

	@JsonIgnore
	@ApiModelProperty(value="Join 일자")
	private String format_date;

	@ApiModelProperty(value="작가명")
	private String author_name;

	@ApiModelProperty(value="성우명")
	private String voice_name;

	@ApiModelProperty(value="Join opus 언어코드")
	private String lang;

	@ApiModelProperty(value="Join opus 전체판매 금액")
	private int total;

	@ApiModelProperty(value="Join 작품 열람 횟수")
	private int view_cnt;

	@ApiModelProperty(value="Join 작품 좋아요 횟수")
	private int fav_cnt;

	@ApiModelProperty(value="Join 작품 별점 평가 횟수")
	private int star_score_cnt;

	@ApiModelProperty(value="Join 작품 이벤트 여부")
	private String evt_yn;

	@ApiModelProperty(value="Join 작품 현재 랭킹")
	private int ranking;

	@ApiModelProperty(value="Join 작품 이전 랭킹")
	private int pre_ranking;

	@ApiModelProperty(value="Join 회차 재생시간 총합(단위 : 초)")
	private int duration_total;

	@ApiModelProperty(value="Join 신작 작품 여부")
	private String new_yn;
}
