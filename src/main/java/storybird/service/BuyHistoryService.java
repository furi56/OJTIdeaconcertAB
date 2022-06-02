package storybird.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import storybird.dao.BuyHistoryDAO;
import storybird.enums.PageSetting;
import storybird.util.PaginationInfo;
import storybird.vo.OpusHistoryVo;

import java.util.List;
import java.util.Map;

@Service
public class BuyHistoryService extends AbstractService {
	@Autowired
	BuyHistoryDAO buyDao;

	public int getUseCashSum(String search,String start_date, String end_date,String kind){
		return buyDao.selectAllUseCashSum(search, start_date, end_date, kind);
	}

	public int getOpusBuyHistoryCount(int memNo, int opusNo){
		return buyDao.countOpusBuyHistoryByMemNo(memNo, opusNo);
	}

	public PaginationInfo getUseCashHistoryPagingList(int page, String search, String kind, String startDate, String endDate, int recordCnt){
		int total = buyDao.countUseCashHistory(search, kind, startDate, endDate);

		PaginationInfo pginfo = getPaginationInfo(page, recordCnt, total, PageSetting.PAGE_SIZE.get());
		pginfo.setList(buyDao.selectUseCashHistory(search, kind, startDate, endDate, pginfo.getFirstRecordIndex(), pginfo.getRecordCountPerPage()));
		return pginfo;
	}

	public PaginationInfo getChargeHistoryPagingList(int page, int memNo){
		int total = buyDao.countCashHistoryByMemNo(memNo);
		PaginationInfo pginfo = getPaginationInfo(page, PageSetting.PER_PAGE_NUM.get(), total, PageSetting.PAGE_SIZE.get());
		pginfo.setList(buyDao.selectAllCashHistoryByMemNo(memNo, pginfo.getFirstRecordIndex(), pginfo.getRecordCountPerPage()));
		return pginfo;
	}
	public PaginationInfo getChargeHistoryGroupPagingList(int page, int memNo){
		int total = buyDao.countCashHistoryGroupByMemNo(memNo);
		PaginationInfo pginfo = getPaginationInfo(page, PageSetting.PER_PAGE_NUM.get(), total, PageSetting.PAGE_SIZE.get());
		pginfo.setList(buyDao.selectAllCashHistoryGroupByMemNo(memNo, pginfo.getFirstRecordIndex(), pginfo.getRecordCountPerPage()));
		return pginfo;
	}

	public PaginationInfo getChargeHistoryPagingListAll(int page,String search,String start_date,String end_date,String kind, int recordCnt){
		int total = buyDao.countCashHistory(search, start_date, end_date, kind);
		PaginationInfo pginfo = getPaginationInfo(page, recordCnt, total, PageSetting.PAGE_SIZE.get());
		pginfo.setList(buyDao.selectAllCashHistory(pginfo.getFirstRecordIndex(), pginfo.getRecordCountPerPage(), search, start_date, end_date, kind));
		return pginfo;
	}
	public PaginationInfo getoutCashHistoryPagingList(int page,String search,String start_date,String end_date,String kind, int recordCnt){
		int total = buyDao.countoutCashHistory(search, start_date, end_date, kind);
		PaginationInfo pginfo = getPaginationInfo(page, recordCnt, total, PageSetting.PAGE_SIZE.get());
		pginfo.setList(buyDao.selectAlloutCashHistory(pginfo.getFirstRecordIndex(), pginfo.getRecordCountPerPage(), search, start_date, end_date, kind));
		return pginfo;
	}

	public PaginationInfo getMemberBuyOpusPagingList(int page, int memNo, String rantYn){
		int total = buyDao.countBuyOpusListByMemNo(memNo, rantYn);
		PaginationInfo pginfo = getPaginationInfo(page, PageSetting.STORAGE_PAGE_NUM.get(), total, PageSetting.PAGE_SIZE.get());

		pginfo.setList(buyDao.selectBuyOpusListByMemNo(memNo, rantYn, pginfo.getFirstRecordIndex(), pginfo.getRecordCountPerPage()));
		return pginfo;
	}

	public void changeShowValue(int buyIdx, String showYn){
		buyDao.updateShowByIdx(buyIdx, showYn);
	}

	public List<Map<String,Object>> getAllSaleInfo(int month){
		return buyDao.selectAllSaleInfoByMonth(month);
	}
/*
	public int getBuysearch(int memNo, int opusNo, int epiSeq) {
		return buyDao.selectCashbybuy(memNo, opusNo, epiSeq);
	}
	*/
	public int getGiftBuysearch(int memNo, int opusNo, int epiSeq,int gift_no) {
		return buyDao.selectGiftCashbybuy(memNo, opusNo, epiSeq,gift_no);
	}

	public boolean getBuyNeedState(int memNo, int opusNo, int epiSeq){
		return buyDao.selectOpusBuyNeed(memNo, opusNo, epiSeq);
	}

	public void changeCancelState(int buyIdx, String cancelYn){
		buyDao.updateCancelState(buyIdx, cancelYn);
	}
	public int getBuyState(int memNo, int opusNo, int epiSeq){
		return buyDao.selectOpusBuyStatus(memNo, opusNo, epiSeq);
	}
	
	public boolean getGiftBuyNeedState(int memNo, int opusNo, int epiSeq,int gift_no){
		return buyDao.selectGiftOpusBuyNeed(memNo, opusNo, epiSeq,gift_no);
	}
	
	public int getRemainCash(int memNo) {
		return buyDao.selectCashSumByMemNo(memNo);
	}

	public int getBonusRemainCash(int memNo) {
		return buyDao.selectBonusCashSumByMemNo(memNo);
	}
	
	public void saveCashHistory(OpusHistoryVo vo) {
		buyDao.insertCashHistory(vo);
	}
	public void saveMemberShipHistory(OpusHistoryVo vo){
		buyDao.insertMemberShipCashHistory(vo);
	}

	public int getCountNowBuyHistory(int memNo, int opusNo, int epiSeq){
		return buyDao.countNowBuyHistory(memNo, opusNo, epiSeq);
	}

	public void saveGiftCashHistory(OpusHistoryVo vo) {
		buyDao.insertGiftCashHistory(vo);
	}
	public void saveoutCashHistory(OpusHistoryVo vo) {
		buyDao.insertoutHistory(vo);
	}

}
