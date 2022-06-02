package storybird.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"), //입력값 오류
    METHOD_NOT_ALLOWED(405, "C002", " Method Not Allowed"), //존재하지않는 메소드
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"), //엔티티를 찾을 수 없음
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"), // 서버에러
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"), // 유효하지않은 타입
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"), //권한 필요
    INVALID_INPUT_FILE_TYPE(400, "C007" ,"Invalid Input File Type"), //유효하지않은 파일 타입
    INVALID_INPUT_DATE(400, "C008", " Invalid Input Date"), // 유효하지않은 날짜값
    CANNOT_BE_REGISTRATION(400, "C009", "Cannot be Registration"), //등록할 수 없는 값
    CANNOT_BE_REMOVE(400, "C010", "Cannot be Remove"), //삭제할 수 없는 값
    CONNECTION_ACCESS_FAIL(400, "C011", "Connection Access Fail"), //권한토큰 없음 접근 실패
    CANNOT_ACCESS_PAGE(400, "C012", "Cannot Access Page"), //접근 불가 페이지
    DUPLICATE_VALUE(400, "C013", "Duplicate Value"), //중복값 제한
    JWT_TOKEN_EXPIRATION(403, "C100", "JWT Token expiration"), //접근 허가 토큰 만료
    
    // Member
    USER_NOT_FOUND(403, "M000", "User Not Found"), //유저를 찾을 수 없음
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"), //이메일 중복됨
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"), //유효하지않은 로그인정보
    PASSWORD_MISMATCH(400, "M003", "Password mismatch"), //비밀번호 틀림
    PASSWORD_FORMAT_NOT_CORRECT(400, "M004", "Password format is Not Correct"), //비밀번호 형식이 올바르지않음
    INVALID_AUTH_CODE(400, "M005", "Invalid Input Authentication code"), //유효하지않는 보안코드
    AFTER_SIX_MONTHS_OF_ACCESS(400, "M006", "After 6 months of access"), //접속한지 6개월이 경과함
    // NEED_SIGN_UP(400, "M007", "Need Sign Up"), //로그인 필요
    USE_MEMBERSHIP(400, "M008", "Membership already in use"), //멤버쉽 사용중

    // Admin
    ID_DUPLICATION(400, "A001", "ID is Duplication"), //아이디 중복
    BUSINESS_NUMBER_DUPLICATION(400, "A001", "Business Number is Duplication"), //사업자번호 중복
    CONTRACT_NOT_EXPIRED(400, "A002", "Contract Not Expired"), //계약이 만료되지않음
    REGISTERED_EPISODE_EXIST(400, "A003", "Registered Episode exist"), //등록된 회차정보가 존재함
    USED_COUPON(400, "A004", "Used Coupon"), //사용된 쿠폰임
    GIFT_NOT_EXPIRED(400, "A005", "Gift Not expired"), //선물 유효기간이 만료되지않음
    DISPLAY_DUPLICATION(400, "A006","Display Duplication"), // 진열관리 중복되는 값
    CANNOT_BE_CHANGED(400, "A007","Cannot be Changed"), // 변경할 수 없는 값

    //Payment
    LACK_OF_CASH(400, "P000", "Lack of Cash"), //캐시가 모자름
    NO_BUY_HISTORY(400, "P001", "No Buy History"), //구매 기록이 존재하지않음
    MORE_THAN_THE_REFUND(400, "P002", "More than the refund."), //환불 횟수 초과
    INCORRECT_AMOUNT(400, "P003", "Incorrect Amount"), //결제 금액 불일치
    NOT_REFUNDABLE_AMOUNT(400, "P004", "Not refundable amount"), //환불 가능 금액 없음

    //File
    INVALID_EXTENSION(400, "F001", "Invalid Extension"), //허용되지않은 확장자명

    // Iamport
    IAMPORT_UNAUTHORIZED(401, "IP401", "Iamport Unauthrized"), //아임포트 접근권한 없음
    IAMPORT_STATUS_FAIL(400, "IP001", "Iamport Transaction failed" ), //아임포트 거래중 요류
    IAMPORT_REQUEST_FAIL(400, "IP002", "Iamport Request failed." ), //아임포트 API 요청 오류
    IAMPORT_REFUND_ORDER(400, "IP003", "Order that has already been refunded." ), //이미 환불이 완료된 건
    IAMPORT_NOT_TRANSACTION(404, "IP404", "Can't check the transaction details"), //거래내역 확인불가
    IAMPORT_SERVER_ERROR(500, "IP500", "Server Error"),
	;
	
	private final String code;
	private final String message;
	private int status;
	
	private ErrorCode(final int status, final String code, final String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}
    public boolean equals(String code) {
        return this.code.equals(code);
    }
    public static ErrorCode of(String code) {
        return Arrays.stream(ErrorCode.values())
                .filter(user -> user.equals(code))
                .findAny()
                .orElse(INTERNAL_SERVER_ERROR);
    }
}
