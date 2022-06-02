package storybird.security.sns;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SnsDataRequestService {
    public ResponseEntity<String> requestNaverProfile(HttpEntity request){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
          "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                request,
                String.class
        );
    }

    public HttpEntity<MultiValueMap<String, String>> generateProfileRequest(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return new HttpEntity<>(headers);
    }

    public HttpEntity<MultiValueMap<String, String>> generateAuthCodeRequest(String code, String state){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id","NQcxgbnVuVcZEhFqucA8");
        params.add("client_secret", "HFZY58atlD");
        params.add("code",code);
        return new HttpEntity<>(params, headers);
    }

    public ResponseEntity<String> requestAccessToken(HttpEntity request){
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
          "https://nid.naver.com/oauth2.0/token",
          HttpMethod.POST,
          request,
          String.class
        );
    }
}
