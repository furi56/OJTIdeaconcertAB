package storybird.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import storybird.vo.OpusHistoryVo;
import storybird.vo.join.MyPageOpusInfoVo;

import java.util.List;
import java.util.Map;

@Mapper
public interface BuyHistoryDAO {
	public int selectAllUseCashSum(String search,String start_date, String end_date,String kind);
	public List<OpusHistoryVo> selectUseCashHistory(String search, String kind, String start_date, String end_date, int start, int num);
	public List<OpusHistoryVo> selectAllCashHistoryByMemNo(@Param("mem_no")int mem_no, @Param("start")int start, @Param("num")int num);
	public List<OpusHistoryVo> selectAllCashHistoryGroupByMemNo(@Param("mem_no")int mem_no, @Param("start")int start, @Param("num")int num);
	public List<OpusHistoryVo> selectAllCashHistory(@Param("start")int start, @Param("num")int num, @Param("search")String search, @Param("start_date")String start_date, @Param("end_date")String end_date, @Param("kind")String kind);
	public List<OpusHistoryVo> selectAlloutCashHistory(@Param("start")int start, @Param("num")int num, @Param("search")String search, @Param("start_date")String start_date, @Param("end_date")String end_date, @Param("kind")String kind);

	public int selectCashSumByMemNo(int mem_no);
	public int selectBonusCashSumByMemNo(int mem_no);
	public boolean selectOpusBuyNeed(@Param("mem_no")int mem_no,@Param("opus_no")int opus_no, @Param("epi_seq")int epi_seq);
	public int selectOpusBuyStatus(int mem_no, int opus_no, int epi_seq);
	public boolean selectGiftOpusBuyNeed(@Param("mem_no")int mem_no,@Param("opus_no")int opus_no, @Param("epi_seq")int epi_seq,@Param("gift_no")int gift_no);
	public int selectCashbybuy(@Param("mem_no")int mem_no,@Param("opus_no")int opus_no, @Param("epi_seq")int epi_seq);
	public int selectGiftCashbybuy(@Param("mem_no")int mem_no,@Param("opus_no")int opus_no, @Param("epi_seq")int epi_seq,@Param("gift_no")int gift_no);
	public List<Map<String,Object>> selectAllSaleInfoByMonth(int month);
	public List<MyPageOpusInfoVo> selectBuyOpusListByMemNo(int no, String rant_yn, int start, int num);
	public int countBuyOpusListByMemNo(int mem_no, String rant_yn);
	public int countCashHistoryByMemNo(int mem_no);
	public int countCashHistoryGroupByMemNo(int mem_no);
	public int countOpusBuyHistoryByMemNo(int mem_no, int opus_no);
	public int countUseCashHistory(String search, String kind, String start_date, String end_date);
	public int countCashHistory(@Param("search")String search,@Param("start_date")String start_date,@Param("end_date")String end_date,@Param("kind")String kind);
	public int countoutCashHistory(@Param("search")String search,@Param("start_date")String start_date,@Param("end_date")String end_date,@Param("kind")String kind);
	public int countAllOpusSaleTable(@Param("search")String search, @Param("kind")String kind);
	public int countAllPartnerSaleTable(@Param("search")String search, @Param("kind")String kind,
										@Param("start_date")String start_date, @Param("end_date")String end_date);
	public int countPartnerOutSale();
	public int countPartnerOpusByNo(int no);
	public int countSaleTableByOpusNo(int opus_no);
	public int countMemberShipHistory(int mem_no, int opus_no, int epi_seq, int charge_seq);
	public int countNowBuyHistory(int mem_no, int opus_no, int epi_seq);
	public int insertGiftCashHistory(OpusHistoryVo vo);
	public int insertCashHistory(OpusHistoryVo vo);
	public int insertMemberShipCashHistory(OpusHistoryVo vo);
	public int insertoutHistory(OpusHistoryVo vo);
	public int updateShowByIdx(int h_idx, String show_yn);
	public int updateCancelState(int h_idx, String cancel_yn);
}
