package storybird.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EpisodeVo {
	@ApiModelProperty(value="회차 고유 번호")
	private int epi_seq;
	
	@ApiModelProperty(value="작품 고유 번호")
	private int opus_no;
	
	@ApiModelProperty(value="작품 제목 Join 전용")
	private String opus_title;
	
	@ApiModelProperty(value="작품 분류 Join 전용")
	private String opus_kind;
	
	@ApiModelProperty(value="작품 장르 Join 전용")
	private String genre;
	
	@ApiModelProperty(value="뷰어 종류" ,example="video")
	private String view_kind;
	
	@ApiModelProperty(value="회차 제목")
	private String epi_title;
	
	@ApiModelProperty(value="회차 번호")
	private int epi_num;
	
	@ApiModelProperty(value="조회수")
	private int view_cnt;
	
	@ApiModelProperty(value="별점")
	private int rating;
	
	@ApiModelProperty(value="무료 열람 여부")
	private String free_yn = "N";
	
	@ApiModelProperty(value="회차 열람 가능 여부")
	private String open_yn = "Y";
	
	@ApiModelProperty(value="회자 파일")
	private List<MultipartFile> epi_file;

	@ApiModelProperty(value="삽화 파일")
	private List<MultipartFile> art_work_file;
	
	@ApiModelProperty(value="대여 비용")
	private int rent_price;
	
	@ApiModelProperty(value="소장 비용")
	private int product_price;
	
	@ApiModelProperty(value="면세 코드")
	private String tax_free_code;

	@ApiModelProperty(value="면세 태그명")
	private String tax_free_tag;

	@ApiModelProperty(value="면세 여부")
	private String tax_free_yn;
	
	@ApiModelProperty(value="삭제 여부")
	private String del_yn;
	
	@ApiModelProperty(value="등록일")
	@JsonIgnore
	private Date reg_date;
	
	@ApiModelProperty(value="이전 회차번호")
	private int preIdx;
	
	@ApiModelProperty(value="다음 회차번호")
	private int nextIdx;

	@ApiModelProperty(value="구매상태")
	private int buy_state;

	@ApiModelProperty(value="작품 구매 일자")
	private String use_date;

	@ApiModelProperty(value="대여 종료 일자(분)")
	private String rent_end_minute;

	@ApiModelProperty(value="별점")
	private double star_score;

	@ApiModelProperty(value="오디오북 회차 재생시간")
	private int file_duration;
}
