package storybird.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class PagingResponse {
    private int page;
    private int perpage;
    private int totalpage;
    private int totalrecordcount;
    private Object data;
}
