package storybird.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import storybird.dao.EpisodeFileDAO;
import storybird.vo.EpisodeFileVo;

@Service
public class EpisodeFileService {
	
	@Autowired
	EpisodeFileDAO episodeFileDao;
	
	
	public List<String> getEpisodeFilePath(int epiSeq){
		return episodeFileDao.selectEpisodeFilePathByEpiSeq(epiSeq);
	}
	
	public List<EpisodeFileVo> getEpisodeFile(int epiSeq){
		return episodeFileDao.selectEpisodeFileByEpiSeq(epiSeq);
	}
}
