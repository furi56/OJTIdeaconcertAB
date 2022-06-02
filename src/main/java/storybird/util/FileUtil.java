package storybird.util;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.web.multipart.MultipartFile;
import storybird.enums.ErrorCode;
import storybird.enums.FilePath;
import storybird.enums.FileType;
import storybird.enums.ImageSize;
import storybird.exception.ToonflixErrorException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FileUtil {
	
	//시간을 사용한 랜덤 파일 이름 반환
	public static String randomFileName() { 
		return System.currentTimeMillis()+"";
	}
	//UUID를 사용한 랜덤 파일 이름 반환 
	public static String randomUUIDFileName() {
		return UUID.randomUUID().toString();
	}
	public static String getFileExt(String fileName) {
		if(fileName.lastIndexOf(".") >= 0) {
			return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase().toLowerCase();
		}else {
			return "";
		}
	}
	//단일 디렉토리 생성
	public static void mkDir(String dest) throws Exception{
		File f = new File(dest);
		f.mkdir();
	}
	//여러 디렉토리 동시 생성
	public static void mkDirs(String dest) throws Exception{
		File f = new File(dest);
		f.mkdirs();
	}
	//파일 제거
	public static void delFile(String filePath) throws Exception{
		File f = new File(filePath);
		f.delete();
	}
	
	//디렉토리 제거
	public static void delDir(String dest) throws Exception{
		File directory = new File(dest);
		File[] fileList;
		while(directory.exists() && directory.isDirectory()) {
			fileList = directory.listFiles();
			if(fileList != null && directory.exists()) {
				for(int i=0; i<fileList.length; i++) {
					fileList[i].delete();
				}
				directory.delete();
			}
			
		}
	}
	// 디렉토리 내부 파일 제거
	public static void delDirInFile(String dest) throws Exception{
		File directory = new File(dest);
		File[] fileList;
		if(directory.exists() && directory.isDirectory()) {
			fileList = directory.listFiles();
			if(fileList != null && directory.exists()) {
				for(int i=0; i<fileList.length; i++) {
					fileList[i].delete();
				}
			}
			
		}
	}
	//디렉토리 내부 특정 파일 불러오기
	public static String getDirInFile(String dest, String fileName) throws Exception{
		File directory = new File(dest);
		File[] fileList;
		if(directory.exists() && directory.isDirectory()) {
			fileList = directory.listFiles();
			if(fileList != null && directory.exists()) {
				for(int i=0; i<fileList.length; i++) {
					String name = fileList[i].getName().substring(0,fileList[i].getName().lastIndexOf("."));
					System.out.println("getDirInFile >> "+name);
					if(name.equals(fileName)) return fileList[i].getName();
				}
			}
		}
		throw new Exception();
	}
	//디렉토리 내부 파일 갯수
	public static int getDirFileCount(String dest) throws Exception{
		File directory = new File(dest);
		return directory.listFiles().length;
	}
	
	//파일 업로드
	public static String uploadFile(String dest, MultipartFile file) throws Exception{
		return uploadFile(dest, file, randomFileName());
	}
	//파일 업로드
	public static String uploadUUIDFile(String dest, MultipartFile file) throws Exception{
		String ext = getFileExt(file.getOriginalFilename());
		String fileName = randomUUIDFileName();
		File checkFile = new File(dest+fileName+"."+ext);
		while(checkFile.exists()) {
			fileName = randomUUIDFileName();
			checkFile = new File(dest+fileName+"."+ext);
		}
		return uploadFile(dest, file, fileName);
	}

	//이미지 파일 resize 후 업로드
	public static String uploadImageFile(String dest, MultipartFile file, String saveName, ImageSize size) throws Exception {
		mkDirs(dest);
		String ext = getFileExt(file.getOriginalFilename());
		String[] supFormat = { "bmp", "jpg", "jpeg", "png"};
		if(!Arrays.asList(supFormat).contains(ext)){
			throw new ToonflixErrorException("", ErrorCode.INVALID_EXTENSION);
		}
		String fileName = saveName +"."+ ext;
		File saveFile = new File(dest+"/"+fileName);
		String savePath = saveFile.getAbsolutePath().replaceAll("\\\\", "/");

		resize(file, savePath, ext, size);
		return savePath.replace(FilePath.DEFAULT_UPLOAD_PATH.getPath(), "");
	}

	//파일 업로드 저장되는 파일명 지정 가능
	public static String uploadFile(String dest, MultipartFile file, String saveName) throws Exception{
		String ext = getFileExt(file.getOriginalFilename());
		return uploadFile(dest, file, saveName, ext);
	}
	//파일 업로드 파일 확장자 지정 가능
	public static String uploadFile(String dest, MultipartFile file, String saveName, String ext) throws Exception{
		mkDirs(dest);
		String fileName = saveName +"."+ ext;
		File saveFile = new File(dest+"/"+fileName);
		String savePath = saveFile.getAbsolutePath().replaceAll("\\\\", "/");

		file.transferTo(saveFile);
		return savePath.replace(FilePath.DEFAULT_UPLOAD_PATH.getPath(), "");
	}
	//다수 파일 업로드
	public static String uploadFiles(String dest, List<MultipartFile> files) throws Exception {
		mkDir(dest);
		for(int i = 0; i < files.size(); i++) {
			String ext = getFileExt(files.get(i).getOriginalFilename());
			String fileName = (i+1) +"."+ ext;
			
			File saveFile = new File(dest+"/"+fileName);
			
			files.get(i).transferTo(saveFile);
		}

		dest = dest.replaceAll("\\\\", "/");
		return dest.replace(FilePath.DEFAULT_UPLOAD_PATH.getPath(), "");
	}

	public static long getAudioDuration(String filePath){
		try{
			File file = new File(filePath);

			AudioFile f = AudioFileIO.read(file);
			AudioHeader ah = f.getAudioHeader();

			long trackLength = (long)ah.getTrackLength();

			System.out.println("Duration in Integer : "+trackLength);
			return trackLength;
		}catch(Exception e){
			throw new ToonflixErrorException("파일 접근 실패", ErrorCode.INVALID_TYPE_VALUE);
		}
	}
	
	public static void resize(MultipartFile file, String filePath, String formatName, ImageSize size) throws IOException{
		BufferedImage inputImage = ImageIO.read(file.getInputStream());

		int originWidth = inputImage.getWidth();
		int originHeight = inputImage.getHeight();

		int width = size.getWidth();
		int height = size.getHeight();

		if(originWidth != width || originHeight != height){
			Image resizeImage = inputImage.getScaledInstance(width, height, Image.SCALE_FAST);
			BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = outputImage.createGraphics();
			graphics2D.drawImage(resizeImage, 0,0,null);
			graphics2D.dispose();

			File newFile = new File(filePath);
			ImageIO.write(outputImage, formatName, newFile);
		}else{
			file.transferTo(new File(filePath));
		}

	}

	public static boolean isFileContentType(MultipartFile file, FileType fileType){
		String contentType = file.getContentType();

		return contentType.contains(fileType.getType());
	}
}
