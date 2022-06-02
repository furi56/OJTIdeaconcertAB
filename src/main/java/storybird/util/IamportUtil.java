package storybird.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import storybird.enums.ErrorCode;
import storybird.enums.GlobalEnum;
import storybird.exception.ToonflixErrorException;
import storybird.vo.IamportPaymentVo;
import storybird.vo.IamportTokenVo;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class IamportUtil {

    private static String API_KEY = "API_KEY";
    private static String API_SECRET = "API_SECRET";

    //아임포트 토큰발급
    public static String getToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("imp_key", API_KEY);
        map.put("imp_secret", API_SECRET);

        Gson var = new Gson();
        String json = var.toJson(map);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return restTemplate.postForObject("https://api.iamport.kr/users/getToken", entity, String.class);
    }

    public static String requestSubPay(IamportPaymentVo payment) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> customDataMap = objectMapper.readValue(payment.getCustom_data(), HashMap.class);

        String token = getToken();
        Gson str = new Gson();
        token = token.substring(token.indexOf("response") + 10);
        token = token.substring(0, token.length() - 1);

        IamportTokenVo vo = str.fromJson(token, IamportTokenVo.class);

        String access_token = vo.getAccess_token();
        System.out.println(access_token);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(access_token);

        Map<String, Object> map = new HashMap<>();
        map.put("customer_uid", payment.getCustomer_uid());
        map.put("merchant_uid", "mer_storybird_" + System.currentTimeMillis());
        map.put("amount", payment.getAmount());
        map.put("name", payment.getName());
        map.put("buyer_name", payment.getBuyer_name());
        map.put("custom_data", payment.getCustom_data());
        map.put("notice_url", GlobalEnum.get(GlobalEnum.IAMPORT_WEBHOOK_URL));

        Gson var = new Gson();
        String json = var.toJson(map);
        System.out.println(json);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        String jsonData = restTemplate.postForObject("https://api.iamport.kr/subscribe/payments/again", entity, String.class);
        ImpRestApiResponseDataVerify(jsonData);
        // restTemplate.exchange("https://api.iamport.kr/subscribe/customers/"+payment.getCustomer_uid(), HttpMethod.DELETE, entity, String.class);
        return jsonData;
    }

    public static String scheduleMonthPay(Payment payment, int paymentDay) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> customDataMap = objectMapper.readValue(payment.getCustomData(), HashMap.class);

        String token = getToken();
        long timestamp = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        cal.add(Calendar.MONTH, +1);
        if (cal.getActualMaximum(Calendar.DAY_OF_MONTH) < paymentDay) {
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        } else {
            cal.set(Calendar.DATE, paymentDay);
        }

        String date = sdf.format(cal.getTime());
        try {
            Date stp = sdf.parse(date);
            timestamp = stp.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Gson str = new Gson();
        token = token.substring(token.indexOf("response") + 10);
        token = token.substring(0, token.length() - 1);
        IamportTokenVo vo = str.fromJson(token, IamportTokenVo.class);
        String access_token = vo.getAccess_token();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(access_token);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("merchant_uid", "mer_storybird_" + System.currentTimeMillis());
        jsonObject.addProperty("schedule_at", timestamp);
        jsonObject.addProperty("amount", payment.getAmount());
        jsonObject.addProperty("custom_data", payment.getCustomData());
        jsonObject.addProperty("notice_url", (String) GlobalEnum.get(GlobalEnum.IAMPORT_WEBHOOK_URL));

        JsonArray jsonArr = new JsonArray();

        jsonArr.add(jsonObject);
        JsonObject reqJson = new JsonObject();

        reqJson.addProperty("customer_uid", payment.getCustomerUid());
        reqJson.add("schedules", jsonArr);
        String json = str.toJson(reqJson);
        System.out.println(json);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        String jsonData = restTemplate.postForObject("https://api.iamport.kr/subscribe/payments/schedule", entity, String.class);
        ImpRestApiResponseDataVerify(jsonData);
        return jsonData;
    }

    public static String requestCancelPay(Payment payment, BigDecimal cancelRequestAmount, String reason) {
        IamportPaymentVo paymentVo = IamportPaymentVo.buildIamportPaymentVo(payment);
        return requestCancelPay(paymentVo, cancelRequestAmount, reason);
    }

    public static String requestCancelPay(IamportPaymentVo paymentVo, BigDecimal cancelRequestAmount, String reason) {
        String token = getToken();
        Gson str = new Gson();
        token = token.substring(token.indexOf("response") + 10);
        token = token.substring(0, token.length() - 1);

        IamportTokenVo tokenVo = str.fromJson(token, IamportTokenVo.class);

        String access_token = tokenVo.getAccess_token();

        BigDecimal amount = paymentVo.getAmount() == null ? BigDecimal.ZERO : paymentVo.getAmount();
        BigDecimal cancelAmount = paymentVo.getCancel_amount() == null ? BigDecimal.ZERO : paymentVo.getCancel_amount();
        cancelRequestAmount = cancelRequestAmount.compareTo(BigDecimal.ZERO) == 0 ? amount : cancelRequestAmount;

        BigDecimal cancelableAmount = amount.subtract(cancelAmount);
        if (cancelableAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ToonflixErrorException("전액환불건 처리불가", ErrorCode.IAMPORT_REFUND_ORDER);
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(access_token);

        Map<String, Object> map = new HashMap<>();
        map.put("reason", reason);
        map.put("imp_uid", paymentVo.getImp_uid());
        map.put("amount", cancelRequestAmount);
        map.put("checksum", cancelableAmount);
        map.put("notice_url", GlobalEnum.get(GlobalEnum.IAMPORT_WEBHOOK_URL));

        Gson var = new Gson();
        String json = var.toJson(map);
        System.out.println(json);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        String jsonData = restTemplate.postForObject("https://api.iamport.kr/payments/cancel", entity, String.class);
        ImpRestApiResponseDataVerify(jsonData);
        return jsonData;
    }

    public static String unSchedule(String customerUid) {
        String token = getToken();
        Gson str = new Gson();
        token = token.substring(token.indexOf("response") + 10);
        token = token.substring(0, token.length() - 1);

        IamportTokenVo vo = str.fromJson(token, IamportTokenVo.class);

        String access_token = vo.getAccess_token();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(access_token);

        Map<String, Object> map = new HashMap<>();
        map.put("customer_uid", customerUid);

        Gson var = new Gson();
        String json = var.toJson(map);
        System.out.println(json);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        String jsonData = restTemplate.postForObject("https://api.iamport.kr/subscribe/payments/unschedule", entity, String.class);
        restTemplate.exchange("https://api.iamport.kr/subscribe/customers/" + customerUid, HttpMethod.DELETE, entity, String.class);
        ImpRestApiResponseDataVerify(jsonData);
        return jsonData;
    }

    public static JsonNode certifications(String impUid) throws IOException {
        try {
            String token = getToken();
            Gson str = new Gson();
            token = token.substring(token.indexOf("response") + 10);
            token = token.substring(0, token.length() - 1);

            IamportTokenVo vo = str.fromJson(token, IamportTokenVo.class);

            String access_token = vo.getAccess_token();

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(access_token);

            Map<String, Object> map = new HashMap<>();

            Gson var = new Gson();
            String json = var.toJson(map);
            System.out.println(json);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            ResponseEntity<String> resEntity = restTemplate.exchange("https://api.iamport.kr/certifications/" + impUid, HttpMethod.GET, entity, String.class);
            ObjectMapper objMapper = new ObjectMapper();
            JsonNode jsonNode = objMapper.readTree(resEntity.getBody());
            if (jsonNode.get("code").asInt() != 0) {
                throw new ToonflixErrorException("본인인증 오류", ErrorCode.IAMPORT_NOT_TRANSACTION);
            }
            return jsonNode.path("response");
        } catch (Exception e) {
            throw new ToonflixErrorException("본인인증 오류", ErrorCode.IAMPORT_NOT_TRANSACTION);
        }
    }

    private static void ImpRestApiResponseDataVerify(String jsonData) {
        try {
            System.out.println("ImpRestApiResponseDataVerify >> " + jsonData);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> paymentResultMap = objectMapper.readValue(jsonData, HashMap.class);
            String resData = jsonData.substring(jsonData.indexOf("response") + 10);
            // resData = resData.substring(0, resData.length() - 1).replaceAll("[\\[\\[\\]]","");
            Map<String, Object> responseResultMap;
            if (resData.substring(0, 1).equals("[")) {
                ArrayList<Map<String, Object>> mapList = objectMapper.readValue(resData, ArrayList.class);
                responseResultMap = mapList.get(0);
            } else {
                responseResultMap = objectMapper.readValue(resData, HashMap.class);
            }
            if (Integer.parseInt(paymentResultMap.get("code").toString()) == 0) {
                String status = responseResultMap.get("status") == null ? "empty" : responseResultMap.get("status").toString();
                System.out.println("status >> " + status);
                if (status.equals("paid") || status.equals("empty")) {
                    //결제 정상 승인
                    return;
                } else if (status.equals("ready")) {
                    //가상계좌 발급 완료
                    return;
                } else if (status.equals("cancelled")) {
                    //환불 정상 승인
                    return;
                } else {
                    //결제 승인 실패 (예: 고객 카드 한도초과, 거래정지카드, 잔액부족 등)
                    //paymentResultMap.status : falied로 수신
                    throw new ToonflixErrorException(paymentResultMap.get("message").toString(), ErrorCode.IAMPORT_STATUS_FAIL);
                }
            } else {
                //결제 요청 실패 paymentResult is null
                throw new ToonflixErrorException(paymentResultMap.get("message").toString(), ErrorCode.IAMPORT_REQUEST_FAIL);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ToonflixErrorException("결제 요청 실패", ErrorCode.IAMPORT_REQUEST_FAIL);
        }
    }
}
