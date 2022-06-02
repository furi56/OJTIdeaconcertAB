package storybird.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import storybird.vo.WatchHistoryVo;

@Mapper
public interface WatchHistoryDAO {
	public List<WatchHistoryVo> selectAllWatchHistoryByMemNo(int mem_no, String order, int start, int num );
	public WatchHistoryVo selectWatchHistoryBySeq(int seq);
	public boolean checkWatchHistory(int mem_no, int opus_no, int epi_seq);
	public int countWatchHistoryByMemNo(int seq);
	public int insertWatchHistory(WatchHistoryVo vo);
	public int updateWatchHistoryDelStateBySeq(int seq, int mem_no);
	public int updateWatchHistoryCntByMemNo(int opus_no, int epi_seq, int mem_no);
	public int deleteWatchHistoryListBySeq(int seq);
}
