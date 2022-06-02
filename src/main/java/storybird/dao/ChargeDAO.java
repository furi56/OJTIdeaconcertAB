package storybird.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import storybird.vo.ChargeVo;

@Mapper
public interface ChargeDAO {
	public List<ChargeVo> selectAllChargeItem();
	public List<ChargeVo> selectChargeItemByNationCode(String nation_code);
	public ChargeVo selectOneChargeItem(@Param("item_code")String item_code);
}
