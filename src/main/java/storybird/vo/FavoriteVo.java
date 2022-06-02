package storybird.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@ToString
public class FavoriteVo {
    @ApiModelProperty(value="계약 고유 코드")
    private String mem_no;

    @ApiModelProperty(value="계약 제목")
    private String cont_title;

    @ApiModelProperty(value="회차 카운트")
    private int epi_cnt;

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

    @ApiModelProperty(value="글 작가명")
    private String write_author;

    @ApiModelProperty(value="그림 작가명")
    private String draw_author;

    @ApiModelProperty(value="글 작가 고유 번호")
    private String write_author_no;

    @ApiModelProperty(value="그림 작가 고유 번호")
    private String draw_author_no;

    @ApiModelProperty(value="회사 고유 번호")
    private String company_code;

    @ApiModelProperty(value="회사 고유 번호")
    private String company_name;

    @ApiModelProperty(value="연재 시작 일자")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date start_date;

    @ApiModelProperty(value="연재 종료 일자")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date end_date;

    @ApiModelProperty(value="나이 제한")
    private int age_limit;

    @ApiModelProperty(value="해쉬 태그", example="태그1,태그2,태그3")
    private String hash_tag;

    @ApiModelProperty(value="스토리 소개")
    private String story_intro;

    @ApiModelProperty(value="작품 공개 여부", example="Y")
    private String open_yn = "N";

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
    private MultipartFile thumb_file;

    @ApiModelProperty(value="썸네일(가로) 원본명")
    private String thumb_hori_origin;

    @ApiModelProperty(value="썸네일(가로) 저장  경로")
    private String thumb_hori_path;

    @ApiModelProperty(value="썸네일(가로) 파일")
    private MultipartFile thumb_hori_file;

    @ApiModelProperty(value="썸네일(세로) 원본명")
    private String thumb_vert_origin;

    @ApiModelProperty(value="썸네일(세로) 저장 경로")
    private String thumb_vert_path;

    @ApiModelProperty(value="썸네일(세로) 파일")
    private MultipartFile thumb_vert_file;

    @ApiModelProperty(value="국가코드")
    private String nation_code;

    @ApiModelProperty(value="등록일")
    private Date reg_date;

    @ApiModelProperty(value="보유 회차 총합")
    private int epi_total;
}
