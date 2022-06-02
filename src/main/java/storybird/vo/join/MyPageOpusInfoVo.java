package storybird.vo.join;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@Data
public class MyPageOpusInfoVo {
    @ApiModelProperty(value="좋아요 고유번호")
    private int fav_seq;

    @ApiModelProperty(value="구매 기록 고유 번호")
    private int h_idx;

    @ApiModelProperty(value="작품 고유 번호")
    private int opus_no;

    @ApiModelProperty(value="작품 회차 수(1화 2화)")
    private int epi_num;

    @ApiModelProperty(value="작품 제목")
    private String opus_title;

    @ApiModelProperty(value="회차 제목")
    private String epi_title;

    @ApiModelProperty(value="썸네일 경로")
    private String thumb_path;

    @ApiModelProperty(value="작가 이름")
    private String author_name;

    @ApiModelProperty(value="노출 작가 명")
    private String exposure_author;

    @ApiModelProperty(value="노출 성우 명")
    private String exposure_voice_actor;

    @ApiModelProperty(value="노출 회사 명")
    private String exposure_company;

    @ApiModelProperty(value="장르")
    private String genre;

    @ApiModelProperty(value="마지막 회차 관람 일자")
    private String last_epi_date;

    @ApiModelProperty(value="구매 일자")
    private String use_date;

    @ApiModelProperty(value="대여 종료 일자")
    private String rant_end_date;
}
