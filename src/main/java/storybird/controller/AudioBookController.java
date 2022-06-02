package storybird.controller;

import java.util.*;

import javax.security.auth.message.AuthException;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import storybird.enums.ErrorCode;
import storybird.enums.FileType;
import storybird.exception.ErrorResponse;
import storybird.exception.ToonflixErrorException;
import storybird.security.ToonflixUsers;
import storybird.service.*;
import storybird.util.PaginationInfo;
import storybird.vo.*;

/**
 * 오디오북 관리 컨트롤러
 */

@RestController(value = "오디오북 컨트롤러")
public class AudioBookController {

	@Autowired OpusService opusService;
	@Autowired EpisodeService episodeService;
	@Autowired WatchHistoryService watchService;

	@ApiOperation(value="오디오북홈 1~5위 실시간 랭킹 오디오북 조회")
	@GetMapping("/audio/home/time")
	public ResponseEntity<?> homeTimeList(){
		return ResponseEntity.ok(DataResponse.builder()
				.status(200)
				.message("success")
				/* ####
				* opusService.getTimeOpus 수정 필요
				*/
				.data(opusService.getTimeOpus())
				.build());
	}

	@ApiOperation(value="장르별 오디오북 조회")
	@ApiImplicitParam(
			name = "order"
			, value = "정렬순서 (1: 등록일자(오름차순), 2:등록일자(내림차순), 3:작품명(오름차순))"
			, required = false
			, dataType = "string")
	@GetMapping("/audio/genre/{optPage}")
	public ResponseEntity<?> genreList(@RequestParam(defaultValue = "ALL") String genre,
										  @PathVariable Optional<Integer> optPage, String order) {
		int page = optPage.isPresent() ? optPage.get() : 1;
		/*
		* ####
		* opusService.getGenreOpusPagingList 수정 필요
		*/
		PaginationInfo info = opusService.getGenreOpusPagingList(page, genre, order);
		return ResponseEntity.ok(info.getPagingResponse());
	}

	@ApiOperation(value="신작 오디오북 조회")
	@GetMapping("/audio/new/{optPage}")
	public ResponseEntity<?> newAudioAllList(@RequestParam(defaultValue = "ALL")String genre,
											 @PathVariable Optional<Integer> optPage, int month) {
		int page = optPage.isPresent() ? optPage.get() : 1;
		/*
		* ####
		* opusService.getOpusInMonthList 수정 필요
		*/
		PaginationInfo info = opusService.getOpusInMonthList(page, genre, month);
		return ResponseEntity.ok(info.getPagingResponse());
	}

	//==========================================오디오북 전체보기 END================================================

	@ApiOperation(value="오디오북 상세 정보 조회")
	@GetMapping("/audio/{wt_idx}")
	public ResponseEntity<?> audioInfo(@PathVariable int wt_idx) {
		/*
		* ####
		* opusService.getDetailOpus 수정 필요
		*/
		OpusVo vo = opusService.getDetailOpus(wt_idx);

		return ResponseEntity.ok(DataResponse.builder()
				.status(200)
				.message("success")
				.data(vo)
				.build());
	}

	@ApiOperation(value="오디오북 회차 전체 리스트 조회")
	@GetMapping("/audio/{wt_idx}/epi")
	@ApiImplicitParam(name="order", type="string", value="인기순:HOT, 회차 내림차순:NEW, 회차 오름차순:OLD")
	public ResponseEntity<?> audioEpisodeList(@PathVariable int wt_idx, String order) {

		return ResponseEntity.ok(DataResponse.builder()
				.status(200)
				.message("success")
				/* ####
				*  episodeService.getEpisodeOpusAllList 수정 필요
				*/
				.data(episodeService.getEpisodeOpusAllList(wt_idx ,order))
				.build());
	}

	@ApiOperation(value="오디오북 열람")
	@GetMapping("/audio/viewer/{epi_idx}")
	public ResponseEntity<?> viewer(@PathVariable int epi_idx) throws Exception {
			// 해당 메소드를 테스트할때 AccessToken이 헤더로 넘어와야 합니다!!
		/* ####
		* episodeService.getDetailEpisode 수정 필요
		* */
		EpisodeVo episodevo = episodeService.getDetailEpisode(epi_idx);

		int wt_idx = episodevo.getOpus_no();
		Map<String,Object> paramMap = new HashMap<>();
		/* ####
		* episodeService.getEpisodePrebyNext 수정 필요
		*/
		episodeService.getEpisodePrebyNext(episodevo, wt_idx, epi_idx);

		try{
			ToonflixUsers user = ToonflixUsers.getAuthorizedUsers();
			int watchMemNo = user.getUser_no();
			/* ####
			* watchService.saveWatchHistory 수정 필요
			*/
			watchService.saveWatchHistory(wt_idx, watchMemNo, epi_idx);
		}catch(UsernameNotFoundException e){

		}
		/*
		* episodeService.getEpisodeFile 수정 필요
		*/
		paramMap.put("EpisodeFile",episodeService.getEpisodeFile(wt_idx, epi_idx, FileType.CALL_EPISODE.getKind()));
		paramMap.put("ArtWorkFile",episodeService.getEpisodeFile(wt_idx, epi_idx, FileType.CALL_ARTWORK.getKind()));
		paramMap.put("EpisodeDetail",episodevo);
		return ResponseEntity.ok(DataResponse.builder()
				.status(200)
				.message("success")
				.data(paramMap)
				.build());
	}


}
