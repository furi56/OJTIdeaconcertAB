package storybird.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import storybird.vo.EpisodeFileVo;

@Mapper
public interface EpisodeFileDAO {
	public List<EpisodeFileVo> selectEpisodeFileByEpiSeq(int epi_seq);
	
	public List<String> selectEpisodeFilePathByEpiSeq(int epi_seq);
	
	public String selectEpisodeFilePathByFileNo(int file_no);
	
	public int countEpisodeFileByEpiSeq(int epi_seq, String call_type);
	
	public int insertEpisodeFile(EpisodeFileVo vo);
	
	public int sortEpisodeOrder(int epi_seq, String call_type);

	public int updateEpisodeFileDeleteStateByEpiSeq(int epi_seq);

	public int updateEpisodeFileDeleteStateByFileNo(int file_no);

	public int deleteEpisodeFileByEpiSeq(int epi_seq);
	
	public int deleteEpisodeFileByFileNo(int file_no);
}
