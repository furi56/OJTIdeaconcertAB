package storybird.controller;

import java.util.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import storybird.annotation.SearchValue;
import storybird.enums.PageSetting;
import storybird.service.*;
import storybird.util.PaginationInfo;
import storybird.vo.DataResponse;
import storybird.vo.PagingResponse;


/**
 * 플랫폼 메인 컨트롤러
 * 작품 검색, 회사 정보 조회
 * 베너, 이벤트, 팝업 정보 조회
 */

@RestController(value = "메인 컨트롤러")
public class MainController {

    @Autowired OpusService opusService;

    @ApiOperation(value = "작품 검색")
    @GetMapping("/opus/search/{optPage}")
    public ResponseEntity<?> searchOpus(@PathVariable Optional<Integer> optPage, @SearchValue String search, String tag,
                                        @RequestParam(defaultValue = "ALL") String genre) {
        int page = optPage.isPresent() ? optPage.get() : 1;
        /*
        * 검색 작품 리스트 조회
        * opusService.getSearchOpusPagingList 수정 필요
        */
        PaginationInfo info = opusService.getSearchOpusPagingList(page, search, tag, genre);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pagingList", info.getList());

        /*
        * 장르별 동일 조건 검색 작품 수 조회
        * opusService.getSearchOpusGenreCount 수정 필요
        */
        paramMap.put("genreTotal", opusService.getSearchOpusGenreCount(search, tag));
        PagingResponse response = info.getPagingResponse();
        response.setData(paramMap);
        return ResponseEntity.ok(response);
    }
}
