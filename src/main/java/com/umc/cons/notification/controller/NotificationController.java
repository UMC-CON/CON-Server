package com.umc.cons.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc.cons.common.annotation.LoginMember;
import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.content.service.ContentService;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.notification.domain.entity.Notification;
import com.umc.cons.notification.dto.NotificationDto;
import com.umc.cons.notification.dto.NotificationRequestDto;
import com.umc.cons.notification.dto.NotificationResponseDto;
import com.umc.cons.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;
	private final ContentService contentService;

	@PostMapping()
	public BaseResponse<BaseResponseStatus> registerNotification(@RequestBody NotificationDto notificationDto,
		@LoginMember Member member) {

		Content content = contentService.findOne(notificationDto.getContentId());
		Notification notification = NotificationDto.toEntity(notificationDto, member, content);
		notificationService.registerNotification(notification);

		return new BaseResponse(BaseResponseStatus.SUCCESS);
	}

	@GetMapping
	public BaseResponse<List<NotificationResponseDto>> getNotifications(@LoginMember Member member) {
		List<NotificationResponseDto> notifications = notificationService.getNotifications(member);

		return new BaseResponse<>(notifications);
	}

	@DeleteMapping
	public BaseResponse<BaseResponseStatus> deleteNotification(@RequestParam String uuid) {
		notificationService.deleteNotification(uuid);

		return new BaseResponse(BaseResponseStatus.SUCCESS);
	}

	@PutMapping
	public BaseResponse<NotificationResponseDto> updateNotification(@RequestBody NotificationRequestDto requestDto) {
		Content content = contentService.findOne(requestDto.getContentId());

		Notification notification = notificationService.updateNotification(requestDto, content);
		NotificationResponseDto responseDto = NotificationResponseDto.of(notification);

		return new BaseResponse<>(responseDto);
	}

}
