package storybird.enums;

public enum PageSetting {
	//페이지 당 보여지는 컨텐츠 수
	PER_PAGE_NUM(15),
	//페이지 당 보여지는 컨텐츠 수
	RANK_PER_PAGE_NUM(12),
	//페이지 당 보여지는 컨텐츠 수
	WEBTOON_PER_PAGE_NUM(10),
	//admin 페이지 당 보여지는 컨텐츠 수
	ADIM_PER_PAGE_NUM(12),
	//화면 하단에 표시될 page목록 갯수
	PAGE_SIZE(10),
	//게시물 화면 하단에 표시될 page목록 갯수
	BOARD_PAGE_SIZE(10),
	//팝업 화면 하단에 표시될 page목록 갯수
	POPUP_PAGE_SIZE(5),
	//팝업 페이지 당 보여지는 컨텐츠 수
	POPUP_PAGE_NUM(10),
	//팝업 페이지 당 보여지는 컨텐츠 수
	BOARD_PAGE_NUM(5),
	//보관함 페이지 당 보여지는 컨텐츠수
	STORAGE_PAGE_NUM(10),
	//요금제 이용내역 페이지 당 보여지는 컨텐츠 수
	MEMBERSHIP_PAGE_NUM(4),
	//메인 상단 작품 목록 보여지는 컨텐츠 수
	MAIN_OPUS_PAGE_NUM(15),
	//메인 검색 작품 컨텐츠수
	SEARCH_OPUS_PAGE_NUM(15),
	//댓글 페이지 당 보여지는 컨텐츠 수
	REPLY_PAGE_NUM(10),
	//댓글 페이지 하단에 표시될 page 목록 갯수
	REPLY_PAGE_SIZE(10),
	//메인 베너 최대 갯수
	MAX_MAIN_BANNER_NUM(8),
	//신작 베너 최대 갯수
	MAX_NEW_BANNER_NUM(4);

	private int num;
	
	PageSetting(int num){
		this.num = num;
	}
	
	public int get() {
		return num;
	}
	
	@Override
	public String toString() {
		return Integer.toString(num);
	}
}
