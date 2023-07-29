package com.umc.cons.notification.email;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.umc.cons.notification.domain.entity.Notification;
import com.umc.cons.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final NotificationService notificationService;

	@Scheduled(cron = "0 * * * * *")
	@Async
	public void sendMail() {
		// Get current time
		LocalDateTime now = LocalDateTime.now();

		// Find notifications with matching time from the database
		List<Notification> notifications = notificationService.findNotificationsByTime(now);

		for (Notification notification : notifications) {
			sendEmailToUser(notification);
		}
	}

	private void sendEmailToUser(Notification notification) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		String title = notification.getTitle();

		simpleMailMessage.setTo(notification.getEmail());
		simpleMailMessage.setSubject(title + "보러가기");
		simpleMailMessage.setText(title + "보러갈 시간");

		try {
			javaMailSender.send(simpleMailMessage);
			log.info("Email sent to: " + notification.getEmail());
		} catch (Exception e) {
			log.error("Failed to send email to: " + notification.getEmail(), e);
		}
	}
}

