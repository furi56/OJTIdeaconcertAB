package storybird.controller;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.annotations.ApiOperation;
import storybird.enums.FilePath;
import storybird.util.FileUtil;

/**
 * 파일 관련 기능 컨트롤러
 * 파일 가져오기, 다운로드
 */

@Controller
public class FileController {

	@ApiOperation(value = "이미지 불러오기 ex) /image/opus/epi/file.jpg")
	@GetMapping(value = "/image/{type}/{type2}/{file_name:.+}")
	public void getFileData(@PathVariable("type") String type, @PathVariable("type2") String type2,
												@PathVariable("file_name") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Path path = Paths.get(FilePath.DEFAULT_UPLOAD_PATH + type + "/" + type2 + "/" + fileName);
		getFileData(request, response, path);
	}

	@ApiOperation(value = "이미지 불러오기 ex) /image/opus/file.jpg")
	@GetMapping(value = "/image/{type}/{file_name:.+}")
	public void getEventFileData(@PathVariable("type") String type, @PathVariable("file_name") String fileName,
													 HttpServletRequest request, HttpServletResponse response) throws Exception {
		Path path = Paths.get(FilePath.DEFAULT_UPLOAD_PATH + type + "/" + fileName);
		getFileData(request, response, path);
	}

	@ApiOperation(value = "다운로드1")
	@GetMapping(value = "/download/{type}/{type2}/{file_name:.+}")
	public void downloadFileData(@PathVariable("type") String type, @PathVariable("type2") String type2,
								 @PathVariable("file_name") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePath = FilePath.DEFAULT_UPLOAD_PATH + type + "/" + type2 + "/" + fileName;
		downloadFileData(request, response, filePath);

	}

	@ApiOperation(value = "이미지 다운로드2")
	@GetMapping(value = "/download/{type}/{file_name:.+}")
	public void downloadFileData(@PathVariable("file_name") String fileName, @PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePath = FilePath.DEFAULT_UPLOAD_PATH + "/" + type + "/" + fileName;
		downloadFileData(request, response, filePath);
	}

	@ApiOperation(value = "작품 썸네일 불러오기")
	@GetMapping(value = "/contents/{type}/{opus_no}/thumb/{file_name:.+}")
	public void getOpusFileData(@PathVariable("opus_no") String opus_no, @PathVariable("type") String type,
													@PathVariable("file_name") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Path path = Paths.get(FilePath.DEFAULT_UPLOAD_PATH + type + "/" + opus_no + "/thumb/" + fileName);
		getFileData(request, response, path);
	}
	@ApiOperation(value = "Episode File load", notes = "에피소드 파일 불러오기")
	@GetMapping(value = "/contents/{type}/{opus_no}/{epi_num}/epi/{file_name:.+}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void getEpisodeFileData(@PathVariable("opus_no") String opus_no, @PathVariable("type") String type,
														   @PathVariable("file_name") String fileName, @PathVariable("epi_num") String epiNum,
														   HttpServletRequest request, HttpServletResponse response) throws IOException {
		String dest = FilePath.DEFAULT_UPLOAD_PATH + type + "/" + opus_no + "/" + epiNum + "/epi/";

		Path path = Paths.get(FilePath.DEFAULT_UPLOAD_PATH + type + "/" + opus_no + "/" + epiNum + "/epi/" + fileName);
		getFileData(request, response, path);
	}

	private void getFileData(HttpServletRequest request, HttpServletResponse response, Path path) throws IOException {

		ServletOutputStream outStream = response.getOutputStream();
		FileInputStream inputStream = null;

		try {
//		boolean isReferer = request.getHeader("Referer") != null ? request.getHeader("Referer").contains("localhost:8089") : false;
//		
//		if(!isReferer) {
//			response.getOutputStream().write("Error".getBytes());
//			response.getOutputStream().flush();
//			return null;
//		}
			inputStream = new FileInputStream(path.toFile());

			response.setHeader("Access-Control-Allow-Origin", "*");
			String contentType = "";

			contentType = Files.probeContentType(path);


			response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
			// http://yoonbumtae.com/?p=2891 참조 링크
			if (contentType.contains("audio")) {
				// Range 추출
				Long startRange = 0L;
				Long endRange = 0L;
				Boolean isPartialRequest = false;
				try {
					if (request.getHeader("range") != null) {
						String rangeStr = request.getHeader("range");

						String[] range = rangeStr.replace("bytes=", "").split("-");
						startRange = range[0] != null ? Long.parseLong(range[0]) : 0L;
						if (range[1] != null) {
							endRange = Long.parseLong(range[1]);
							isPartialRequest = true;
						}
					}
				} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {

				}

				// 헤더 설정
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "filename=\"audio\"");
				response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
				response.setHeader("Content-Transfer-Encoding", "binary");
				// 부분 범위 리퀘스트인지, 젠체 범위 리퀘스트인지에 따라 Content-Range값을 다르게
				if (isPartialRequest) {
					response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + startRange + "-" + endRange + "/" + path.toFile().length());
				} else {
					response.setHeader(HttpHeaders.CONTENT_LENGTH, path.toFile().length() + "");
					response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes 0-" + path.toFile().length() + "/" + path.toFile().length());
					startRange = 0L;
					endRange = path.toFile().length();
				}

				// 랜덤 액세스 파일을 이용해 음성파일을 범위로 읽기
				try (RandomAccessFile randomAccessFile = new RandomAccessFile(path.toFile(), "r")) {
					Integer bufferSize = 1024, data = 0;
					byte[] b = new byte[bufferSize];
					Long count = startRange;
					Long requestSize = endRange - startRange + 1;

					//startRange에서 시작
					randomAccessFile.seek(startRange);

					while (true) {
						// 버퍼 사이즈(1024)보다 범위가 작으면
						if (requestSize <= 2) {
							outStream.flush();
							break;
						}
						// 나머지는 일반적으로 진행
						data = randomAccessFile.read(b, 0, b.length);
						// count가 endRange 이상이면 요청 범위를 넘어선 것이므로 종료료
						if (count <= endRange) {
							outStream.write(b, 0, data);
							count += bufferSize;
							randomAccessFile.seek(count);
						} else {
							break;
						}
					}
					outStream.flush();
				}
			}else{
				FileCopyUtils.copy(inputStream, outStream);
			}

		} catch (Exception e) {
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
			outStream.close();
		}

	}
	
	private void downloadFileData(HttpServletRequest request, HttpServletResponse response, String path) throws Exception{
//		boolean isReferer = request.getHeader("Referer") != null ? request.getHeader("Referer").contains("localhost:8089") : false;
//		
//		if(!isReferer) {
//			response.getOutputStream().write("Error".getBytes());
//			response.getOutputStream().flush();
//			return null;
//		}
		String fileName = request.getParameter("fileName");
		File downloadFile = new File(path);
		FileInputStream inputstream = new FileInputStream(downloadFile);
		
		String mimeType = request.getServletContext().getMimeType(path);
		if(mimeType == null) {
			mimeType = "application/octet-stream";
		}
		String ext = FileUtil.getFileExt(downloadFile.getName());
		String disposStr = "attachment; filename*=" + "UTF-8" + "''"+ URLEncoder.encode(fileName+"."+ext, "UTF-8");

		response.setContentType(mimeType);
		response.setContentLength((int)downloadFile.length());
		response.setHeader("Content-Disposition", disposStr);
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		ServletOutputStream outStream = response.getOutputStream();
		
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		
		while((bytesRead = inputstream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputstream.close();
		outStream.close();
	}
}
