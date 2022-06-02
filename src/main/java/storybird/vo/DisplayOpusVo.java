package storybird.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DisplayOpusVo {
    @ApiModelProperty(value="고유번호")
    private int dsp_no;

    @ApiModelProperty(value="순서를 바꾸기 위한 고유번호")
    private int target_no;

    @ApiModelProperty(value="작품 FK")
    private int opus_no;
    
    @ApiModelProperty(value="순서")
    private int dsp_order;

    @ApiModelProperty(value="진열 종류")
    private String dsp_type;

    @ApiModelProperty(value="작품 고유 코드")
    private String opus_code;

    @ApiModelProperty(value="작품명")
    private String opus_title;
    
    @ApiModelProperty(value="작품 카테고리")
    private String opus_kind;

    @ApiModelProperty(value="썸네일 경로")
    private String thumb_path;

    @ApiModelProperty(value="장르")
    private String genre;

    @ApiModelProperty(value="작가명")
    private String author_name;

    @ApiModelProperty(value="연재 요일")
    private String day_week;

    @ApiModelProperty(value="회차 갯수")
    private String epi_total;

    @ApiModelProperty(value="언어")
    private String lang_code;

    @ApiModelProperty(value="등록일자")
    private Date reg_date;
}
