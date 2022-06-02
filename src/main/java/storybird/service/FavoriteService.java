package storybird.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storybird.dao.FavoriteDAO;
import storybird.enums.PageSetting;
import storybird.util.PaginationInfo;

@Service
public class FavoriteService extends AbstractService {
    @Autowired
    FavoriteDAO favoriteDAO;

    public PaginationInfo getFavoriteHistoryPagingList(int page, int memNo){
        int total = favoriteDAO.countFavSelect(memNo);
        PaginationInfo pginfo = getPaginationInfo(page, PageSetting.PER_PAGE_NUM.get(), total, PageSetting.PAGE_SIZE.get());
        pginfo.setList(favoriteDAO.selectAllFavByMemNo(memNo, pginfo.getFirstRecordIndex(), PageSetting.PER_PAGE_NUM.get()));
        return pginfo;
    }

    public PaginationInfo getMemberFavOpusPagingList(int page, int memNo){
        int total = favoriteDAO.countFavOpusListByMemNo(memNo);
        PaginationInfo pginfo = getPaginationInfo(page, PageSetting.STORAGE_PAGE_NUM.get(), total, PageSetting.PAGE_SIZE.get());

        pginfo.setList(favoriteDAO.selectFavOpusListByMemNo(memNo, pginfo.getFirstRecordIndex(), pginfo.getRecordCountPerPage()));
        return pginfo;
    }

    public boolean getFavorite(int opus_no,int mem_no){
        return favoriteDAO.FavSelect(opus_no,mem_no);
    }
    public void registerFav(int opus_no,int mem_no){
        if(!favoriteDAO.FavSelect(opus_no,mem_no)){
            favoriteDAO.insertOpusFavorite(opus_no,mem_no);
        }
    }
    public void deleteFav(int opus_no,int mem_no){
        favoriteDAO.deleteOpusFavorite(opus_no,mem_no);
    }
    public void removeFavHistory(int seq){
        favoriteDAO.deleteOpusFavoriteBySeq(seq);
    }

}
