package storybird.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EpisodeFileVo {
	@ApiModelProperty(value="회차 파일 고유 번호")
	private int file_no;
	
	@ApiModelProperty(value="회차 파일명")
	private String file_name;
	
	@ApiModelProperty(value="회차 파일 크기")
	private long file_size;
	
	@ApiModelProperty(value="회차 파일 저장 경로")
	private String file_path;
	
	@ApiModelProperty(value="회차 파일 타입")
	private String file_type;

	@ApiModelProperty(value="파일 호출 형식")
	private String file_call_type;

	@ApiModelProperty(value="파일 재생 길이(오디오)")
	private long file_duration;

	@ApiModelProperty(value="회차 파일 배치  순서")
	private int order;
	
	@ApiModelProperty(value="관련 회차 고유 번호")
	private int epi_seq;
	
	@ApiModelProperty(value="관련 작품 고유 번호")
	private int opus_no;
}
