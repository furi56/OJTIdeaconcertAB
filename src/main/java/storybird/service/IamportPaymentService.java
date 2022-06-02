package storybird.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storybird.dao.IamportPaymentDAO;
import storybird.vo.IamportPaymentVo;

@Service
public class IamportPaymentService {
    @Autowired
    IamportPaymentDAO paymentDao;

    public IamportPaymentVo getOnePayment(String merUid){
        return paymentDao.selectOnePaymentByMerUID(merUid);
    }

    public IamportPaymentVo getOnePayment(int chargeSeq){
        return paymentDao.selectOnePaymentByChargeSeq(chargeSeq);
    }

    public String getMerchantUid(int chargeSeq){
        return paymentDao.selectMerchantUidByChargeSeq(chargeSeq);
    }

    public String getFirstChargeDate(String customerUid){
        return paymentDao.selectFirstChargeDateByCustomerUid(customerUid);
    }

    public void savePayment(IamportPaymentVo vo){
        paymentDao.insertPayment(vo);
    }

    public void modifyPayment(IamportPaymentVo vo){
        paymentDao.updatePaymentByMerchantUid(vo);
    }
}
