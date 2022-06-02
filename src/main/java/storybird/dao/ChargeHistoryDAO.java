package storybird.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import storybird.vo.ChargeHistoryVo;
import storybird.vo.join.CancelChargeHistoryVo;

@Mapper
public interface ChargeHistoryDAO {
	public ChargeHistoryVo selectChargeHistoryBySeq(int charge_seq);
	public int selectAllChargeHistorySum(String search, String pay_type, String start_date, String end_date);
	public int selectAllChargeCashHistorySum(String search, String pay_type, String start_date, String end_date);
	public int selectMonthChargeCancelCountByMemNo(int mem_no);
	public ChargeHistoryVo selectMemberShipHistoryByMemNo(int mem_no);
	public CancelChargeHistoryVo selectCancelMemberShipByMemNo(int mem_no);
	public List<ChargeHistoryVo> selectPagingMemberShipHistoryByMemNo(int mem_no, int start, int num);
	public int countPagingMemberShipHistoryByMemNo(int mem_no);
	public List<ChargeHistoryVo> selectAllChargeHistoryByMemNo(@Param("mem_no")int mem_no, @Param("charge_seq")int charge_seq, @Param("start")int start, @Param("num")int num);
	public List<ChargeHistoryVo> selectAllChargeHistory(@Param("mem_no")int mem_no);
	public int selectChargeCashSumByMemNo(int mem_no);
	public int countChargeHistoryByMemNo(int mem_no);
	public int countUseCouponByCNo(int c_no);
	public int countSearchChargeHistory(@Param("search")String search,
			@Param("pay_type")String pay_type, @Param("start_date")String start_date, @Param("end_date")String end_date);
	public int insertChargeHistory(ChargeHistoryVo vo);

	public int updateChargeHistoryAmountByChargeSeq(BigDecimal settle_amt, int charge_seq);
	public int updateChargeBuyStatusByChargeSeq(int charge_seq);
}
