package storybird.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChargeVo {
	@ApiModelProperty(value="상품 고유 번호")
	private String item_code;
	
	@ApiModelProperty(value="상품명")
	private String item_name;
	
	@ApiModelProperty(value="상품 타입명")
	private String item_type;
	
	@ApiModelProperty(value="상품 순서")
	private int item_order;

	@ApiModelProperty(value="상품 개월 수")
	private int item_month;

	@ApiModelProperty(value="결제 금액")
	private int price;
	
	@ApiModelProperty(value="국가코드")
	private String nation_code;
	
	@ApiModelProperty(value="사용여부")
	private String status_yn;

	@JsonIgnore
	@ApiModelProperty(value="상품 등록날짜")
	private Date reg_date;
	
	
}
