package storybird.dao;

import org.apache.ibatis.annotations.Mapper;
import storybird.vo.IamportPaymentVo;

@Mapper
public interface IamportPaymentDAO {
    public IamportPaymentVo selectOnePaymentByMerUID(String merchant_uid);
    public String selectMerchantUidByChargeSeq(int charge_seq);
    public IamportPaymentVo selectOnePaymentByChargeSeq(int charge_seq);
    public String selectFirstChargeDateByCustomerUid(String customer_uid);

    public int insertPayment(IamportPaymentVo vo);

    public int updatePaymentByMerchantUid(IamportPaymentVo vo);
}
