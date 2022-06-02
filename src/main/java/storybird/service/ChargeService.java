package storybird.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import storybird.dao.ChargeDAO;
import storybird.security.JwtTokenProvider;
import storybird.vo.ChargeVo;

@Service
public class ChargeService extends AbstractService{

	@Autowired ChargeDAO chargeDAO;
	@Autowired JwtTokenProvider jwtTokenProvider;
	@Autowired ChargeHistoryService chargeHistoryService;
	
	public List<ChargeVo> getselectAllChargeItem() {
		return chargeDAO.selectAllChargeItem();
	}
	public List<ChargeVo> getselectChargeItem(String nationCode) {
		return chargeDAO.selectChargeItemByNationCode(nationCode);
	} 
	public ChargeVo getselectOneChargeItem(String item_code) {
		return chargeDAO.selectOneChargeItem(item_code);
	}
	
}