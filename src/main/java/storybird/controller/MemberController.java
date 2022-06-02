package storybird.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import storybird.security.ToonflixUsers;
import storybird.service.BuyHistoryService;
import storybird.service.ChargeHistoryService;
import storybird.service.MemberService;
import storybird.service.OpusService;
import storybird.util.IamportUtil;
import storybird.vo.DataResponse;
import storybird.vo.MemberVo;

import javax.security.auth.message.AuthException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * 플랫폼 회원 관련 기능 컨트롤러
 * 회원 정보 조회 및 수정, 탈퇴
 */

@RestController(value = "회원 컨트롤러")
public class MemberController {
    @Autowired
    ChargeHistoryService chargeHistoryService;
    @Autowired
    MemberService memberService;
    @Autowired
    OpusService opusService;

    @ApiOperation(value="현재 로그인 유저 상세 정보 조회")
    @GetMapping("/user")
    public ResponseEntity<?> userData() throws Exception{
        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
        MemberVo vo = memberService.getMemberDetail(user.getUser_no());
        vo.setPassword("");
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(vo)
                .build());
    }

    @ApiOperation(value="유저 상세 정보 조회")
    @GetMapping("/user/id/{id}")
    public ResponseEntity<?> userData(@PathVariable String id, String userType) throws Exception{
        MemberVo vo = memberService.getMemberDetail(id, userType);
        vo.setPassword("");
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(vo)
                .build());
    }

    @ApiOperation(value="유저 본인인증 진행")
    @PostMapping("/user/certifications")
    public ResponseEntity<DataResponse> userCertifications(String imp_uid) throws Exception{
        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
        JsonNode jNode = IamportUtil.certifications(imp_uid);

        String birthDate = jNode.get("birthday").asText(null);
        String gender = jNode.get("gender").asText(null);

        MemberVo memVo = new MemberVo();
        memVo.setGender(gender);
        //memVo.setBirth_year(Integer.parseInt(birthYear));
        memVo.setBirth_date(birthDate);
        memVo.setMem_no(user.getUser_no());
        memVo.setEvent_alarm_yn(null);
        memberService.modifyMember(memVo);
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .build());
    }

    @ApiOperation(value="유저 정보 수정")
    @PutMapping("/user")
    public ResponseEntity<?> modifyUserInfo(MemberVo vo, String oldPw) throws Exception{

        memberService.modifyMemberByBasic(vo, oldPw);

        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .build());
    }

    @ApiOperation(value="유저 탈퇴")
    @PutMapping("/user/secession")
    @ApiImplicitParams({
            @ApiImplicitParam(name="password", value="현재 비밀번호", type="string", required=true),
            @ApiImplicitParam(name="seces_feed", value="탈퇴사유(1:플랫폼사유,2:컨텐츠사유)", type="string", required=true),
            @ApiImplicitParam(name="seces_content", value="탈퇴전 피드백 내용", type="string", required=true)
    })
    public ResponseEntity<?> userSecession(MemberVo vo) throws Exception{
        memberService.secessionMember(vo);

        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .build());
    }

    @ApiOperation(value="현재 로그인 유저 멤버쉽 사용여부 조회(true:정액제사용중, false:X)")
    @GetMapping("/user/membership")
    public ResponseEntity<?> membershiprInfo() throws AuthException {
        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
        int mem_no = user.getUser_no();

        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(chargeHistoryService.getMemberShipHistory(mem_no) != null)
                .build());
    }

    @ApiOperation(value="현재 로그인 유저 정액제 열람 작품 조회")
    @GetMapping("/user/membership/opus/{opusNo}")
    public ResponseEntity<?> membershipOpusReadInfo(@PathVariable int opusNo){
        try{
            ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
            int mem_no = user.getUser_no();
            boolean isMemberShipOpus = opusService.getDetailOpus(opusNo).getVou_free_yn().equals("Y");
            return ResponseEntity.ok(DataResponse.builder()
                    .status(200)
                    .message("success")
                    .data(chargeHistoryService.getMemberShipHistory(mem_no) != null && isMemberShipOpus)
                    .build());
        }catch(AuthException | UsernameNotFoundException e){
            return ResponseEntity.ok(DataResponse.builder()
                    .status(200)
                    .message("success")
                    .data(false)
                    .build());
        }
    }

    @ApiOperation(value="현재 로그인 유저 본인인증 여부 조회(인증O: true, 인증X: false")
    @GetMapping("/user/check/certifications")
    public ResponseEntity<?> checkUserCertifications() throws AuthException {
        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();

        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(user.getOther_info().get("birthDate") != null)
                .build());
    }

    @ApiOperation(value="현재 로그인 유저 성인 인증 여부 조회(성인O: true, 성인X: false)")
    @GetMapping("/user/check/adult")
    public ResponseEntity<?> checkUserAdult() throws AuthException {
        ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();

        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(user.getOther_info().get("adultYn").equals("Y") ? true:false)
                .build());
    }
}
