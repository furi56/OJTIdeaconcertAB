package storybird.util;

import java.io.*;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MailSendUtil {

	final String EMAIL_ID="##이메일 입력";
	final String EMAIL_PW="##비밀번호 입력";
	final String EMAIL_SENDER_NAME="##보낸사람 이름";

	int size;
	// 회원가입 보안코드 메일 html
	@Value("classpath:templates/mail/mail_auth.html")
	private Resource authMail;
	// 비밀번호 찾기 메일 html
	@Value("classpath:templates/mail/mail_forgot_password.html")
	private Resource findPwMail;



	private HtmlEmail initEmailSender() {
		HtmlEmail initEmail = new HtmlEmail();
		/*
		* HtmlEmail을 사용하여 메일 옵션 설정
		*/
		return initEmail;
	}

	//회원가입 보안코드 메일 보내기
	public String sendAuthEmail(String emailTo, String subject, String hashKey) {
		String message = createMail(authMail).replace("%change", hashKey);
		return sendEmail(emailTo, subject, message);
	}
	//비밀번호 찾기 메일 보내기
	public String sendFindPwEmail(String emailTo, String subject, String hashKey) {
		String message = createMail(findPwMail).replace("%change",
				"<a href='http://localhost:3000/modifypw?authKey="+hashKey+"' target='_blank'>비밀번호 변경</a>");
		return sendEmail(emailTo, subject, message);
	}

	private String createMail(Resource mail) {
		StringBuilder mailBuilder = new StringBuilder();
		/*
		* Resource mail 파일을 읽어 드리고 Text를 추출해 mailBuilder에 저장
		 */
		return mailBuilder.toString();
	}

	private String sendEmail(String emailTo, String subject, String msg) {
		HtmlEmail sendemail = initEmailSender();
			/*
			* HtmlEmail 을 사용해서 메일 전송 기능 구현
			*/
		return "OK";
	}
}
