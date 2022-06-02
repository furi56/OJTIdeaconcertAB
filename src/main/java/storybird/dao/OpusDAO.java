package storybird.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import storybird.vo.OpusVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OpusDAO {
	public List<OpusVo> selectAllOpus(int start, int num, String search,
									  String category, String genre, String service_state,
									  String comp, String day);
	public List<OpusVo> selectMainSearchOpus(int start, int num, String search,
											 String tag, String genre);
	public List<OpusVo> selectOpusStatis(int start, int num, String start_date, String end_date,String kind);
	public List<Map<String,Object>> selectMainSearchGenreCount(String search, String tag);
	public List<OpusVo> selectNewOpusInMonthList(String genre, String kind);
	public List<OpusVo> tempSelectNewOpusInMonthList(String genre, String kind, int start, int num);
	public int countTempSelectNewOpusInMonthList(String genre, String kind);
	public OpusVo selectOpusByNo(int opus_no);
	public List<OpusVo> selectLangCodeOpusListByNo(int no);
	public OpusVo selectOpusByLangCode(int cont_no, String lang_code);
	public List<OpusVo> weekAllOpus(int week, String order, String kind);
	public List<OpusVo> selectGenreOpus(String genre, String order, String kind, int start, int num);
	public List<OpusVo> compAllOpus(String kind);
	public List<OpusVo> searchOpus(int start, int num, String search,
								   String service_state, boolean epi_filter);
	public List<OpusVo> searchOpusCompany(int start, int num, String search , String company_code);
	public List<OpusVo> genreOpus(String genre);
	public List<OpusVo> genreAllOpus(String genre);
	public List<OpusVo> genreNewOpus(String genre, Date new_date);
	public List<OpusVo> realTimeRankingOpus(String genre, int total, int start, int num, String kind, String standard_date);
	public List<OpusVo> dateRankingOpus(String genre, String dateType, int dayNum, int total, int start, int num, String kind);
	public List<OpusVo> genreAllbydayOpus(String genre);
	public List<OpusVo> timeOpus(String kind, String standard_date);
	public int countPartnerOpusCompStatusByAccountNo(int no, String compYn);
	public int countAuthorOpusCompStatusByAccountNo(int no, String compYn);
	public List<OpusVo> selectAuthorOpusByAccountNo(int no, int start, int num);
	public List<OpusVo> selectOpusByNoList(List<Integer> list);
	public int countOpusStatis(String kind);

	public int countOpus();
	public int countOpusByLangCode(int cont_no, String lang_code);
	public int countMainSearchOpus( String search, String tag, String genre);
	public int countDetailSerachOpus(String search, String category, String genre,
									 String service_state, String comp, String day);
	public int countSearchOpus(String search,String service_state, boolean epi_filter);
	public int countSearchOpusCompany(String search,String company_code);
	public int countNotPermissionOpus();
	public int countCompOpus(String kind);
	public int countGenreOpus(String genre, String kind);

	public int selectMaxOpusNo();
	public int insertOpus(OpusVo vo);
	public int updateOpusByNo(OpusVo vo);
	public int updateOpusByNoAndLang(OpusVo vo);
	public int updateOpusOpenByNo(int opus_no);
	public int updateOpusServiceByNo(int opus_no, String service_state);
	public int updateOpusCompByNo(int opus_no);
	public int updateOpusPerByNo(int opus_no);
	public int updateDelStateByNo(int opus_no);
	public int addOpusCountByNo(int opus_no);
	public int deleteOpusByNo(int opus_no);
	public List<OpusVo> weekOpus(int start, int num, int week, String order);
	public List<OpusVo> compOpus(int start, int num, String kind);

}