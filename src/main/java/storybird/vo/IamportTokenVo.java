package storybird.vo;

import lombok.Data;

@Data
public class IamportTokenVo {
    private String access_token;
    private long now;
    private long expired_at;
}
