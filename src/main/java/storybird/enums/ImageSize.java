package storybird.enums;

public enum ImageSize {
	OPUS_THUMBNAIL(214,214),
	OPUS_HEADER_IMAGE(190,290),
	USER_PROFILE(90,90),
	MAIN_BANNER_IMAGE(636,460),
	MAIN_BANNER_BACKGROUND(1200,494),
	NEW_BANNER_IMAGE(585,316),
	EVENT_IMAGE(585,236),
	;
	
	private int width;
	private int height;
	
	ImageSize(int witdh, int height){
		this.width = witdh;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
