package storybird.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import storybird.dao.ChargeHistoryDAO;
import storybird.enums.ErrorCode;
import storybird.enums.PageSetting;
import storybird.exception.ToonflixErrorException;
import storybird.util.PaginationInfo;
import storybird.vo.ChargeHistoryVo;
import storybird.vo.join.CancelChargeHistoryVo;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ChargeHistoryService extends AbstractService{
	
	@Autowired
	ChargeHistoryDAO chargeDao;

	public ChargeHistoryVo getChargeHistory(int chargeSeq){
		return chargeDao.selectChargeHistoryBySeq(chargeSeq);
	}

	public PaginationInfo getChargeHistoryPagingList(int page, int memNo){
		int total = chargeDao.countChargeHistoryByMemNo(memNo);
		PaginationInfo pginfo = getPaginationInfo(page, PageSetting.PER_PAGE_NUM.get(), total, PageSetting.PAGE_SIZE.get());

		pginfo.setList(chargeDao.selectAllChargeHistoryByMemNo(memNo, 0,pginfo.getFirstRecordIndex(), PageSetting.PER_PAGE_NUM.get()));
		return pginfo;
	}

	public int getMonthCancelHistoryCount(int memNo){
		return chargeDao.selectMonthChargeCancelCountByMemNo(memNo);
	}

	public ChargeHistoryVo getChargeHistoryInfo(int memNo, int chargeSeq){
		return chargeDao.selectAllChargeHistoryByMemNo(memNo, chargeSeq,0, 1).get(0);
	}

	public List<ChargeHistoryVo> getChargeHistoryAll(int mem_no){
		return chargeDao.selectAllChargeHistory(mem_no);
	}
	
	public int getChargeCash(int memNo) {
		return chargeDao.selectChargeCashSumByMemNo(memNo);
	}

	public ChargeHistoryVo getMemberShipHistory(int memNo){
		return chargeDao.selectMemberShipHistoryByMemNo(memNo);
	}

	public CancelChargeHistoryVo getCancelMemberShipInfo(int memNo){
		return chargeDao.selectCancelMemberShipByMemNo(memNo);
	}

	public PaginationInfo getMemberShipHistoryList(int page, int memNo){
		int total = chargeDao.countPagingMemberShipHistoryByMemNo(memNo);
		PaginationInfo info = getPaginationInfo(page, PageSetting.MEMBERSHIP_PAGE_NUM.get(),total, PageSetting.PAGE_SIZE.get() );

		info.setList(chargeDao.selectPagingMemberShipHistoryByMemNo(memNo,
				info.getFirstRecordIndex(), info.getRecordCountPerPage()));
		return info;
	}

	public int getAllChargeAmount(String search, String payType, String start_date, String end_date){
		return chargeDao.selectAllChargeHistorySum(search, payType, start_date, end_date);
	}

	public int getAllChargeCash(String search, String payType, String start_date, String end_date) {
		return chargeDao.selectAllChargeCashHistorySum(search, payType, start_date, end_date);
	}
	
	public void saveChargeHistory(ChargeHistoryVo vo) {
		chargeDao.insertChargeHistory(vo);
	}

	public void saveCouponChargeHistory(int cNo, int memNo){
		ChargeHistoryVo memberShipVo = chargeDao.selectMemberShipHistoryByMemNo(memNo);
		if(chargeDao.countUseCouponByCNo(cNo) > 0){
			throw new ToonflixErrorException("이미 사용된 쿠폰", ErrorCode.DUPLICATE_VALUE);
		}
		// 사용중인 멤버쉽이 없을시 사용 등록 가능
		if(memberShipVo == null){
			ChargeHistoryVo cVo = new ChargeHistoryVo();
			cVo.setMem_no(memNo);
			cVo.setC_no(cNo);
			cVo.setItem_name("쿠폰(멤버쉽)");
			cVo.setItem_type("F");
			cVo.setSettle_amt(0);
			cVo.setSettle_method("COUPON");
			cVo.setBuy_state("B");
			cVo.setNation_code("KR");
			cVo.setItem_month(1);

			chargeDao.insertChargeHistory(cVo);
		}else{
			throw new ToonflixErrorException("멤버쉽 사용중 등록불가", ErrorCode.CANNOT_BE_REGISTRATION);
		}
	}

	public void modifyChargeHistoryAmount(BigDecimal amount, int chargeSeq){
		chargeDao.updateChargeHistoryAmountByChargeSeq(amount, chargeSeq);
	}

	public void changeChargeBuyStatus(int chargeSeq){
		chargeDao.updateChargeBuyStatusByChargeSeq(chargeSeq);
	}
}
