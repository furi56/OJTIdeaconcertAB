package storybird.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;
import storybird.enums.ErrorCode;
import storybird.enums.GlobalEnum;
import storybird.enums.NationCode;
import storybird.exception.ErrorResponse;
import storybird.exception.ToonflixErrorException;
import storybird.security.JwtTokenProvider;
import storybird.security.ToonflixUsers;
import storybird.service.*;
import storybird.util.DateUtil;
import storybird.util.IamportUtil;
import storybird.util.PaginationInfo;
import storybird.vo.*;
import storybird.vo.join.CancelChargeHistoryVo;

import javax.security.auth.message.AuthException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 플랫폼 상품결제 컨트롤러
 */

@Controller
public class ChargeController {

    @Autowired ChargeService chargeService;
    @Autowired BuyHistoryService buyHistoryService;
    @Autowired MemberService memberService;
    @Autowired ChargeHistoryService chargeHistoryService;
    @Autowired EpisodeService epiService;
    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired IamportPaymentService paymentService;

    private IamportClient api;
    private static String API_KEY = "";
    private static String API_SECRET = "";

    public ChargeController() {
        this.api = new IamportClient(API_KEY, API_SECRET);
    }

    @ApiOperation(value = "결제 상품 리스트")
    @GetMapping("/charge/item")
    public ResponseEntity<?> charge() throws Exception {
        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
        int mem_no = user.getUser_no();

        String country = NationCode.find(StringUtils.upperCase(LocaleContextHolder.getLocale().toLanguageTag())).getCode();

        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(chargeService.getselectChargeItem(country))
                .build());
    }

    @ApiOperation(value = "아임포트 결제 상품 상세 조회")
    @ResponseBody
    @GetMapping("/charge/cash/verifyIamport/{imp_uid}")
    public ResponseEntity<?> paymentByImpUid(
            @PathVariable(value = "imp_uid") String imp_uid) throws IamportResponseException, IOException {
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(api.paymentByImpUid(imp_uid))
                .build());
    }

    @ApiOperation(value = "아임포트 customer_uid(빌링키) 결제 - 멤버쉽 결제")
    @PostMapping("/imp/membership/billings")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_uid", value="빌링키", dataType="string", paramType="form", required=true),
            @ApiImplicitParam(name="amount", value="상품가격", dataType="int", paramType="form", required=true),
            @ApiImplicitParam(name="name", value="상품명", dataType="string", paramType="form", required=true),
            @ApiImplicitParam(name="custom_data", value="커스텀데이터(로그인토큰,상품코드 - token,ItemCode)", dataType="string", paramType="form", required=true)
    })
    public ResponseEntity<?> iamportBillingPayment(@ApiIgnore IamportPaymentVo payment)
            throws Exception {
        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
        memberService.changeMemberCustomerUid(user.getUser_no(), payment.getCustomer_uid());
        payment.setBuyer_name(user.getId());
        ObjectMapper objectMapper = new ObjectMapper();

        ChargeHistoryVo chVo = chargeHistoryService.getMemberShipHistory(user.getUser_no());

        String requestPayStr = IamportUtil.requestSubPay(payment);
                //카드 정상 승인
        return ResponseEntity.ok(DataResponse.builder()
                    .status(200)
                    .message("success")
                    .build());
    }

    @ApiOperation(value="아임포트 웹훅")
    @PostMapping("/iamport-webhook")
    public ResponseEntity<?> iamportWebhook(HttpServletRequest request, String imp_uid, String merchant_uid, String status) throws IamportResponseException, IOException, AuthException {
        String message = "";
        String ip = request.getHeader("X-Forwarded-For");
        /*if(ip == null) ip = request.getRemoteAddr();

        if(!ip.equals("52.78.100.19") && !ip.equals("52.78.48.223")){
            return ResponseEntity.ok(DataResponse.builder()
                    .message("fail")
                    .status(400)
                    .build());
        }*/

        String token = IamportUtil.getToken();
        Payment payment = api.paymentByImpUid(imp_uid).getResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> customDataMap = objectMapper.readValue(payment.getCustomData(), HashMap.class);

        IamportPaymentVo paymentVo = paymentService.getOnePayment(merchant_uid);
        //DB 결제금액과 아임포트 서버 결제 금액 비교
        if(paymentVo == null){
            if(status.equals("paid"))
            switch(payment.getCustomerUidUsage() != null ? payment.getCustomerUidUsage() : "null"){
                case "payment.scheduled":
                    saveIamportChargeHistory(payment);
                    message = "예약 결제 DB 등록";
                    break;
                case "payment":
                    saveIamportChargeHistory(payment);
                    message = "API 재결제 DB 등록";
                    break;
                case "null":
                    saveIamportChargeHistory(payment);
                    message = "일반결제 DB 등록";
                    break;
                case "issue":
                    message = "빌링키 발급건";
                    break;

            }
        }else if(paymentVo.getAmount().compareTo(payment.getAmount()) == 0 && !status.equals("cancelled")){
            switch(payment.getStatus()){
                case "ready":
                    message = "가상계좌개설";
                    break;
                case "paid" :
                    message = "일반결제완료";
                    break;
            }
        }else if(status.equals("cancelled")){
            Gson gson = new Gson();
            message = "관리자 취소";

            if(paymentVo.getCharge_seq() > 0){
                // IamportUtil.unSchedule(paymentVo.getCustomer_uid());
                chargeHistoryService.changeChargeBuyStatus(paymentVo.getCharge_seq());
            }else if(payment.getCancelAmount().compareTo(payment.getAmount()) == 0 && paymentVo.getH_idx() > 0){
                buyHistoryService.changeCancelState(paymentVo.getH_idx(), "Y");
            }
            paymentVo.setCancelled_at(payment.getCancelledAt().getTime()/1000);
            paymentVo.setCancel_reason(payment.getCancelReason());
            paymentVo.setCancel_amount(payment.getCancelAmount());
            paymentVo.setStatus(payment.getStatus());
            paymentVo.setReceipt_url(payment.getReceiptUrl());
            paymentService.modifyPayment(paymentVo);

        }else{
            message = "구매 금액 오등록 수정";
            paymentVo.setAmount(payment.getAmount());
            paymentService.modifyPayment(paymentVo);
        }

        return ResponseEntity.ok(DataResponse.builder()
                .message(message)
                .status(200)
                .build());
    }

    @ApiOperation(value="멤버쉽 환불 정보 조회")
    @GetMapping("/imp/membership/cancel")
    @ApiResponses({
            @ApiResponse(code = 200, message="success", response=CancelChargeHistoryVo.class)
    })
    public ResponseEntity<DataResponse> memberShipCancelInfo() throws AuthException {
        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
        CancelChargeHistoryVo memberShipVo = chargeHistoryService.getCancelMemberShipInfo(user.getUser_no());
        int refundAmount = memberShipVo.getSettle_amt() - (memberShipVo.getFee() + memberShipVo.getUse_date_amount());
        memberShipVo.setRefund_amount(refundAmount < 0 ? 0 : refundAmount);

        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(memberShipVo)
                .build());
    }

    @ApiOperation(value="아임포트 멤버쉽 환불")
    @PostMapping("/imp/membership/cancel")
    public ResponseEntity<?> iamportRegularPaymentCancel() throws AuthException, IamportResponseException, IOException {

        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
        CancelChargeHistoryVo memberShipVo = chargeHistoryService.getCancelMemberShipInfo(user.getUser_no());
        if(memberShipVo.getC_no() == 0 ){
            IamportPaymentVo paymentVo = paymentService.getOnePayment(memberShipVo.getCharge_seq());
            // IamportUtil.unSchedule(paymentVo.getCustomer_uid());

            //테스트모드 부분 환불 미활성화
            // long refundSum = memberShipVo.getSettle_amt() - memberShipVo.getUse_date_amount() - memberShipVo.getFee();

            long refundSum = memberShipVo.getSettle_amt();
            /*if(refundSum <= (int)GlobalEnum.get(GlobalEnum.REFUNDABLE_AMOUNT)){
                throw new ToonflixErrorException("환불 불가능 금액", ErrorCode.NOT_REFUNDABLE_AMOUNT);
            }*/
            if(refundSum < 0){
                refundSum = 0;
            }
            String jsonStr = IamportUtil.requestCancelPay(paymentVo,BigDecimal.valueOf(refundSum),"멤버쉽 취소");

            Payment payment = api.paymentByImpUid(paymentVo.getImp_uid()).getResponse();

            paymentVo.setCancelled_at(payment.getCancelledAt().getTime()/1000);
            paymentVo.setCancel_reason(payment.getCancelReason());
            paymentVo.setCancel_amount(payment.getCancelAmount());
            paymentVo.setStatus(payment.getStatus());
            paymentVo.setReceipt_url(payment.getReceiptUrl());
            paymentService.modifyPayment(paymentVo);
        }
        chargeHistoryService.changeChargeBuyStatus(memberShipVo.getCharge_seq());
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .build());
    }

    private void saveIamportChargeHistory(Payment payment) throws AuthException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> customDataMap = objectMapper.readValue(payment.getCustomData(), HashMap.class);
            String PgProvider = payment.getPgProvider().toUpperCase();

            String token = customDataMap.get("token").toString();

            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
            int mem_no = user.getUser_no();

            IamportPaymentVo paymentVo = IamportPaymentVo.buildIamportPaymentVo(payment);
            /*멤버쉽 구매 기록 DB 저장 */
            if(customDataMap.get("itemCode") != null) {

                String itemCode = customDataMap.get("itemCode").toString();
                ChargeVo chargevo = chargeService.getselectOneChargeItem(itemCode);

                if(chargeHistoryService.getMemberShipHistory(mem_no) != null){
                    String jsonData = IamportUtil.requestCancelPay(paymentVo, payment.getAmount(), "이미 멤버쉽을 보유하고 있습니다");
                    return;
                }

                ChargeHistoryVo vo = new ChargeHistoryVo();
                String chargeName = chargevo.getItem_name();

                vo.setMem_no(mem_no); vo.setItem_code(itemCode); vo.setItem_type(chargevo.getItem_type());
                vo.setItem_month(chargevo.getItem_month()); vo.setItem_name(chargeName); vo.setSettle_amt(chargevo.getPrice());
                vo.setNation_code(chargevo.getNation_code()); vo.setSettle_method(PgProvider); vo.setBuy_state("B");

                chargeHistoryService.saveChargeHistory(vo);
                paymentVo.setCharge_seq(vo.getCharge_seq());
            /*작품 대여,소장 기록 DB 저장 */

            }
            else{
                IamportUtil.requestCancelPay(payment, BigDecimal.ZERO,"결제오류발생 환불진행");
                throw new ToonflixErrorException("아임포트 에러 발생", ErrorCode.IAMPORT_SERVER_ERROR);
            }
            paymentService.savePayment(paymentVo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ToonflixErrorException("아임포트 에러 발생", ErrorCode.IAMPORT_SERVER_ERROR);
        }
    }
}