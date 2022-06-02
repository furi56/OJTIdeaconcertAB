package storybird.enums;

public enum FilePath {
	// 로컬 경로

	DEFAULT_UPLOAD_PATH("C:/temp_file/story-bird/"),
	AUDIO_PATH("C:/temp_file/story-bird/audio/"),
	MOVIE_PATH("C:/temp_file/story-bird/movie/"),
	WEBBOOK_PATH("C:/temp_file/story-bird/webbook/"),
	PARTNER_LICENSE_PATH("C:/temp_file/story-bird/partner/license/"),
	PARTNER_ACCOUNT_PATH("C:/temp_file/story-bird/partner/acc/"),
	PARTNER_PROFILE_PATH("C:/temp_file/story-bird/partner/profile/"),
	AUTHOR_ACCOUNT_PATH("C:/temp_file/story-bird/author/acc/"),
	AUTHOR_PROFILE_PATH("C:/temp_file/story-bird/author/profile/"),
	USER_PROFILE("C:/temp_file/story-bird/user/profile/"),
	ADMIN_PROFILE("C:/temp_file/story-bird/admin/profile/"),
	BANNER_MAIN_IMAGE("C:/temp_file/story-bird/banner/main/"),
	BANNER_LONG_IMAGE("C:/temp_file/story-bird/banner/long/"),
	BANNER_RECOM_IMAGE("C:/temp_file/story-bird/banner/recom/"),
	CONTRACT("C:/temp_file/story-bird/contract/"),
	EVENT("C:/temp_file/story-bird/event/"),
	POPUP_IMAGE("C:/temp_file/story-bird/popup/"),
	FAVICON_IMAGE("C:/temp_file/story-bird/favicon/"),
	LOGO_IMAGE("C:/temp_file/story-bird/logo/"),
	COPYRIGHT_MARK_IMAGE("C:/temp_file/story-bird/copyright/"),
	MAINTENANCE_FILE("C:/temp_file/story-bird/maintenance/"),
	INQUIRY_FILE("C:/temp_file/story-bird/inquiry/"),
	SERIAL_INQUIRY_FILE("C:/temp_file/story-bird/s-inquiry/"),
	THUMBNAIL("thumb/"),
	EPISODE("epi/");
	
//서버 경로
/*
	DEFAULT_UPLOAD_PATH("/home/resources/data/storybird/"),
	AUDIO_PATH("/home/resources/data/storybird/audio/"),
	MOVIE_PATH("/home/resources/data/storybird/movie/"),
	WEBBOOK_PATH("/home/resources/data/storybird/webbook/"),
	PARTNER_LICENSE_PATH("/home/resources/data/storybird/partner/license/"),
	PARTNER_ACCOUNT_PATH("/home/resources/data/storybird/partner/acc/"),
	PARTNER_PROFILE_PATH("/home/resources/data/storybird/partner/profile/"),
	AUTHOR_ACCOUNT_PATH("/home/resources/data/storybird/author/acc/"),
	AUTHOR_PROFILE_PATH("/home/resources/data/storybird/author/profile/"),
	USER_PROFILE("/home/resources/data/storybird/user/profile/"),
	ADMIN_PROFILE("/home/resources/data/storybird/admin/profile/"),
	BANNER_MAIN_IMAGE("/home/resources/data/storybird/banner/main/"),
	BANNER_LONG_IMAGE("/home/resources/data/storybird/banner/long/"),
	BANNER_RECOM_IMAGE("/home/resources/data/storybird/banner/recom/"),
	CONTRACT("/home/resources/data/storybird/contract/"),
	EVENT("/home/resources/data/storybird/event/"),
	POPUP_IMAGE("/home/resources/data/storybird/popup/"),
	FAVICON_IMAGE("/home/resources/data/storybird/favicon/"),
	LOGO_IMAGE("/home/resources/data/storybird/logo/"),
	COPYRIGHT_MARK_IMAGE("/home/resources/data/storybird/copyright/"),
	MAINTENANCE_FILE("/home/resources/data/storybird/maintenance/"),
	INQUIRY_FILE("/home/resources/data/storybird/inquiry/"),
	SERIAL_INQUIRY_FILE("/home/resources/data/storybird/s-inquiry/"),
	THUMBNAIL("thumb/"),
	EPISODE("epi/");
*/
	private String path;
	
	FilePath(String path){
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	
	@Override
	public String toString() {
		return path;
	}
}
