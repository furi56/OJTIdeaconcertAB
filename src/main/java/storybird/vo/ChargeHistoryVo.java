package storybird.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ChargeHistoryVo {
	@ApiModelProperty(value="충전내역 고유 번호")
	private int charge_seq;

	@ApiModelProperty(value="유저 고유 번호")
	private int mem_no;

	@ApiModelProperty(value="쿠폰 고유 번호")
	private int c_no;
	
	@ApiModelProperty(value="구매 상품명")
	private String item_name;

	@ApiModelProperty(value="상품 종류")
	private String item_type;

	@ApiModelProperty(value="구매  아이템 번호")
	private String item_code;

	@ApiModelProperty(value="멤버쉽 개월수")
	private int item_month;

	@ApiModelProperty(value="결제 금액")
	private int settle_amt;
	
	@ApiModelProperty(value="결제 수단")
	private String settle_method;
	
	@ApiModelProperty(value="결제 코인 수량")
	private int charge_cash;

	@ApiModelProperty(value="구매 상태", example="Y: 환불, N:환불안함")
	private String buy_state;
	
	@ApiModelProperty(value="충전 일자")
	private String charge_date;

	@ApiModelProperty(value="국가코드")
	private String nation_code;
	
	@ApiModelProperty(value="취소 가능일")
	private int c_date;

	@ApiModelProperty(value="JOIN 이용권 사용 종료 일자")
	private String use_end_date;

	@ApiModelProperty(value="JOIN 아임포트 결제 수단")
	private String payment_type;
}

