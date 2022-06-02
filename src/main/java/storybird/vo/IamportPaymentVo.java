package storybird.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.PaymentCancelDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class IamportPaymentVo {

    @JsonIgnore
    private List<IamportPaymentVo> paymentList;

    @ApiModelProperty(value="고유번호")
    private int imp_no;

    @ApiModelProperty(value="충전내역 고유번호")
    private int charge_seq;

    @ApiModelProperty(value="구매내역 고유번호")
    private int h_idx;

    @ApiModelProperty(value="아임포트 고유 번호")
    private String imp_uid;

    @ApiModelProperty(value="주문번호")
    private String merchant_uid;

    @ApiModelProperty(value="결제수단")
    private String pay_method;

    @ApiModelProperty(value="결제를 진행한 환경, 기기")
    private String channel;

    @ApiModelProperty(value="결제 PG사")
    private String pg_provider;

    @ApiModelProperty(value="PG사 고유거래번호")
    private String pg_tid;

    @ApiModelProperty(value="")
    private boolean escrow;

    @ApiModelProperty(value="카드사 승인번호")
    private String apply_num;

    @ApiModelProperty(value="은행 코드")
    private String bank_code;

    @ApiModelProperty(value="은행명")
    private String bank_name;

    @ApiModelProperty(value="카드코드")
    private String card_code;

    @ApiModelProperty(value="카드명")
    private String card_name;

    @ApiModelProperty(value="카드번호")
    private String card_number;

    @ApiModelProperty(value="카드 할부 개월수")
    private int card_quota;

    @ApiModelProperty(value="카드 타입")
    private int card_type;

    @ApiModelProperty(value="가상계좌")
    private String vbank_code;

    @ApiModelProperty(value="가상계좌 은행명")
    private String vbank_name;

    @ApiModelProperty(value="가상계좌 입금계좌번호")
    private String vbank_num;

    @ApiModelProperty(value="가상계좌 예금주")
    private String vbank_holder;

    @ApiModelProperty(value="가상계좌 입금기한")
    private long vbank_date;

    @ApiModelProperty(value="가상계좌")
    private long vbank_issued_at;

    @ApiModelProperty(value="결제 상품명")
    private String name;

    @ApiModelProperty(value="결제 금액")
    private BigDecimal amount;

    @ApiModelProperty(value="결제 취소금액")
    private BigDecimal cancel_amount;

    @ApiModelProperty(value="화폐 코드 명")
    private String currency;

    @ApiModelProperty(value="구매자 성명")
    private String buyer_name;

    @ApiModelProperty(value="구매자 이메일")
    private String buyer_email;

    @ApiModelProperty(value="구매자 전화번호")
    private String buyer_tel;

    @ApiModelProperty(value="구매자 주소")
    private String buyer_addr;

    @ApiModelProperty(value="구매자 우편번호")
    private String buyer_postcode;

    @ApiModelProperty(value="환불 금액")
    private String cancel_request_amount;

    @ApiModelProperty(value="환불 사유")
    private String refund_reason;

    @ApiModelProperty(value="환불 수령 계좌 예금주명")
    private String refund_holder;

    @ApiModelProperty(value="환불 수령 은행코드")
    private String refund_bank;

    @ApiModelProperty(value="환불 수령 계좌번호")
    private String refund_account;

    @ApiModelProperty(value="커스텀 데이터")
    private String custom_data;

    @ApiModelProperty(value="결제 상태[ ready:미결제 ,paid:결제완료 ]")
    private String status;

    @ApiModelProperty(value="결제 시작 시각")
    private long started_at;

    @ApiModelProperty(value="결제 완료 시각")
    private long paid_at;

    @ApiModelProperty(value="결제 실패 시각")
    private long failed_at;

    @ApiModelProperty(value="결제 취소 시각")
    private long cancelled_at;

    @ApiModelProperty(value="")
    private String fail_reason;

    @ApiModelProperty(value="")
    private String cancel_reason;

    @ApiModelProperty(value="영수증 조회 URL")
    private String receipt_url;

    @ApiModelProperty(value="")
    private PaymentCancelDetail[] cancel_history;

    @ApiModelProperty(value="")
    private boolean cash_receipt_issued;

    @ApiModelProperty(value="")
    private String customer_uid;

    @ApiModelProperty(value="")
    private String customer_uid_usage;

    public static IamportPaymentVo buildIamportPaymentVo(Payment payment){
        IamportPaymentVo vo = new IamportPaymentVo();
        vo.setImp_uid(payment.getImpUid());
        vo.setMerchant_uid(payment.getMerchantUid());
        vo.setPay_method(payment.getPayMethod());
        vo.setChannel(payment.getChannel());
        vo.setPg_provider(payment.getPgProvider());
        vo.setPg_tid(payment.getPgTid());
        vo.setApply_num(payment.getApplyNum());

        //은행 정보
        vo.setBank_code(payment.getBankCode());
        vo.setBank_name(payment.getBankName());

        //카드사 정보
        vo.setCard_code(payment.getCardCode());
        vo.setCard_name(payment.getCardName());
        vo.setCard_number(payment.getCardNumber());
        vo.setCard_type(payment.getCardType());

        // 가상계좌(vbank)관련 값이 필요할 수 있음
        //상품 정보
        vo.setName(payment.getName());
        vo.setAmount(payment.getAmount());
        vo.setCurrency(payment.getCurrency());

        //구매자 정보
        vo.setBuyer_email(payment.getBuyerEmail());
        vo.setBuyer_name(payment.getBuyerName());
        vo.setBuyer_tel(payment.getBuyerTel());

        vo.setStatus(payment.getStatus());
        vo.setStarted_at(payment.getStartedAt());
        vo.setPaid_at(payment.getPaidAt().getTime()/1000L);
        vo.setCustom_data(payment.getCustomData());

        vo.setCancel_amount(payment.getCancelAmount());
        vo.setCancel_history(payment.getCancelHistory());
        vo.setFailed_at(payment.getFailedAt().getTime()/1000L);
        vo.setCancelled_at(payment.getCancelledAt().getTime()/1000L);
        vo.setFail_reason(payment.getFailReason());
        vo.setCancel_reason(payment.getCancelReason());
        vo.setReceipt_url(payment.getReceiptUrl());
        vo.setCustomer_uid(payment.getCustomerUid());
        vo.setCustomer_uid_usage(payment.getCustomerUidUsage());
        return vo;
    }
}
