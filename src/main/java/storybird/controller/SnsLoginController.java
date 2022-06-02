package storybird.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SNS 로그인(네이버) 컨트롤러
 */

@Controller
public class SnsLoginController {

    @Autowired
    storybird.security.sns.SnsDataRequestService snsService;

    @GetMapping("/login/naver/auth")
    @ResponseBody
    public String authNaver(String accessToken){
        HttpEntity<?> entity = snsService.generateProfileRequest(accessToken);
        return snsService.requestNaverProfile(entity).getBody();
    }
}
