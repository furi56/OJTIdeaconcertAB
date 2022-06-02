package storybird.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import storybird.enums.PageSetting;
import storybird.security.ToonflixUsers;
import storybird.service.*;
import storybird.util.PaginationInfo;
import storybird.vo.*;
import storybird.vo.join.MyPageOpusInfoVo;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 플랫폼 마이페이지 기능 컨트롤러
 * 최근 본 작품, 찜한 작품, 요금제 조회
 */

@Controller
@RequestMapping("/mypage")
public class MyPageController {

	@Autowired WatchHistoryService watchHistoryService;

	
	@ApiOperation(value="마이페이지 최근 본 작품")
	@GetMapping("/watch/list/{optPage}")
	@ApiImplicitParam(name="order", value="정렬 방식(최근 재생순:R, 최다 재생순:M)",dataType="string", required = false)
	@ApiResponses({
			@ApiResponse(code=200, message="성공", response= WatchHistoryVo.class, responseContainer = "List")
	})
	public ResponseEntity<?> storageView(@PathVariable Optional<Integer> optPage, @RequestParam(defaultValue = "R") String order) throws Exception{
		int page = optPage.isPresent() ? optPage.get() : 1;
		PaginationInfo info = watchHistoryService.getWatchHistoryPagingList(page, PageSetting.STORAGE_PAGE_NUM.get(), order);
		return ResponseEntity.ok(info.getPagingResponse());
	}
}
