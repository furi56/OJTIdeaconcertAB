package storybird.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WatchHistoryVo {
	@ApiModelProperty(value="작품 관람 기록 고유 번호")
	private int watch_seq;
	
	@ApiModelProperty(value="관람 작품 고유 번호")
	private int opus_no;
	
	@ApiModelProperty(value="유저 고유 번호")
	private int mem_no;
	
	@ApiModelProperty(value="작품 관람 회차 고유 번호")
	private int epi_seq;
	
	@ApiModelProperty(value="최종 관람 일자")
	private String last_watch_date;
	
	@ApiModelProperty(value="작품명 Join전용")
	private String opus_title;

	@ApiModelProperty(value="회차 수 (1화, 2화) Join전용")
	private int epi_num;

	@ApiModelProperty(value="회차차 Join전용")
	private String epi_title;

	@ApiModelProperty(value="글작가 명 Join전용")
	private String author_name;
	
	@ApiModelProperty(value="작품 썸네일 Join전용")
	private String thumb_path;

	@ApiModelProperty(value="최초관람, 소장일자 Join전용")
	private String use_date;

	@ApiModelProperty(value="대여,멤버쉽 종료일자 Join전용")
	private String rent_end_date;

	@ApiModelProperty(value="작품 구매 형태(한글) Join전용")
	private String w_type;
}
