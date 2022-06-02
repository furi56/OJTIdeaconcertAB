package storybird.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class DataResponse {
    @ApiModelProperty(example="상태코드")
    private int status;
    @ApiModelProperty(example="메세지")
    private String message;
    @ApiModelProperty(example="응답데이터")
    private Object data;
}
