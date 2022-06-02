package storybird.vo.join;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StarScoreAvgVo {
    @ApiModelProperty(value="별점 평균값")
    private double avg_score;

    @ApiModelProperty(value="별점 투표 횟수")
    private int cnt;
}
