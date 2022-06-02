package storybird.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OpusHistoryVo {
	@ApiModelProperty(value="작품 구매 기록 고유 번호")
	private int h_idx;
	
	@ApiModelProperty(value="유저 고유 번호")
	private int mem_no;
	
	@ApiModelProperty(value="관람 작품 고유 번호")
	private int opus_no;

	@ApiModelProperty(value="담당 관리자 고유 번호")
	private int admin_no;

	@ApiModelProperty(value="관람 작품 고유 번호")
	private int partner_no;

	@ApiModelProperty(value="관람 작품 구분")
	private String opus_kind;

	@ApiModelProperty(value="관람 작품 타이틀")
	private String opus_title;

	@ApiModelProperty(value="관람 작품 타이틀")
	private String company_name;

	@ApiModelProperty(value="플랫폼 이름")
	private String platform_name;

	@ApiModelProperty(value="작품 관람 회차 고유 번호")
	private int epi_seq;

	@ApiModelProperty(value="선물 고유 번호")
	private int gift_no;

	@ApiModelProperty(value="작품 관람 회차 고유 번호")
	private int epi_number;

	@ApiModelProperty(value="작품 관람 회차 테스트")
	private String  epi_title;
	
	@ApiModelProperty(value="작품 관람 회차 금액")
	private int use_cash;

	@ApiModelProperty(value="정액제 구입 기록 번호")
	private int mem_ship_seq;

	@ApiModelProperty(value="작품 관람 회차 금액")
	private int sale_cash;
	
	@ApiModelProperty(value="작품 관람 대여 여부")
	private String rant_yn;

	@ApiModelProperty(value="구매 취소 여부")
	private String cancel_yn;
	
	@ApiModelProperty(value="사용 일자")
	private String use_date;

	@ApiModelProperty(value="에피소드 등록 일자 Join전용")
	private String start_date;

	@ApiModelProperty(value="에피소드 등록 일자 Join전용")
	private String end_date;

	@ApiModelProperty(value="에피소드 등록 일자 Join전용")
	private String deposit_date;

	@ApiModelProperty(value="구매 일자 Join전용")
	private String sale_date;

	@ApiModelProperty(value="정산 일자 Join전용")
	private String tax_date;

	@ApiModelProperty(value="대여 금액 Join전용")
	private int rent_price;

	@ApiModelProperty(value="소장 금액 Join전용")
	private int product_price;

	@ApiModelProperty(value="남은 캐시 Join전용")
	private int remain_cash = 0;

	@ApiModelProperty(value="남은 보너스 캐시 Join전용")
	private int remain_bonus_cash = 0;

	@ApiModelProperty(value="구매 수량 Join전용")
	private int buy_amount;

	@ApiModelProperty(value="사용자 ID Join전용")
	private String mem_id;

	@ApiModelProperty(value="구매상품 타입 Join전용")
	private String item_type;
}

