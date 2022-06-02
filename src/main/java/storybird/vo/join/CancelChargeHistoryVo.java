package storybird.vo.join;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CancelChargeHistoryVo {
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
	
	@ApiModelProperty(value="결제 금액")
	private int settle_amt;
	
	@ApiModelProperty(value="결제 수단")
	private String settle_method;
	
	@ApiModelProperty(value="구매 상태", example="Y: 환불, N:환불안함")
	private String buy_state;
	
	@ApiModelProperty(value="충전 일자")
	private String charge_date;

	@ApiModelProperty(value="JOIN 이용권 사용 종료 일자")
	private String use_end_date;

	@ApiModelProperty(value="JOIN 수수료")
	private int fee;

	@ApiModelProperty(value="JOIN 환불 금액")
	private int refund_amount;

	@ApiModelProperty(value="JOIN 기간 사용금액")
	private int use_date_amount;
}

