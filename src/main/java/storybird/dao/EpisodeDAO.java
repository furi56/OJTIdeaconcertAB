package storybird.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import storybird.vo.EpisodeFileVo;
import storybird.vo.EpisodeImgVo;
import storybird.vo.EpisodeVo;

import java.util.List;

@Mapper
public interface EpisodeDAO {
	public List<EpisodeVo> selectAllEpisode(@Param("start")int start, @Param("num")int num, @Param("opus_no")int opus_no);
	public List<EpisodeVo> selectOpusEpisodeAll(int opus_no, int mem_no, int rent_day, int start, int num, String order);
	public List<EpisodeFileVo> selectEpisodeFile(int opus_no, int epi_seq, String call_type);
	public List<EpisodeVo> selectEpisodeByOpusNo(int opus_no);
	public EpisodeVo selectEpisodeBySeq(int epi_seq);
	public int countEpisode();
	public int opuscountEpisode(int opus_no);
	public EpisodeVo OpenEpisode(int opus_no);
	public int countEpisodeByOpusNo(int opus_no);
	public int selectMaxEpisodeNumByOpusNo(int opus_no);
	public List<EpisodeVo> selectEpisodeViewrlist(@Param("opus_no")int opus_no,@Param("epi_seq")int epi_seq);
	public int selectNextEpisodeSeq();
	public EpisodeVo selectprebynext(@Param("opus_no")int opus_no,@Param("epi_seq")int epi_seq);
	public int selectMaxEpisodeNum();
	public int insertEpisode(EpisodeVo vo);
	public int updateEpisodeBySeq(EpisodeVo vo);
	public int updateEpisodeFreeBySeq(int epi_seq);

	public int updateEpisodeOpenBySeq(int epi_seq);
	public int updateEpisodeDeleteStateBySeq(int epi_seq);
	public int deleteEpisodeBySeq(int epi_seq);
}
