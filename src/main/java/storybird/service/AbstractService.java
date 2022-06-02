package storybird.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import storybird.enums.FilePath;
import storybird.util.PaginationInfo;

public class AbstractService {

	//작품 카테고리 별 경로 반환
	public String getOpusPath(String kind) {
		switch(kind){
		case "AB":
			return FilePath.AUDIO_PATH.getPath();
		case "WM":
			return FilePath.MOVIE_PATH.getPath();
		case "WB":
			return FilePath.WEBBOOK_PATH.getPath();
		}
		return FilePath.AUDIO_PATH.getPath();
	}

	//PaginationInfo 객체 생성
	public PaginationInfo getPaginationInfo(int page, int recordCnt, int totalRecordCnt, int pageSize) {
		PaginationInfo pginfo = new PaginationInfo();
		
		pginfo.setRecordCountPerPage(recordCnt);
		pginfo.setTotalRecordCount(totalRecordCnt);
		pginfo.setPageSize(pageSize);
		pginfo.setCurrentPageNo(page);
		
		return pginfo;
	}
	
	public String getRandomStr(int size) {
		// 48 ~ 122 / 0 ~ z
		Random random = new Random();
		String randomStr = random.ints(48, 123)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(size)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		new StringBuilder().appendCodePoint(1);
		return randomStr;
	}
	public String getRandomInt(int size) {
		Random random = new Random();
		String randomStr = random.ints(0,9)
				.limit(size)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();
		return randomStr;
	}
	public boolean isDateExpired(String toDateStr, String endDateStr) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date endDate = format.parse(endDateStr);
		Date toDate = format.parse(toDateStr);

		return isDateExpired(toDate, endDate);
	}

	public boolean isDateExpired(Date toDate, Date endDate){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		int compare = endDate.compareTo(toDate);

		if(compare >= 0 ) return false;
		else return true;
	}

}
