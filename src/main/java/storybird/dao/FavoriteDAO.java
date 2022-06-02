package storybird.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import storybird.vo.FavoriteVo;
import storybird.vo.OpusVo;

import java.util.List;
@Mapper
public interface FavoriteDAO {
    public List<OpusVo> selectFavOpusListByMemNo(int no, int start, int num);
    public List<FavoriteVo> selectAllFavByMemNo(int mem_no, int start, int num);
    public boolean FavSelect(int opus_no, int mem_no);
    public int countFavSelect(int mem_no);
    public int countFavOpusListByMemNo(int no);
    public void insertOpusFavorite(int opus_no,int mem_no);
    public void deleteOpusFavorite(int opus_no,int mem_no);
    public void deleteOpusFavoriteBySeq(int seq);
}
