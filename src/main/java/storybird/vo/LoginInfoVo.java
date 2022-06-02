package storybird.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LoginInfoVo {
    //======================계정정보=================================
    @ApiModelProperty(value = "아이디")
    private String id;

    @ApiModelProperty(value = "비밀번호")
    @JsonIgnore
    private String pw;

    @ApiModelProperty(value = "프로필 이미지 저장 경로")
    private String profile_file_path;

    @ApiModelProperty(value = "프로필 이미지 원본명")
    private String profile_file_origin;

    @ApiModelProperty(value = "프로필 이미지 파일")
    private MultipartFile profile_file;

    @ApiModelProperty(value = "생년일자")
    private String birth_date;

    @ApiModelProperty(value = "연락처")
    private String phone;

    @ApiModelProperty(value = "성별")
    private String gender;
    //==============================================================
}
