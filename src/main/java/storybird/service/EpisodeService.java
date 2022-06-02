package storybird.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import storybird.dao.EpisodeDAO;
import storybird.dao.EpisodeFileDAO;
import storybird.dao.OpusDAO;
import storybird.enums.*;
import storybird.exception.ToonflixErrorException;
import storybird.security.ToonflixUsers;
import storybird.util.FileUtil;
import storybird.util.PaginationInfo;
import storybird.vo.EpisodeFileVo;
import storybird.vo.EpisodeVo;
import storybird.vo.OpusVo;

import javax.security.auth.message.AuthException;
import java.util.List;

@Service
public class EpisodeService extends AbstractService{

	@Autowired EpisodeDAO episodeDao;
	@Autowired OpusDAO opusDao;
	@Autowired EpisodeFileDAO episodeFileDao;
	//작품회차 상세 정보
	public EpisodeVo getDetailEpisode(int epiSeq) {
		/*
		* 사용테이블 episode, opus
		* 작품회차 고유번호(epiSeq)를 사용해 회차 상세 정보를 조회하는 기능 구현
		*
		* 반환값
		* [
		* 	episode : 전체
		*   opus : 작품명(opus_title)
		* ]
		*/
		return episodeDao.selectEpisodeBySeq(epiSeq);
	}

	public List<EpisodeVo> getEpisodeOpusAllList(int opus_no, String order) {
		/*
		 * 사용테이블 episode, opus_history, episode_file
		 *
		 * 작품에 해당하는 회차 리스트 정보를 조회하는 기능 구현
		 * 작품회차 고유번호(epi_seq)를 기준으로 다른 테이블과 Join하여 아래와 같은 형태로 값을 반환해야함
		 * episode_file, opus_history는 episdoe 테이블의 epi_seq를 fk로 가지고있다.
		 * String order의 값에 따라 인기순:HOT, 회차 내림차순:NEW, 회차 오름차순:OLD 으로 정렬한다.
		 * 반환값
		 * [
		 * 	episode : 회차고유번호(epi_seq), 회차제목(epi_title), 회차 번호(epi_num), 무료여부(free_yn)
		 * 	episode_file : 각 회차별 재생 시간(별칭명은 epi_duration으로 표시)
		 * 	opus_history : 회차 구매 횟수 (count함수 사용, 별칭명은 opus_cnt로 표시)
		 *  정렬 : {
		 * 		인기순 : 회차 구매 횟수(opus_cnt)를 사용
		 * 		회차 내림차순 : 회차 번호(episode.epi_num) 사용
		 * 		회차 오름차순 : 회차 번호(episode.epi_num) 사용
		 *  }
		 * ]
		 *
		 */
		return null;
	}


	public EpisodeVo getEpisodePrebyNext(EpisodeVo vo, int opusNo, int epiSeq) {
		/*
		*  사용테이블 episode
		*
		*  작품,회차 고유번호(opusNo,epiSeq)를 사용해 다음화차와 이전화차의 고유 번호를 반환해준다.
		*  ex) Vo(Dto)객체의 set메소드로 값을 저장하고 객체를 반환 vo.setNextSeq, vo.setPreSeq
		*
		*  반환값
		*  [
		*    episode : 이전회차고유번호(별칭명 preSeq), 다음회차고유번호(별칭명 nextSeq)
		* 		이전, 다음회차를 구하기 위해선 아래와 같은 방법을 예시로 들 수 있다.
		* 		ex) 이전회차 : 작품고유번호, 회차고유번호가 동일한 episode 테이블 내 데이터 회차 번호(epi_num)오름차순 정렬 후
		* 					가장 첫번째 row 조회
		*			다음회차 : 작품고유번호, 회차고유번호가 동일한 episode 테이블 내 데이터 회차 번호(epi_num)내림차순 정렬 후
		 * 					가장 첫번째 row 조회
		*  ]
		*
		*  현재 반환값이 EpisodeVo로 되어있지만 변경하여도 상관없음
		*  (결과값으로 이전, 다음 회차 고유 번호를 넘겨줄 수 있어야함)
		*/
		return vo;
	}

	public List<EpisodeFileVo> getEpisodeFile(int opus_no, int epi_seq, String callType) {
		/*
		 *  사용테이블 episode_file
		 *
		 *  작품,회차 고유번호(opusNo,epiSeq)를 사용해 해당 회차의 파일 정보를 조회한다
		 *
		 *  반환값
		 *  List[
		 *    episode_file : 전체
		 * 		정렬 {
		 * 			episode_file.order 오름차 순으로 정렬 필요
		 * 		}
		 *  ]
		 *
		 */
		return null;
	}
}