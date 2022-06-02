package storybird.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EpisodeImgVo {
	@ApiModelProperty(value="파일 고유 번호")
	private int file_no;
	
	@ApiModelProperty(value="파일 명")
	private String file_name;
	
	@ApiModelProperty(value="파일 경로")
	private String file_path;
	
	@ApiModelProperty(value="파일 형식")
	private String file_type;
	
	@ApiModelProperty(value="순서")
	private int order;
	
	@ApiModelProperty(value="회차 고유번호")
	private int epi_seq;
	
	@ApiModelProperty(value="작품 고유번호")
	private int opus_no;
	
	
}
