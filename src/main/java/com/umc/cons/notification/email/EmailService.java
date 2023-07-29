package com.umc.cons.notification.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.umc.cons.notification.domain.entity.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

	private final JavaMailSender javaMailSender;

	public void sendMail(Notification notification) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		String title = notification.getTitle();

		simpleMailMessage.setTo(notification.getEmail());
		simpleMailMessage.setSubject(title + "보러가기");
		simpleMailMessage.setText(title + "보러갈 시간");

		try {
			javaMailSender.send(simpleMailMessage);
		} catch (Exception e) {
			log.info(notification.getEmail() + " 전송 실패");
		}
	}
}
