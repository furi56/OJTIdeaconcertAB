package storybird.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import storybird.enums.FilePath;
import storybird.repository.ExampleRepository;
import storybird.security.JwtTokenProvider;
import storybird.security.JwtUserDetailsService;
import storybird.security.ToonflixUsers;
import storybird.service.MemberService;
import storybird.util.FileUtil;
import storybird.util.PaginationInfo;
import storybird.vo.DataResponse;
import storybird.vo.IamportPaymentVo;
import storybird.vo.MemberVo;
import storybird.vo.PagingResponse;

import javax.security.auth.message.AuthException;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
기능 테스트 컨트롤러
**/
@Controller
public class TestController {

    @Autowired private JwtUserDetailsService jwtUserDetailService;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired
    ExampleRepository exampleRepository;

    @Autowired
    MemberService memberService;

    @GetMapping("/user/token")
    public ResponseEntity<DataResponse> getUserToken() {
        String token = jwtTokenProvider.createToken(
                "qdsa09@gmail.com", Collections.singletonList("USER"), "BS");
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return ResponseEntity.ok(DataResponse.builder()
                .message("Token access")
                .status(200)
                .data(tokenMap)
                .build());
    }
    @GetMapping("/user/exp/token")
    public ResponseEntity<DataResponse> getUserExpiredToken() {
        String token = jwtTokenProvider.createToken(
                "qdsa09@gmail.com", Collections.singletonList("USER"), "BS", 0);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return ResponseEntity.ok(DataResponse.builder()
                .message("Token access")
                .status(200)
                .data(tokenMap)
                .build());
    }


    @GetMapping("/admin/token")
    public ResponseEntity<DataResponse> getAdminToken() {
        String token = jwtTokenProvider.createToken(
                "ideaconcert", Collections.singletonList("ADMIN"), "");
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return ResponseEntity.ok(DataResponse.builder()
                .message("Token access")
                .status(200)
                .data(tokenMap)
                .build());
    }

    //토큰 정보 조회
    @PostMapping("/token")
    public ResponseEntity<List<String>> test(String token) {
        System.out.println("==================Token Info================");
        System.out.println(jwtTokenProvider.getUserIdFromToken(token));
        System.out.println(jwtTokenProvider.getExpirationDateFromToken(token));
        System.out.println(jwtTokenProvider.getUserRole(token));
        return ResponseEntity.ok(jwtTokenProvider.getUserRole(token));
    }

    //토큰 생성 테스트
    @GetMapping("/test/token")
    public ResponseEntity<DataResponse> tokenTest() {
        String id = "qdsa09@gmail.com";
        final ToonflixUsers userDetails = jwtUserDetailService.loadUserByUsername(id, "BS");
        String refreshToken = jwtTokenProvider.createRefreshToken();
        String accessToken = jwtTokenProvider.createToken(id, userDetails.getRoles(), "BS");

        HttpHeaders headers = new HttpHeaders();
        headers.set("accessToken", accessToken);
        headers.set("refreshToken", refreshToken);

        return new ResponseEntity<>(DataResponse.builder()
                .status(200)
                .message("success")
                .build(), headers, HttpStatus.OK);
    }

    //토큰 발급
    @GetMapping("/test/token/issue")
    public ResponseEntity<DataResponse> tokenTest2(String id, String userType, long accessTime){
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_BASIC");
        String refreshToken = jwtTokenProvider.createRefreshToken();
        //crateToken
        String accessToken = jwtTokenProvider.createToken(id, roles, userType, accessTime);

        HttpHeaders headers = new HttpHeaders();
        headers.set("accessToken", accessToken);
        headers.set("refreshToken", refreshToken);

        return new ResponseEntity<>(DataResponse.builder()
                .status(200)
                .message("success")
                .build(), headers, HttpStatus.OK);
    }

    //요청시 받은 로그인토큰 정보 조회
    @GetMapping("/test/user")
    @ResponseBody
    public ToonflixUsers getUser() throws AuthException {
        return ToonflixUsers.getAuthorizedUsers();
    }

    //비밀번호 인코딩
    @GetMapping("/test/pw/enc")
    @ResponseBody
    public String encPW(String pw) {
        return new BCryptPasswordEncoder().encode(pw);
    }

    //403 에러 강제 발생
    @GetMapping("/test/error/403")
    @ResponseBody
    public void test1() {
        throw new UsernameNotFoundException("Not AuthorizedUser");
    }

    //기본 반환 JSON
    @GetMapping("/test/data")
    public ResponseEntity<DataResponse> getData(){
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data("data")
                .build());
    }
    //페이징 반환 JSON
    @GetMapping("/test/paging")
    public ResponseEntity<PagingResponse> getPaging(){
        PaginationInfo info = new PaginationInfo();

        List<String> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add((i+1)+"번");
        }


        info.setRecordCountPerPage(10);
        info.setTotalRecordCount(100);
        info.setPageSize(5);
        info.setCurrentPageNo(1);
        info.setList(list);

        return ResponseEntity.ok(info.getPagingResponse());
    }

    //파일 업로드 테스트
    @PostMapping("/test/upload")
    public ResponseEntity<DataResponse> upload(MultipartFile file, String savePath) throws Exception {
        System.out.println(savePath.substring(0, savePath.lastIndexOf("/")));
        FileUtil.mkDirs(FilePath.DEFAULT_UPLOAD_PATH + savePath.substring(0, savePath.lastIndexOf("/")));
        File saveFile = new File(FilePath.DEFAULT_UPLOAD_PATH+savePath);

        file.transferTo(saveFile);

        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .build());
    }

    @GetMapping("/test/jpa")
    public ResponseEntity<DataResponse> test12(){
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(exampleRepository.findAll())
                .build());
    }

    @GetMapping("/test/batis")
    public ResponseEntity<DataResponse> test123(){
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .data(memberService.getMemberDetail(1))
                .build());
    }

    @GetMapping("/test/valid")
    public ResponseEntity<DataResponse> testValid(MemberVo vo){
        return ResponseEntity.ok(DataResponse.builder()
                .status(200)
                .message("success")
                .build());
    }

}
