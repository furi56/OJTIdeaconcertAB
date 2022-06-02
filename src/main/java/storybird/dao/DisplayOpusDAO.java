package storybird.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import storybird.vo.DisplayOpusVo;
import storybird.vo.OpusVo;

import java.util.List;

@Mapper
public interface DisplayOpusDAO {
    public List<DisplayOpusVo> selectDisplayOpusByType(@Param("start")int start, @Param("num")int num, @Param("search")String search, @Param("dsp_type")String dsp_type, @Param("genre")String genre,
                                                @Param("day_week")String day_week, @Param("opus_kind")String opus_kind, @Param("start_date")String start_date,
                                                       @Param("lang_code")String lang_code);
    public int countDisplayOpusByType(@Param("search")String search, @Param("dsp_type")String dsp_type, @Param("genre")String genre, @Param("day_week")String day_week, @Param("opus_kind")String opus_kind,
                                      @Param("start_date")String start_date, @Param("lang_code") String lang_code);
    public boolean selectLastOrderByNo(int no, String dsp_type, String opus_kind, String genre);
    public boolean selectFirstOrderByNo(int no, String dsp_type, String opus_kind, String genre);
    public boolean findDuplicateDisplay(DisplayOpusVo vo);
    public boolean findDuplicateDisplayByOpusNo(int opus_no);
    public DisplayOpusVo selectDisplayOpusByNo(int no);
    public DisplayOpusVo selectDropOrderByNo(int dsp_order, String opus_kind, String dsp_type, String genre);
    public DisplayOpusVo selectRiseOrderByNo(int dsp_order, String opus_kind, String dsp_type, String genre);
    public List<OpusVo> selectDisplayOpusToOpusVo(@Param("start")int start, @Param("num")int num, @Param("dsp_type")String dsp_type, @Param("genre")String genre,
                                                  @Param("day_week")String day_week, @Param("opus_kind")String opus_kind, @Param("order")String order);
    public int countDisplayOpusToOpusVo(@Param("dsp_type")String dsp_type, @Param("genre")String genre,
                                        @Param("day_week")String day_week, @Param("opus_kind")String opus_kind);

    public int insertDisplayOpus(DisplayOpusVo vo);

    public int sortDisplayOpusOrder(String dsp_type, String lang_code, String opus_kind);
    public int updateOrderRiseByNo(DisplayOpusVo vo);
    public int updateAfterOrderRiseByNo(int dsp_no);
    public int updateOrderDropByNo(DisplayOpusVo vo);
    public int updateOrderByNo(int dsp_no, int dsp_order);
    public int updateDisplayOpusDeleteStateByNo(int no);
    public int deleteDisplayOpusByNo(int no);
}
