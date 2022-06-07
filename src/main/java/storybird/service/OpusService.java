package storybird.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import storybird.dao.EpisodeDAO;
import storybird.dao.OpusDAO;
import storybird.enums.PageSetting;
import storybird.util.PaginationInfo;
import storybird.vo.OpusVo;

import java.time.ZoneId;
import java.util.*;

@Service
public class OpusService extends AbstractService {

	@Autowired
	OpusDAO opusDao;

	@Autowired
	EpisodeDAO epiDao;

	// 작품 상세보기
	public OpusVo getDetailOpus(int opusNo) {
		/*
		*  테이블 opus, episode, episode_file, opus_history
		*
		* 작품 상세 정보를 조회하는 기능 구현
		* 작품 고유번호(opus_no)를 기준으로 다른 테이블과 Join하여 아래와 같은 형태로 값을 반환해야함
		* episode, episode_file, opus_history는 opus테이블의 opus_no를 fk로 가지고있다.
		* 반환값
		* [
		* 	opus : 작품번호(opus_no), 작품명(opus_title), 장르(genre)
		* 	episode : 작품에 포함된 에피소드 갯수(count함수 사용, 별칭명은 epi_cnt 로 표시)
		* 	episode_file : 에피소드 재생 시간 총합(sum함수 사용, episode_file.file_duration 합산, 별칭명은 epi_duration으로 표시)
		* 	opus_history : 작품 구매(열람) 횟수 총합(count함수 사용, 별칭명은 opus_cnt로 표시
		* ]
		*
		*/
		return null;
	}

	public Map<String,Object> getLangCodeOpusList(int opusNo){
		List<OpusVo> list = opusDao.selectLangCodeOpusListByNo(opusNo);
		Map<String, Object> resultMap = new HashMap<>();
		for(OpusVo temp : list){
			resultMap.put(temp.getLang_code(),temp.getOpus_no());
		}
		return resultMap;
	}

	public List<OpusVo> getTimeOpus() {
		/*
		* 사용테이블 opus, opus_history
		* 1. opus_history에 등록된 데이터를 작품 번호(opus_no)로 그룹화 시키고 총갯수를  구한다(count함수)
		* (해당 값은 랭킹 순위를 나누기 위한 용도로 사용됨)
		* 2.(1)에서 구한 값과 opus 테이블을 조인시켜 아래와 같은 값이 나오도록 기능을 구현한다.
		*  - 결과값으로 순위, 작품 번호(opus_no), 작품명(opus_title), 총갯수(count)가 출력되고 오름차순으로 1~5위까지 나와야한다.
		*  - 순위를 구할때 사용할 수 있는 mysql함수는 ROW_NUMBER, RANK 등이 있다.
		*/
		return Collections.EMPTY_LIST;
	}
	public PaginationInfo getTimeRankingOpus(int page, String genre, int type, String kind) {
		String dateType;
		int dayNum;
		int total = opusDao.countGenreOpus(genre, kind);
		PaginationInfo pgInfo = getPaginationInfo(page, PageSetting.RANK_PER_PAGE_NUM.get(), total, PageSetting.PAGE_SIZE.get());

		switch (type){
			case 1: dayNum = 1; dateType = "DAY"; break;
			case 2: dayNum = 7; dateType = "DAY"; break;
			case 3: dayNum = 1; dateType = "MONTH"; break;
			case 4: dayNum = 1; dateType = "YEAR"; break;
			default :
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")));
				String standard_date = "";
				System.out.println(cal.get(Calendar.HOUR));
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if(cal.get(Calendar.MINUTE) > 30){
					standard_date = year+"/"+month+"/"+day+"/"+hour+"/30";
				}else{
					standard_date = year+"/"+month+"/"+day+"/"+hour+"/00";
				}

				pgInfo.setList(opusDao.realTimeRankingOpus(genre, total, pgInfo.getFirstRecordIndex(), pgInfo.getRecordCountPerPage(), kind, standard_date));
				return pgInfo;
		}
		pgInfo.setList(opusDao.dateRankingOpus(genre,dateType,dayNum, total, pgInfo.getFirstRecordIndex(), pgInfo.getRecordCountPerPage(), kind));
		return pgInfo;
	}
	public PaginationInfo getGenreOpusPagingList(int page, String genre, String order) {
		PaginationInfo pginfo = null;
		/*
		* 사용테이블 opus
		* 메소드가 받은 파라미터값을 사용해 opus 테이블의 정보를 조건에 맞게 조회하는 기능 구현
		* 값은 페이징이 되어야하고 genre와 order값에 따라 검색 조건이 변경되야한다.
		* int page = 현재 페이지
		* String genre = 조회하려는 작품의 장르값( opus.genre )
		* String order = 1: 등록일자(오름차순), 2:등록일자(내림차순), 3:작품명(오름차순) 값에 따라 조회값 정렬방식 변경
		* ex)page=1, genre=RM, order=1 일때 1페이지에 표시될 장르가 "로맨스"고 "등록일자 오름차순"정렬된 작품 리스트
		*
		* PaginationInfo 객체 반환필요
		* [
		*	pginfo.setRecordCountPerPage - 한페이지 당 컨텐츠수
		*	pginfo.setTotalRecordCount - 전체 컨텐츠 수
		*	pginfo.setPageSize - 노출되는 페이지 크기
		*	pginfo.setCurrentPageNo - 현재페이지
		*   pginfo.setList - 페이징된 데이터리스트
		* ]
		* 페이징 고정 상수값
		* PageSetting.PER_PAGE_NUM.get() - 한페이지 당 컨텐츠 수
		* PageSetting.PAGE_SIZE.get() - 노출되는 페이지 크기
		*/

		return pginfo;
	}

	public PaginationInfo getSearchOpusPagingList(int page, String search, String tag, String genre){
		PaginationInfo pginfo = null;
		/*
		 * 사용테이블 opus
		 * 메소드가 받은 파라미터값을 사용해 opus 테이블의 정보를 조건에 맞게 조회하는 기능 구현
		 * 값은 페이징이 되어야하고 search, tag, genre 에 따라 검색 조건이 변경되야한다.
		 * int page = 현재 페이지
		 * String search = 검색하려는 작품 제목
		 * String tag = 검색하려는 해쉬태그명
		 * String genre = 조회하려는 작품의 장르값( opus.genre )
		 * ex)page=1, search=검색, tag=태그, genre=TR 일때 1페이지에 표시될 장르가 "스릴러"고 제목에 "검색"이 포함되고
		 * 해쉬태그에 "태그"가 포함되는 작품 리스트
		 *
		 * PaginationInfo 객체 반환필요
		 * [
		 *	pginfo.setRecordCountPerPage - 한페이지 당 컨텐츠수
		 *	pginfo.setTotalRecordCount - 전체 컨텐츠 수
		 *	pginfo.setPageSize - 노출되는 페이지 크기
		 *	pginfo.setCurrentPageNo - 현재페이지
		 *   pginfo.setList - 페이징된 데이터리스트
		 * ]
		 * 페이징 고정 상수값
		 * PageSetting.PER_PAGE_NUM.get() - 한페이지 당 컨텐츠 수
		 * PageSetting.PAGE_SIZE.get() - 노출되는 페이지 크기
		 */
		return pginfo;
	}
	public Map<String, Object> getSearchOpusKindCount(String search, String tag, String genre){
		Map<String,Object> resultMap = new HashMap<>();

		resultMap.put("AB", opusDao.countMainSearchOpus(search, tag, genre));

		return resultMap;
	}

	public Map<String, Object> getSearchOpusGenreCount(String search, String tag){
		Map<String,Object> resultMap = new HashMap<>();

		/*
		 * 사용테이블 opus
		 * 메소드가 받은 파라미터값을 사용해 opus 테이블의 정보를 조건에 맞게 조회하는 기능 구현
		 * 값은 페이징이 되어야하고 search, tag 에 따라 검색 조건이 변경되야한다.
		 * String search = 검색하려는 작품 제목
		 * String tag = 검색하려는 해쉬태그명
		 * ex)search=검색, tag=태그 일때 작품명에 "검색"이 포함되고 해쉬태그에 "태그"가 포함되는 작품 수를 장르별 그룹화 하여 조회
		 *
		 * Map 객체 반환필요
		 * [ key(장르) ] | [ value(작품 수) ]
		 *   	TR				 10
		 * 		HR				 0
		 * 		ML				 3
		 * 		AC				 7
		 * 		RC				 9
		 *
		 */

		return resultMap;
	}

	public List<OpusVo> getOpusList(List<Integer> noList){
		return opusDao.selectOpusByNoList(noList);
	}

	public PaginationInfo getOpusInMonthList(int page, String genre, int month){
		PaginationInfo pginfo = null;

		/*
		* 사용테이블 opus
		* 메소드가 받은 파라미터값을 사용해 opus 테이블내 정보를 조건에 맞게 조회하는 기능 구현(페이징)
		* 값은 페이징이 되어야하고 genre와 month값에 따라 검색 조건이 변경되야한다.
		* int page = 현재 페이지
		* String genre = 조회하려는 작품의 장르값( opus.genre )
		* String month = 조회하려는 작품의 등록일( opus.reg_date) 날짜(월)
		* ex) page=1, genre=TR, month=2 일때 1페이지에 표시될 "스릴러"작품이고 등록일이 "2월"인 작품 리스트 조회
		*
		 * PaginationInfo 객체 반환필요
		 * [
		 *	pginfo.setRecordCountPerPage - 한페이지 당 컨텐츠수
		 *	pginfo.setTotalRecordCount - 전체 컨텐츠 수
		 *	pginfo.setPageSize - 노출되는 페이지 크기
		 *	pginfo.setCurrentPageNo - 현재페이지
		 *  pginfo.setList - 페이징된 데이터리스트
		 * ]
		 * 페이징 고정 상수값
		 * PageSetting.PER_PAGE_NUM.get() - 한페이지 당 컨텐츠 수
		 * PageSetting.PAGE_SIZE.get() - 노출되는 페이지 크기
		*
		*/
		return pginfo;
	}

	public void addOpusCount(int opusNo){
		opusDao.addOpusCountByNo(opusNo);
	}

}
