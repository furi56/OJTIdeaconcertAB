package storybird.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import storybird.dao.WatchHistoryDAO;
import storybird.enums.PageSetting;
import storybird.security.ToonflixUsers;
import storybird.util.PaginationInfo;
import storybird.vo.WatchHistoryVo;

@Service
public class WatchHistoryService extends AbstractService{
	
	@Autowired
	WatchHistoryDAO watchDao;
	
	//작품 관람 기록 리스트 조회
	public PaginationInfo getWatchHistoryPagingList(int page, int recordCnt, String order) throws Exception{
		// AccessToken 기반 유저 정보 조회
		ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
		PaginationInfo pginfo = null;
		/*
		 * 사용테이블 opus
		 * 메소드가 받은 파라미터값을 사용해 watch_history 테이블내 정보를 조건에 맞게 조회하는 기능 구현(페이징)
		 * 값은 페이징이 되어야하고 order값에 따라 검색 조건이 변경되야한다.
		 * int page = 현재 페이지
		 * String recordCnt = 페이지당 나타나는 컨텐츠 수
		 * String order = 정렬방식(R:최근 재생순 - last_watch_date, M:최다 재생순 - w_cnt)
		 * ex) page=1, recordCnt=10, order=R 일때 1페이지에 표시될 관람 기록 10개를  최근 재생순으로 조회
		 *
		 * PaginationInfo 객체 반환필요
		 * [
		 *	pginfo.setRecordCountPerPage - 한페이지 당 컨텐츠수
		 *	pginfo.setTotalRecordCount - 전체 컨텐츠 수
		 *	pginfo.setPageSize - 노출되는 페이지 크기
		 *	pginfo.setCurrentPageNo - 현재페이지
		 *  pginfo.setList - 페이징된 데이터리스트
		 * ]
		 *
		 */
		return pginfo;
	}
	
	//작품 관람 기록 삭제
	public void removeWatchHistory(int seq, int memNo){
		watchDao.updateWatchHistoryDelStateBySeq(seq, memNo);
	}

	public void saveWatchHistory(int opusNo, int memNo, int epiSeq){
		/*
		 *  사용테이블 watch_history
		 *
		 *  유저가 작품을 열람했다는 기록을 추가, 수정하는 기능 구현
		 *
		 *  유저가 해당 작품 회차를 단 한번이라도 열람을 했는지 확인 후
		 * 		열람기록이 있다면 해당 유저가 열람한 작품, 회차 고유 번호를 사용해 watch_history.w_cnt 값 +1 추가
		 * 		열람기록이 없다면 해당 유자가 열람한 작품, 회차 고유 번호를 사용해 watch_history insert 실행
		 */

	}
}
