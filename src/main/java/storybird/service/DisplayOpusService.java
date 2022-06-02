package storybird.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storybird.dao.DisplayOpusDAO;
import storybird.enums.ErrorCode;
import storybird.enums.PageSetting;
import storybird.exception.ToonflixErrorException;
import storybird.util.PaginationInfo;
import storybird.vo.DisplayOpusVo;
import storybird.vo.OpusVo;

import java.util.List;

@Service
public class DisplayOpusService extends AbstractService{

    @Autowired
    DisplayOpusDAO displayDao;

    public PaginationInfo getSearchDisplayOpusPagingList(int page, String search, String dspType, String genre,
                                                         String opusKind, String dayWeek, String startDate, String langCode, int recordCnt){
        int total = displayDao.countDisplayOpusByType(search, dspType, genre, dayWeek, opusKind, startDate, langCode);
        PaginationInfo pginfo = getPaginationInfo(page, recordCnt, total, PageSetting.PAGE_SIZE.get());

        pginfo.setList(displayDao.selectDisplayOpusByType(pginfo.getFirstRecordIndex(), recordCnt,search,
                dspType, genre, dayWeek, opusKind, startDate, langCode));

        return pginfo;
    }

    public PaginationInfo getDisplayOpusPagingList(int page, String dspType, String genre,
                                                         String opusKind, String dayWeek, String order){
        int total = displayDao.countDisplayOpusToOpusVo(dspType, genre, dayWeek, opusKind);
        PaginationInfo pginfo = getPaginationInfo(page, PageSetting.MAIN_OPUS_PAGE_NUM.get(), total, PageSetting.PAGE_SIZE.get());

        pginfo.setList(displayDao.selectDisplayOpusToOpusVo(pginfo.getFirstRecordIndex(), PageSetting.ADIM_PER_PAGE_NUM.get(),
                dspType, genre, dayWeek, opusKind, order));

        return pginfo;
    }

    public List<OpusVo> getAllDisplayOpusList(String dspType, String genre,
                                                   String opusKind, String dayWeek, String order, int start, int num){
        int total = displayDao.countDisplayOpusToOpusVo(dspType, genre, dayWeek, opusKind);

        return displayDao.selectDisplayOpusToOpusVo(start, num,
                dspType, genre, dayWeek, opusKind, order);
    }

    public List<OpusVo> getAllDisplayOpusList(String dspType, String genre,
                                              String opusKind, String dayWeek, String order){
        int total = displayDao.countDisplayOpusToOpusVo(dspType, genre, dayWeek, opusKind);

        return displayDao.selectDisplayOpusToOpusVo(0, 5, dspType, genre, dayWeek, opusKind, order);
    }

    public void addDisplayOpus(DisplayOpusVo vo){
        if(displayDao.findDuplicateDisplay(vo))
            throw new ToonflixErrorException("중복 진열", ErrorCode.DISPLAY_DUPLICATION);
        displayDao.insertDisplayOpus(vo);
    }

    public boolean duplicateDisplayOpus(int opusNo){
        return displayDao.findDuplicateDisplayByOpusNo(opusNo);
    }

    public void setDisplayOrderRise(int no, String kind, String genre){
        DisplayOpusVo vo = displayDao.selectDisplayOpusByNo(no);
        if(displayDao.selectFirstOrderByNo(no, vo.getDsp_type(), kind, genre)) throw new ToonflixErrorException("순서 변경 불가", ErrorCode.CANNOT_BE_CHANGED);
        DisplayOpusVo riseVo = displayDao.selectRiseOrderByNo(vo.getDsp_order(), kind, vo.getDsp_type(), genre);
        vo.setDsp_no(no);
        if(riseVo != null){
            displayDao.updateOrderByNo(vo.getDsp_no(), riseVo.getDsp_order());
            displayDao.updateOrderByNo(riseVo.getDsp_no(), vo.getDsp_order());
        }

    }

    public void setDisplayOrderDrop(int no, String kind, String genre){
        DisplayOpusVo vo = displayDao.selectDisplayOpusByNo(no);
        if(displayDao.selectLastOrderByNo(no, vo.getDsp_type(), kind, genre)) throw new ToonflixErrorException("순서 변경 불가", ErrorCode.CANNOT_BE_CHANGED);
        DisplayOpusVo dropVo = displayDao.selectDropOrderByNo(vo.getDsp_order(), kind, vo.getDsp_type(),genre);
        if(dropVo != null){
            displayDao.updateOrderByNo(vo.getDsp_no(), dropVo.getDsp_order());
            displayDao.updateOrderByNo(dropVo.getDsp_no(), vo.getDsp_order());
        }
    }

    public void removeDisplayOpus(int no, String dspType){
        displayDao.updateDisplayOpusDeleteStateByNo(no);
        DisplayOpusVo vo = displayDao.selectDisplayOpusByNo(no);
        displayDao.sortDisplayOpusOrder(dspType, vo.getLang_code(), vo.getOpus_kind());
    }

    public void removeDisplayOpus(Integer[] dspNoList, String dspType){

        if(dspNoList != null) {
            for(int no : dspNoList){
                DisplayOpusVo vo = displayDao.selectDisplayOpusByNo(no);
                displayDao.deleteDisplayOpusByNo(no);
                displayDao.sortDisplayOpusOrder(dspType, vo.getLang_code(), vo.getOpus_kind());
            }
        }
    }
}
